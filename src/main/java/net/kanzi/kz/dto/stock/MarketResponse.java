package net.kanzi.kz.dto.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import net.kanzi.kz.domain.stock.KorMarket;
import net.kanzi.kz.domain.stock.Market;

import java.time.LocalDateTime;

@Getter
public class MarketResponse {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime baseDate;
    private String mktType;
    private Float open;
    private Float high;
    private Float low;
    private Float close;
    private Long volume;
    private Long amount;
    private Long value;
    private Float per;
    private Float pbr;
    private Float dy;
    private Long credit;
    private Long deposit;
    private Float adr;


    @Builder
    public MarketResponse(Long id, LocalDateTime baseDate, String mktType, Float open, Float high, Float low, Float close, Long volume, Long amount, Long value, Float per, Float pbr, Float dy, Long credit, Long deposit, Float adr) {
        this.id = id;
        this.baseDate = baseDate;
        this.mktType = mktType;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.amount = amount;
        this.value = value;
        this.per = per;
        this.pbr = pbr;
        this.dy = dy;
        this.credit = credit;
        this.deposit = deposit;
        this.adr = adr;
    }

    public static MarketResponse of(KorMarket market){
        return MarketResponse.builder()
                .id(market.getId())
                .baseDate(market.getBaseDate())
                .mktType(market.getMktType())
                .open(market.getOpen())
                .high(market.getHigh())
                .low(market.getLow())
                .close(market.getClose())
                .volume(market.getVolume())
                .amount(market.getAmount())
                .value(market.getVal())
                .per(market.getPer() )
                .pbr(market.getPbr())
                .dy(market.getDy())
                .credit(market.getCredit())
                .deposit(market.getDeposit())
                .adr(market.getAdr())
                .build();
    }

}
