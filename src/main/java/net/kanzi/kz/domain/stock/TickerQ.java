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
@Table(name = "ticker_q")
public class TickerQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


}