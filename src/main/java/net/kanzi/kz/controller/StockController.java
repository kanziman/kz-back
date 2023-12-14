package net.kanzi.kz.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.kanzi.kz.advice.DataWrapper;
import net.kanzi.kz.domain.Market;
import net.kanzi.kz.service.CorpService;
import net.kanzi.kz.service.MarketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Log4j2
public class StockController {

    private final CorpService corpService;
    private final MarketService marketService;


    @PostMapping("/api/proxy")
    @DataWrapper
    public ResponseEntity proxyList(@RequestBody Map map) {
        List<String> lines = Arrays.asList(map.get("proxy").toString().split("\n"));

        Map rtn = corpService.insertProxy(lines);
        return ResponseEntity.status(HttpStatus.OK).body(rtn);
    }



    @PostMapping("/api/ticker")
    @DataWrapper
    public ResponseEntity getTicks(@RequestBody Map option) {
        log.info("getTicks"+option);
        Map map = corpService.findByCodeQ(option);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
    @GetMapping("/api/market")
    @DataWrapper
    public ResponseEntity getMarket() {
        List<Market> all = marketService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(all);
    }

    @GetMapping("/api/tickers")
    @DataWrapper
    public ResponseEntity getTickers() {
        List<Map<String, Object>> allTicker = corpService.findAllTicker();

        return ResponseEntity.status(HttpStatus.OK).body(allTicker);
    }
}