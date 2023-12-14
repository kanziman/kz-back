package net.kanzi.kz.advice;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SuccessResponseDto {

    private final boolean success = true;
    private final int status;
    private final Object data;
    private final LocalDateTime timeStamp;

    public SuccessResponseDto(int status, Object data) {
        this.status = status;
        this.data = data;
        this.timeStamp = LocalDateTime.now();
    }
}
