package com.javaExpanding.Two.Error.MyError1;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.javaExpanding.Two.Facility.Controller")
public class MyError1Handler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MyError1> handleValidation(MethodArgumentNotValidException ex) {
        // 첫 번째 에러 메시지만 가져오기
        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("잘못된 입력입니다.");

        return ResponseEntity.badRequest().body(new MyError1(400, msg));
    }
}
