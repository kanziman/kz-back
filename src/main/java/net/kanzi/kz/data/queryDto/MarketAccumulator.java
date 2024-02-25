package net.kanzi.kz.data.queryDto;

import lombok.Data;

@Data
public class MarketAccumulator {
    private long kospiUp;
    private long kospiDown;
    private long kosdaqUp;
    private long kosdaqDown;

    // 누적 메소드
    public MarketAccumulator accumulate(DailyMarketSummary summary) {
        kospiUp += summary.getKospiUp();
        kospiDown += summary.getKospiDown();
        kosdaqUp += summary.getKosdaqUp();
        kosdaqDown += summary.getKosdaqDown();
        return this;
    }

    // 병렬 스트림을 위한 결합 메소드
    public MarketAccumulator combine(MarketAccumulator other) {
        kospiUp += other.kospiUp;
        kospiDown += other.kospiDown;
        kosdaqUp += other.kosdaqUp;
        kosdaqDown += other.kosdaqDown;
        return this;
    }
}
