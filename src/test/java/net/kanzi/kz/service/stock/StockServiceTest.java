package net.kanzi.kz.service.stock;

import net.kanzi.kz.dto.stock.MarketResponse;
import net.kanzi.kz.dto.stock.TickerListResponse;
import net.kanzi.kz.dto.stock.TickerRequest;
import net.kanzi.kz.dto.stock.TickerResponse;
import net.kanzi.kz.repository.stock.CorpRepository;
import net.kanzi.kz.repository.stock.CorpRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private CorpService corpService;
    @Autowired
    private MarketService marketService;

    @Autowired
    private CorpRepositoryImpl corpRepository;
    @Test
    @DisplayName("전 종목 리스트를 가져올 수 있다.")
    public void getAllTickers(){
        List<TickerListResponse> tickerList = corpService.findAllTicker();
        assertThat(tickerList.get(0).getStockCode()).isEqualTo("000020");
    }

    @Test
    @DisplayName("2000.1.4. 이후의 market 데이터를 가져올 수 있다.")
    public void getTickers(){
        List<MarketResponse> marketResponses = marketService.findAll();
        assertThat(marketResponses.get(0).getBaseDate()).isEqualTo(LocalDateTime.of(2000,1,4,0,0));
    }

    @Test
    public void getTicker(){
        TickerRequest tick = TickerRequest.builder()
                .code("000020")
                .option("연결재무제표")
                .build();
        Map byCodeQ = corpRepository.findByCodeQ(tick);
        assertThat(byCodeQ.get("ticker")).isNotNull();
        assertThat(byCodeQ.get("tickerQ")).isNotNull();
    }
}