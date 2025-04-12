package com.github.hdghg.demo.dto;

import org.springframework.http.ProblemDetail;

import javax.annotation.Nonnull;
import java.util.Objects;

public record ErrorDto(@Nonnull Type type, @Nonnull String detail) {

    public enum Type {
        BUSINESS, SYSTEM
    }

    public static ErrorDto fromProblemDetail(@Nonnull ProblemDetail problemDetail) {
        var detail = Objects.requireNonNullElse(problemDetail.getDetail(), "Error");
        return new ErrorDto(Type.BUSINESS, detail);
    }
}
