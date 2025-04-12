package com.github.hdghg.demo.rest;

import com.github.hdghg.demo.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;
import javax.annotation.meta.When;

@ControllerAdvice
@Order(1)
public class Rfc9457ExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(Rfc9457ExceptionHandler.class);

    @Override
    @Nonnull(when = When.MAYBE)
    protected ResponseEntity<Object> handleExceptionInternal(
            @Nonnull Exception ex,
            @Nonnull(when = When.MAYBE) Object body,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatusCode statusCode,
            @Nonnull WebRequest request
    ) {
        log.warn("Handling exception", ex);
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> createResponseEntity(
            @Nonnull(when = When.MAYBE) Object body,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatusCode statusCode,
            @Nonnull WebRequest request
    ) {
        if (body instanceof ProblemDetail problemDetail) {
            return ResponseEntity.status(problemDetail.getStatus())
                    .body(ErrorDto.fromProblemDetail(problemDetail));
        } else {
            return super.createResponseEntity(body, headers, statusCode, request);
        }
    }
}
