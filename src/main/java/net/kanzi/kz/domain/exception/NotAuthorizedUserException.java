package net.kanzi.kz.domain.exception;

public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
