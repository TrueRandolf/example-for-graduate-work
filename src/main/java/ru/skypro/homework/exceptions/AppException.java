package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AppException extends RuntimeException {
    private final HttpStatus httpStatus;

    protected AppException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
