package net.kanzi.kz.dto.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import net.kanzi.kz.domain.stock.Market;
import net.kanzi.kz.domain.stock.Ticker;

import java.time.LocalDateTime;

@Getter
public class TickerResponse {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime baseDate;
    private String stockCode;
    private String fsType;
    private Float per;
    private Float pbr;
    private Float psr;
    private Float por;
    private Float dy;
    private Long value;
    private Long shares;
    private Long volume;
    private Long close;

    @Builder
    public TickerResponse(Long id, LocalDateTime baseDate, String stockCode, String fsType, Float per, Float pbr, Float por,Float psr, Float dy, Long value, Long shares, Long volume, Long close) {
        this.id = id;
        this.baseDate = baseDate;
        this.stockCode = stockCode;
        this.fsType = fsType;
        this.per = per;
        this.pbr = pbr;
        this.psr = psr;
        this.por = por;
        this.dy = dy;
        this.value = value;
        this.shares = shares;
        this.volume = volume;
        this.close = close;
    }

    public static TickerResponse of(Ticker ticker){
        return TickerResponse.builder()
                .id(ticker.getId())
                .baseDate(ticker.getBaseDate())
                .stockCode(ticker.getStockCode())
                .fsType(ticker.getFsType())
                .per(ticker.getPer())
                .pbr(ticker.getPbr())
                .psr(ticker.getPsr())
                .por(ticker.getPor())
                .dy(ticker.getDy())
                .value(ticker.getValue())
                .shares(ticker.getShares())
                .volume(ticker.getVolume())
                .close(ticker.getClose())
                .build();
    }

}
