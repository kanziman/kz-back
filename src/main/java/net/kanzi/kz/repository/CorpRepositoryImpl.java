package net.kanzi.kz.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import net.kanzi.kz.domain.Ticker;
import net.kanzi.kz.domain.TickerQ;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CorpRepositoryImpl {
    private final EntityManager em;


    @Transactional
    public Map insertProxy(List proxy){

        String del = "delete from proxy";
        Query query = em.createNativeQuery(del);
        int delRows = query.executeUpdate();
        System.out.println("delRows = " + delRows);

        String sql = "INSERT INTO proxy (ip) VALUES (?)";
        query = em.createNativeQuery(sql);

        int insRows = 0;
        for(int i=0; i<proxy.size(); i++){
            query.setParameter(1, proxy.get(i));
            insRows += query.executeUpdate();
        }
        System.out.println("insRows = " + insRows);

        Map map = new HashMap();
        map.put("count",insRows);
        return map;
    }


    public List<Map<String, Object>> findAllTickers(){
        String sql = "SELECT 종목코드 as stockCode , 종목명 as stockName, 시장구분 as mktType FROM kor_ticker_today where 종목구분='보통주'";
        Query query = em.createNativeQuery(sql);

        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String,Object>> results = nativeQuery.getResultList();

        return results;
    }

    public  List<Ticker> findByCode(String stockCode) {
        System.out.println("code here = " + stockCode);
        return em.createQuery("select t from Ticker t where t.stockCode = :stockCode", Ticker.class)
                .setMaxResults(6000)
                .setParameter("stockCode", stockCode)
                .getResultList();
    }
    public Map findByCodeQ(Map request) {
        String stockCode = request.get("code").toString();
        String fsOption = request.get("option").toString();


        List<TickerQ> tickersQ = em.createQuery("select t from TickerQ t  where t.stockCode = :stockCode and t.fsType=:fsOption", TickerQ.class)
                .setParameter("stockCode", stockCode)
                .setParameter("fsOption", fsOption)
                .getResultList();
        List<Ticker> tickers = em.createQuery("select t from Ticker t where t.stockCode = :stockCode and t.fsType=:fsOption", Ticker.class)
                .setParameter("stockCode", stockCode)
                .setParameter("fsOption", fsOption)
//                .setMaxResults(500)
                .getResultList();

        Map returnMap = new HashMap<>();
        returnMap.put("tickerQ", tickersQ);
        returnMap.put("ticker", tickers);

        return returnMap;
    }
//    Optional<User> findByEmail(String email);
//    Optional<User> findByUserId(String userId);
}
