package com.example.shopspherebackend.exception;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.shopspherebackend.entity.Role;
import tools.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        MethodArgumentNotValidException ex) {

                List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getField() + " " + error.getDefaultMessage())
                                .toList();

                return ResponseEntity
                                .badRequest()
                                .body(new ErrorResponse(errors));
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleInvalidEnum(
                        HttpMessageNotReadableException ex) {

                Throwable cause = ex.getMostSpecificCause();

                if (cause instanceof InvalidFormatException) {
                        return ResponseEntity.badRequest()
                                        .body(new ErrorResponse(
                                                        List.of("Role must be either USER or ADMIN")));
                }

                return ResponseEntity.badRequest()
                                .body(new ErrorResponse(
                                                List.of("Invalid request body")));
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
                        DataIntegrityViolationException ex) {

                String message = "Data already exists";

                Throwable cause = ex.getMostSpecificCause();
                if (cause != null) {
                        String error = cause.getMessage();

                        if (error.contains("(email)")) {
                                message = "Email already exists";
                        } else if (error.contains("(phone)")) {
                                message = "Phone number already exists";
                        }
                }

                return ResponseEntity.badRequest()
                                .body(new ErrorResponse(List.of(message)));
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponse> handleRuntimeException(
                        RuntimeException ex) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse(List.of(ex.getMessage())));
        }
}