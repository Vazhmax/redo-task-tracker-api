package ru.vazhmax.task.tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
