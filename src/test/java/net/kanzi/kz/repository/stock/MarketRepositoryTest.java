package net.kanzi.kz.repository.stock;

import jakarta.persistence.EntityManager;
import net.kanzi.kz.data.queryDto.*;
import org.assertj.core.api.Assertions;
import org.hibernate.transform.Transformers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.*;

@SpringBootTest
@ActiveProfiles("local")
class MarketRepositoryTest {
    @Autowired
    MarketRepository marketRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("시장 티커를 조회할수있다.(네이티브쿼리)")
    void getMarketTicker(){

        String sql = "SELECT 종목코드 as stockCode, 시장구분 as marketType FROM kor_ticker_today";
        List<KorTicker> tickerList = em.createNativeQuery(sql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(KorTicker.class))
                .getResultList();

        Assertions.assertThat(tickerList.size() > 1000);
    }

}