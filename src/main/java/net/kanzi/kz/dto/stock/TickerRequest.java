package net.kanzi.kz.dto.stock;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TickerRequest {
    private String code;
    private String option;
    @Builder
    public TickerRequest(String code, String option) {
        this.code = code;
        this.option = option;
    }
}
