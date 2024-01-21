package net.kanzi.kz.apiController;

import net.kanzi.kz.domain.exception.EntityNotFoundException;
import net.kanzi.kz.domain.exception.NotAuthorizedUserException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Object> methodArgumentNotValidException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        return ApiResponse.of(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                null
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotAuthorizedUserException.class)
    public ApiResponse<Object> handleEntityNotFoundException(NotAuthorizedUserException e) {
        return ApiResponse.of(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                null
        );
    }

}
