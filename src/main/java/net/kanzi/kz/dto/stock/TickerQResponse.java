package net.kanzi.kz.dto.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import net.kanzi.kz.domain.stock.Ticker;
import net.kanzi.kz.domain.stock.TickerQ;

import java.time.LocalDateTime;

@Getter
public class TickerQResponse {

    private Long id;
    private String stockCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime baseDate;
    private Long value;
    private Long close;
    private String fsType;
    private String stockName;
    private String account;
    private String gongsi;
    private String fsName;
    private String indicator;
    private Long ttm;
    private Long shares;
    private Long perShare;

    @Builder
    public TickerQResponse(Long id, String stockCode,String indicator, LocalDateTime baseDate, Long value, Long close, String fsType, String stockName, String account, String gongsi, String fsName, Long ttm, Long shares, Long perShare) {
        this.id = id;
        this.stockCode = stockCode;
        this.baseDate = baseDate;
        this.value = value;
        this.indicator = indicator;
        this.close = close;
        this.fsType = fsType;
        this.stockName = stockName;
        this.account = account;
        this.gongsi = gongsi;
        this.fsName = fsName;
        this.ttm = ttm;
        this.shares = shares;
        this.perShare = perShare;
    }

    public static TickerQResponse of(TickerQ tickerQ){
        return TickerQResponse.builder()
                .id(tickerQ.getId())
                .stockCode(tickerQ.getStockCode())
                .baseDate(tickerQ.getBaseDate())
                .value(tickerQ.getValue())
                .indicator(tickerQ.getIndicator())
                .close(tickerQ.getClose())
                .fsType(tickerQ.getFsType())
                .stockName(tickerQ.getStockName())
                .account(tickerQ.getAccount())
                .gongsi(tickerQ.getGongsi())
                .fsName(tickerQ.getFsName())
                .ttm(tickerQ.getTtm())
                .shares(tickerQ.getShares())
                .perShare(tickerQ.getPerShare())
                .build();
    }

}
