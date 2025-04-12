package com.github.hdghg.demo.rest;

import com.github.hdghg.demo.dto.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
@Order(2)
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto constraintViolationException(ConstraintViolationException e) {
        return new ErrorDto(ErrorDto.Type.BUSINESS, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto exception(Exception e) {
        return new ErrorDto(ErrorDto.Type.SYSTEM, e.getMessage());
    }

}
