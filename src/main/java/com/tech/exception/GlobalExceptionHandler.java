package com.tech.exception;

import com.tech.configuration.ApiMessages;
import com.tech.exception.custom.*;
import com.tech.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponse apiResponse;
    private final ApiMessages apiMessages;

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        if (Objects.requireNonNull(ex.getRequiredType()).isEnum()) {
            var enumClass = (Class<? extends Enum<?>>) ex.getRequiredType();
            String validEnumValues = Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            apiResponse.setMessage(String.format(apiMessages.getMessage("error.type.mismatch"), ex.getValue(), validEnumValues));
        } else {
            apiResponse.setMessage(String.format(apiMessages.getMessage("error.type.mismatch"), ex.getValue(), ex.getRequiredType().isEnum()));
        }
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsuitableRequestException.class)
    public ResponseEntity<?> handleUnsuitableRequestException(UnsuitableRequestException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingArgumentException.class)
    public ResponseEntity<?> handleMissingArgumentException(MissingArgumentException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<?> handleEmailSendingException(EmailSendingException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SchedulerException.class)
    public ResponseEntity<?> handleSchedulerException(SchedulerException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataExportException.class)
    public ResponseEntity<?> handleDataExportException(DataExportException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DirectoryCreationException.class)
    public ResponseEntity<?> handleDirectoryCreationException(DirectoryCreationException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<?> handleForbiddenAccessException(ForbiddenAccessException ex) {
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setSuccess(false);
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    errorResponse.put(fieldName, message);
                });

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
