package net.kanzi.kz.data.refine;

import lombok.Data;
import lombok.ToString;

import java.sql.Date;

@ToString
@Data
public class KorPrice {
    private String stockCode;
    private Date baseDate;
    private String segment;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;


}
