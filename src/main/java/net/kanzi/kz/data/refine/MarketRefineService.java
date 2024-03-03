package net.kanzi.kz.data.refine;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Log4j2
public class MarketRefineService {

    private final EntityManager em;
    private final DataSource dataSource;

    private final  String priceSql = "select 종목코드 as stockCode, " +
            " ticker.marketType as segment, \n" +
            " 기준일 as baseDate, \n" +
            " 시가 as open, \n" +
            " 고가 as high, \n" +
            " 저가 as low, \n" +
            " 종가 as close, \n" +
            " 거래량 as volume \n" +
            " from kor_price price \n" +
            " join (select 종목코드 as stockCode, 시장구분 as marketType FROM kor_ticker_today) ticker \n" +
            " on price.종목코드 = ticker.stockCode " +
            " where price.기준일 >= (select date_sub(now() , interval 2 MONTH ) from dual)";

    private final String marketSql = "SELECT " +
            "a.기준일 AS baseDate, " +
            "a.시장구분 AS mktType, " +
            "a.시가 AS open, " +
            "a.고가 AS high, " +
            "a.저가 AS low, " +
            "a.종가 AS close, " +
            "a.거래량 AS volume, " +
            "a.거래대금 AS amount, " +
            "a.시가총액 AS val, " +
            "a.신용잔고 AS credit, " +
            "a.예탁금 AS deposit, " +
            "b.PER AS per, " +
            "b.PBR AS pbr, " +
            "b.DY AS dy, " +
            "b.ADR AS adr " +
            "FROM kor_market_price a " +
            "JOIN (SELECT 기준일, 시장구분, PER, PBR, DY, ADR " +
            "      FROM kor_market_value " +
            "      WHERE 지수명 IN ('코스피','코스닥')) b " +
            "ON a.기준일 = b.기준일 AND a.시장구분 = b.시장구분 " +
            "WHERE a.기준일 >= :baseDate";


