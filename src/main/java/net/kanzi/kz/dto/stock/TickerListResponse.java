package net.kanzi.kz.dto.stock;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class TickerListResponse {
    private String stockName;
    private String mktType;
    private String stockCode;
    @Builder
    public TickerListResponse(String stockName, String mktType, String stockCode) {
        this.stockName = stockName;
        this.mktType = mktType;
        this.stockCode = stockCode;
    }


}
