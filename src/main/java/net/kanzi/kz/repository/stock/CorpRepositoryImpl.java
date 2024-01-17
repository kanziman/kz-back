package net.kanzi.kz.repository.stock;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import net.kanzi.kz.domain.stock.Ticker;
import net.kanzi.kz.domain.stock.TickerQ;
import net.kanzi.kz.dto.stock.TickerListResponse;
import net.kanzi.kz.dto.stock.TickerQResponse;
import net.kanzi.kz.dto.stock.TickerRequest;
import net.kanzi.kz.dto.stock.TickerResponse;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CorpRepositoryImpl {
    private final EntityManager em;


    @Transactional
    public Map insertProxy(List proxy){

        String del = "delete from proxy";
        Query query = em.createNativeQuery(del);
        int delRows = query.executeUpdate();

        String sql = "INSERT INTO proxy (ip) VALUES (?)";
        query = em.createNativeQuery(sql);

        int insRows = 0;
        for(int i=0; i<proxy.size(); i++){
            query.setParameter(1, proxy.get(i));
            insRows += query.executeUpdate();
        }

        Map map = new HashMap();
        map.put("count",insRows);
        return map;
    }

    private static final String NORMAL_STOCK = "보통주";
    public List findAllTickers(){

        String sql = "SELECT distinct 종목코드 as stockCode , 종목명 as stockName, 시장구분 as mktType FROM kor_ticker_today where 종목구분=:type";
        Query query = em.createNativeQuery(sql)
                .setParameter("type",NORMAL_STOCK);

        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List resultList = nativeQuery.getResultList();

        return resultList;
    }

    public  List<Ticker> findByCode(String stockCode) {
        return em.createQuery("select t from Ticker t where t.stockCode = :stockCode", Ticker.class)
                .setMaxResults(6000)
                .setParameter("stockCode", stockCode)
                .getResultList();
    }
    public Map findByCodeQ(TickerRequest request) {

        List<TickerQ> tickersQ = em.createQuery("select t from TickerQ t  where t.stockCode = :stockCode and t.fsType=:fsOption", TickerQ.class)
                .setParameter("stockCode", request.getCode())
                .setParameter("fsOption", request.getOption())
                .getResultList();
        List<Ticker> tickers = em.createQuery("select t from Ticker t where t.stockCode = :stockCode and t.fsType=:fsOption", Ticker.class)
                .setParameter("stockCode", request.getCode())
                .setParameter("fsOption", request.getOption())
                .getResultList();

        List<TickerResponse> tickerResponses = tickers.stream()
                .map(t -> TickerResponse.of(t))
                .collect(Collectors.toList());

        List<TickerQResponse> tickerQResponses = tickersQ.stream()
                .map(t -> TickerQResponse.of(t))
                .collect(Collectors.toList());

        Map returnMap = new HashMap<>();
        returnMap.put("tickerQ", tickerQResponses);
        returnMap.put("ticker", tickerResponses);

        return returnMap;
    }
}
