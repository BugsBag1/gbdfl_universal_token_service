package kz.oib.gbdfl_universal_token_service.exception;


import kz.oib.gbdfl_universal_token_service.model.dto.ErrorResponse;
import kz.oib.gbdfl_universal_token_service.model.dto.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BuisnessException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleBuisnessException(BuisnessException ex) {
        return buildErrorResponse("BIZ-001", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleExternalServiceException(ExternalServiceException ex) {
        return buildErrorResponse("EXT-500", ex.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<Object>> handleGenericException(Exception ex) {
        return buildErrorResponse("GEN-500", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResponseWrapper<Object>> buildErrorResponse(String code, String message, HttpStatus status) {
        ResponseWrapper<Object> response = ResponseWrapper.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .error(new ErrorResponse(code, message))
                .build();
        return new ResponseEntity<>(response, status);
    }
}
