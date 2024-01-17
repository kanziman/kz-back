package net.kanzi.kz.dto.stock;

import lombok.Builder;
import lombok.Getter;
import net.kanzi.kz.domain.stock.Market;

import java.time.LocalDateTime;

@Getter
public class MarketResponse {

    private Long id;
    private LocalDateTime baseDate;
    private String mktType;
    private Float open;
    private Float high;
    private Float low;
    private Float close;
    private Double volume;
    private Double amount;
    private Double value;
    private Float per;
    private Float pbr;
    private Float dy;
    private Double credit;
    private Double deposit;
    private Float adr;


    @Builder
    public MarketResponse(Long id, LocalDateTime baseDate, String mktType, Float open, Float high, Float low, Float close, Double volume, Double amount, Double value, Float per, Float pbr, Float dy, Double credit, Double deposit, Float adr) {
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

    public static MarketResponse of(Market market){
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
                .value(market.getValue())
                .per(market.getPer() )
                .pbr(market.getPbr())
                .dy(market.getDy())
                .credit(market.getCredit())
                .deposit(market.getDeposit())
                .adr(market.getAdr())
                .build();
    }

}
