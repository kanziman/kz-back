package net.kanzi.kz.domain.stock;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "ticker")
public class Ticker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stockCode;
    private Date baseDate;
    private String fsType;

    private double pbr;
    private double per;
    private double por;
    private double psr;
    private double dy;
    private float value;
    private long shares;
    private long volume;
    private float close;

    //quarter
    @Override
    public String toString() {
        return "Ticker{" +
                "stockCode='" + stockCode + '\'' +
                ", baseDate=" + baseDate +
                '}';
    }

}