    public int upsertMarketList(List<MarketRefine> marketList) throws SQLException {
        String insertQuery = "INSERT INTO kor_market (baseDate, mktType, open, high, low, close, " +
                "volume, amount, val, credit, deposit, per, pbr, dy, adr) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE open = VALUES(open), high = VALUES(high), low = VALUES(low), close = VALUES(close), volume = VALUES(volume), amount = VALUES(amount), val = VALUES(val), credit = VALUES(credit), deposit = VALUES(deposit), per = VALUES(per), pbr = VALUES(pbr), dy = VALUES(dy), adr = VALUES(adr)";

        Connection conn = dataSource.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            conn.setAutoCommit(false); // 자동 커밋 비활성화

            for (MarketRefine refine : marketList) {

                pstmt.setDate(1, refine.getBaseDate());
                pstmt.setString(2, refine.getMktType());
                pstmt.setObject(3, refine.getOpen(), java.sql.Types.FLOAT); // setObject for Float
                pstmt.setObject(4, refine.getHigh(), java.sql.Types.FLOAT); // setObject for Float
                pstmt.setObject(5, refine.getLow(), java.sql.Types.FLOAT); // setObject for Float
                pstmt.setObject(6, refine.getClose(), java.sql.Types.FLOAT); // setObject for Float
                pstmt.setObject(7, refine.getVolume(), java.sql.Types.BIGINT); // setObject for Long
                pstmt.setObject(8, refine.getAmount(), java.sql.Types.BIGINT); // setObject for Long
                pstmt.setObject(9, refine.getVal(), java.sql.Types.BIGINT); // setObject for Long
                pstmt.setObject(10, refine.getCredit(), java.sql.Types.BIGINT); // setObject for Long
                pstmt.setObject(11, refine.getDeposit(), java.sql.Types.BIGINT); // setObject for Long
                pstmt.setObject(12, refine.getPer(), java.sql.Types.FLOAT); // setObject for Float
                pstmt.setObject(13, refine.getPbr(), java.sql.Types.FLOAT); // setObject for Float
                pstmt.setObject(14, refine.getDy(), java.sql.Types.FLOAT); // setObject for Float
                pstmt.setObject(15, refine.getAdr(), java.sql.Types.FLOAT); // setObject for Float

                pstmt.addBatch(); // 배치에 추가
            }

            int[] ints = pstmt.executeBatch();// 배치 실행
            conn.commit(); // 커밋
            return ints.length;
        } catch (SQLException e) {
            conn.rollback(); // 롤백
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true); // 자동 커밋 활성화
        }
    }


    public List<DailyMarketSummary> getCountAdrReduce(){

        List<KorPrice> priceList = em.createNativeQuery(priceSql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(KorPrice.class))
                .getResultList();

        // group by 날짜
        Map<Date, List<KorPrice>> groupedByDate = priceList.stream()
                .collect(Collectors.groupingBy(KorPrice::getBaseDate));
        // 상승 종목 수, 하락 종목 수 계산
        List<DailyMarketSummary> summaries = getDailyMarketSummaries(groupedByDate);

        summaries.sort(Comparator.comparing(DailyMarketSummary::getDate));

        // 누적값 으로 ADR 계산
        List<DailyMarketSummary> adrList = getAdrList(summaries);

        return adrList;
    }

    /**
     * 일자별로 상승종목수, 하락종목수 계산
     * @param groupedByDate
     * @return List<DailyMarketSummary>
     */
    private List<DailyMarketSummary> getDailyMarketSummaries(Map<Date, List<KorPrice>> groupedByDate) {
        List<DailyMarketSummary> summaries = new ArrayList<>();
        for (Map.Entry<Date, List<KorPrice>> entry: groupedByDate.entrySet()) {
            long kospiUp = entry.getValue().stream()
                    .filter(p -> "KOSPI".equals(p.getSegment()) && p.getClose() > p.getOpen())
                    .count();
            long kospiDown = entry.getValue().stream()
                    .filter(p -> "KOSPI".equals(p.getSegment()) && p.getOpen() > p.getClose())
                    .count();
            long kosdaqUp = entry.getValue().stream()
                    .filter(p -> "KOSDAQ".equals(p.getSegment()) && p.getClose() > p.getOpen())
                    .count();
            long kosdaqDown = entry.getValue().stream()
                    .filter(p -> "KOSDAQ".equals(p.getSegment()) && p.getOpen() > p.getClose())
                    .count();

            summaries.add(new DailyMarketSummary(entry.getKey(), kospiUp, kospiDown, kosdaqUp, kosdaqDown));
        }
        return summaries;
    }



    /**
     * 시가 < 종가 : 상승+1  // 시가 > 종가 : 하락+1
     * @param summaries
     * @return List<DailyMarketSummary>
     */
    private List<DailyMarketSummary> getAdrList(List<DailyMarketSummary> summaries) {
        List<DailyMarketSummary> adrList = IntStream.range(19, summaries.size())
                .mapToObj(i -> {
                    MarketAccumulator acc = IntStream.rangeClosed(i - 19, i)
                            .mapToObj(summaries::get)
                            .reduce(new MarketAccumulator(), MarketAccumulator::accumulate, MarketAccumulator::combine);

                    float adrKospi = acc.getKospiDown() != 0 ? (float) Math.round((float) acc.getKospiUp() / acc.getKospiDown() * 10000) / 100 : 0;
                    float adrKosdaq = acc.getKosdaqDown() != 0 ? (float) Math.round((float) acc.getKosdaqUp() / acc.getKosdaqDown() * 10000) / 100 : 0;

                    DailyMarketSummary summary = summaries.get(i);
                    summary.setAdrKospi(adrKospi);
                    summary.setAdrKosdaq(adrKosdaq);
                    return summary;
                })
                .collect(Collectors.toList());
        return adrList;
    }

    public List<MarketRefine>  getMarketListAfterTargetAdrDate(Date firstDate) {
        return em.createNativeQuery(marketSql, MarketRefine.class)
                .setParameter("baseDate", firstDate)
                .getResultList();
    }

    public void joinAdrAndMarketData(List<MarketRefine> marketListAfterTargetAdrDate, List<DailyMarketSummary> adrList) {
        marketListAfterTargetAdrDate.forEach(
                market -> {
                    adrList.stream()
                            .filter(adr -> adr.getDate().equals(market.getBaseDate()))
                            .findFirst()
                            .ifPresent(
                                    adr -> {
                                        if ("KOSPI".equals(market.getMktType())){
                                            market.setAdr(adr.getAdrKospi());
                                        }
                                        else if ("KOSDAQ".equals(market.getMktType())){
                                            market.setAdr(adr.getAdrKosdaq());
                                        }
                                    }
                            );
                }
        );
    }
}
