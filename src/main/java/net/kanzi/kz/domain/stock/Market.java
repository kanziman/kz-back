package net.kanzi.kz.domain.stock;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "market")
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date baseDate;
    private String mktType;
    private Float open;
    private Float high;
    private Float low;
    private Float close;
    private Float volume;
    private Float amount;
    private Float value;
    private Float fValue;
    private Float per;
    private Float pbr;
    private Float dy;
    private Float credit;
    private Float deposit;
    private Float adr;
}
