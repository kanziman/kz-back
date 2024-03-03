package net.kanzi.kz.data.refine;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class MarketRefine {
    private Date baseDate;
    private String mktType;
    private Float open;
    private Float high;
    private Float low;
    private Float close;
    private Long volume;
    private Long amount;
    private Long val;
    private Long credit;
    private Long deposit;
    private Float per;
    private Float pbr;
    private Float dy;
    private Float adr;
}
