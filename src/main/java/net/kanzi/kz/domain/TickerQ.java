package net.kanzi.kz.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "ticker_q")
public class TickerQ {
    @Id
    private Long id;
    private String stockCode;
    private Date baseDate;
    private String indicator;
    private Double value;
    private Double close;

    //quarter
    private String fsType;
    private String stockName;
    private String account;
    private String gongsi;
    private String fsName;
    private Long ttm;
    private Long shares;
    private Long perShare;

    @Override
    public String toString() {
        return "Ticker{" +
                "stockCode='" + stockCode + '\'' +
                ", baseDate=" + baseDate +
                ", indicator='" + indicator + '\'' +
                ", value=" + value +
                '}';
    }

}