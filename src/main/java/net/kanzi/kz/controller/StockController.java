package net.kanzi.kz.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.kanzi.kz.api.ApiResponse;
import net.kanzi.kz.domain.stock.Market;
import net.kanzi.kz.service.CorpService;
import net.kanzi.kz.service.MarketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Log4j2
@CrossOrigin("*")
public class StockController {

    private final CorpService corpService;
    private final MarketService marketService;


    @PostMapping("/api/stock/proxy")
    public ResponseEntity proxyList(@RequestBody Map map) {
        List<String> lines = Arrays.asList(map.get("proxy").toString().split("\n"));

        Map rtn = corpService.insertProxy(lines);
        return ResponseEntity.status(HttpStatus.OK).body(rtn);
    }

    @PostMapping("/api/stock/ticker")
    public ApiResponse<Map> getTicks(@RequestBody Map option) {
        Map map = corpService.findByCodeQ(option);

        return ApiResponse.of(HttpStatus.OK, map);
    }
    @GetMapping("/api/stock/market")
    public ApiResponse<List<Market>> getMarket() {
        List<Market> markets = marketService.findAll();
        return ApiResponse.of(HttpStatus.OK, markets);
    }

    @GetMapping("/api/stock/tickers")
    public ApiResponse<List<Map<String, Object>>> getTickers() {
        List<Map<String, Object>> allTicker = corpService.findAllTicker();

        return ApiResponse.of(HttpStatus.OK, allTicker);
    }
}