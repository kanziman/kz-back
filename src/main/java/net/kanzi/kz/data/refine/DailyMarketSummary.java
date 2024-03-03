package net.kanzi.kz.data.refine;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@ToString
@Data
@NoArgsConstructor
public class DailyMarketSummary {
    private Date date;
    private long kospiUp;
    private long kospiDown;
    private long kosdaqUp;
    private long kosdaqDown;
    private float adrKospi;
    private float adrKosdaq;

    public DailyMarketSummary(Date date, long kospiUp, long kospiDown, long kosdaqUp, long kosdaqDown) {
        this.date = date;
        this.kospiUp = kospiUp;
        this.kospiDown = kospiDown;
        this.kosdaqUp = kosdaqUp;
        this.kosdaqDown = kosdaqDown;
    }
}