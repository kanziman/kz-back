package net.kanzi.kz.data.refine;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.kanzi.kz.data.aop.Trace;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class MarketScheduler {

    private final MarketRefineService marketRefineService;

    @Scheduled(cron = "0 0 22 * * *")
//    @Scheduled(cron = "* */1 * * * *")
    @Trace
    public void scheduleTask() throws SQLException {

        // ADR LIST
        List<DailyMarketSummary> adrList = marketRefineService.getCountAdrReduce();
        log.info("adrList size : {}", adrList.size());
        Date firstDate = adrList.get(0).getDate();

        // MARKET LIST >= ADR[0].baseDate
        List<MarketRefine> marketListAfterTargetAdrDate = marketRefineService.getMarketListAfterTargetAdrDate(firstDate);
        log.info("marketListAfterTargetAdrDate size : {}", marketListAfterTargetAdrDate.size());


        // JOIN = ADR + MARKET
        marketRefineService.joinAdrAndMarketData(marketListAfterTargetAdrDate, adrList);
        log.info("joined marketListAfterTargetAdrDate size : {}", marketListAfterTargetAdrDate.size());

        // COMPLETE
        int result = marketRefineService.upsertMarketList(marketListAfterTargetAdrDate);
        log.info("result : {}", result);

    }

}
