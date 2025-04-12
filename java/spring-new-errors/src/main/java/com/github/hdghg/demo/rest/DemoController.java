package com.github.hdghg.demo.rest;

import com.github.hdghg.demo.dto.ErrorDto;
import com.github.hdghg.demo.service.DemoService;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.annotation.meta.When;

@RestController
@Validated
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("hello")
    public ResponseEntity<String> hello(@RequestParam("name") @Size(min = 1) String name) {
        DemoService.Result result = demoService.prepareGreeting(name);
        // --enable-preview allows to use pattern matching in Java 17
        return switch (result) {
            case DemoService.Success success -> ResponseEntity.ok(success.data());
            case DemoService.UserIsNotWelcome ignored -> errorDto(null, "Come back later", null);
        };
    }

    @SuppressWarnings("unchecked")
    private <T> T errorDto(
            @Nonnull(when = When.MAYBE) ErrorDto.Type type,
            @Nonnull(when = When.MAYBE) String detail,
            @Nonnull(when = When.MAYBE) HttpStatus status
    ) {
        type = type != null ? type : ErrorDto.Type.BUSINESS;
        status = status != null ? status : HttpStatus.BAD_REQUEST;
        detail = detail != null ? detail : status.getReasonPhrase();

        var dto = new ErrorDto(type, detail);
        return (T) ResponseEntity.status(status).body(dto);
    }
}
