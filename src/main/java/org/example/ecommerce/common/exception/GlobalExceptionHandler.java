package org.example.ecommerce.common.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PromotionException.class)
    public ResponseEntity<?> handlePromotionException(PromotionException e) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(errorBody);
    }








    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<?> handleException(CategoryException e) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(errorBody);
    }

}
