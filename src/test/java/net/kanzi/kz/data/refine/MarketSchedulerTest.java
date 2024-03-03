package net.kanzi.kz.data.refine;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
class MarketSchedulerTest {

    @Autowired
    MarketRefineService marketRefineService;

    @Autowired
    MarketScheduler marketScheduler;


    @Test void adrList() throws SQLException {
//        marketScheduler.scheduleTask();

        List<DailyMarketSummary> adrList = marketRefineService.getCountAdrReduce();
        System.out.println("adrList = " + adrList);

        assertThat(adrList.size()).isGreaterThan(0);
    }

    @Test void getMarketListAfterTargetAdrDate() throws SQLException {

        LocalDate yesterday = LocalDate.now().minusMonths(2);

        // MARKET LIST >= ADR[0].baseDate
        List<MarketRefine> marketListAfterTargetAdrDate = marketRefineService.getMarketListAfterTargetAdrDate(Date.valueOf(yesterday));
        log.info("marketListAfterTargetAdrDate : {}", marketListAfterTargetAdrDate);

        assertThat(marketListAfterTargetAdrDate.size()).isGreaterThan(0);
    }

}