package com.phucitdev.pickleball_backend.commo.exception;
import com.phucitdev.pickleball_backend.commo.exception.branch.BranchNotFoundException;
import com.phucitdev.pickleball_backend.commo.exception.court.BadRequestException;
import com.phucitdev.pickleball_backend.commo.exception.court.DuplicateResourceException;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.commo.exception.timeslot.InvalidTimeSlotException;
import com.phucitdev.pickleball_backend.commo.exception.zone.ZoneNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.phucitdev.pickleball_backend.commo.response.ApiResponse;
import com.phucitdev.pickleball_backend.commo.exception.auth.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // AUTH
    @ExceptionHandler(UnauthenticatedException.class)
    public ApiResponse<?> handleUnauthenticated(UnauthenticatedException ex) {
        return new ApiResponse<>(401, ex.getMessage(), null);
    }
    // DUPLICATION (gom hết)
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicate(DuplicateResourceException ex) {
        return ResponseEntity.status(409)
                .body(new ApiResponse<>(409, ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<?> response = new ApiResponse<>(400, "Validation failed", errors);

        return ResponseEntity.status(400).body(response);
    }
    // Duplication
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(409, ex.getMessage(), null));
    }
    // Username or password wrong
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(401)
                .body(new ApiResponse<>(401, ex.getMessage(), null));
    }
    // Duplication
    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handlePhoneExists(PhoneAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) //  409
                .body(new ApiResponse<>(409, ex.getMessage(), null));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(404, ex.getMessage(), null));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(401, ex.getMessage(), null));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponse<?>> handleExpired(TokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(401, ex.getMessage(), null));
    }
    @ExceptionHandler(InvalidTokenTypeException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidTokenType(InvalidTokenTypeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(401, ex.getMessage(), null));
    }
    @ExceptionHandler(InvalidDeviceIdException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidRequest(InvalidDeviceIdException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(400, ex.getMessage(), null));
    }
    //  VALIDATION / BUSINESS
    @ExceptionHandler(InvalidTimeSlotException.class)
    public ResponseEntity<?> handleInvalidTimeSlot(InvalidTimeSlotException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "code", 400,
                        "message", ex.getMessage()
                ));
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(400, ex.getMessage(), null));
    }
    // FALLBACK
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception ex) {
        return new ApiResponse<>(500, "Internal Server Error", null);
    }
}