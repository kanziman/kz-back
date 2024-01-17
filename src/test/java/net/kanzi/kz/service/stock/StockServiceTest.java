package net.kanzi.kz.service.stock;

import net.kanzi.kz.domain.stock.Market;
import net.kanzi.kz.dto.stock.MarketResponse;
import net.kanzi.kz.dto.stock.TickerListResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CorpServiceTest {

    @Autowired
    private CorpService corpService;
    @Autowired
    private MarketService marketService;

    @Test
    @DisplayName("전 종목 리스트를 가져올 수 있다.")
    public void getAllTickers(){
        List<TickerListResponse> tickerList = corpService.findAllTicker();
        Assertions.assertThat(tickerList.get(0).getStockCode()).isEqualTo("000020");
    }

    @Test
    @DisplayName("market 데이터를 가져올 수 있다.")
    public void getTickers(){


        List<MarketResponse> marketResponses = marketService.findAll();

        System.out.println("marketResponses = " + marketResponses.get(0).getBaseDate());
        Assertions.assertThat(marketResponses.get(0).getBaseDate()).isEqualTo(LocalDateTime.of(2000,1,4,0,0));

    }

}