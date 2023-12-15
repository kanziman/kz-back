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
@Table(name = "ticker")
public class Ticker {
    @Id
    private Long id;
    private String stockCode;
    private Date baseDate;
    private String fsType;

    private String pbr;
    private String per;
    private String por;
    private String psr;
    private String dy;
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