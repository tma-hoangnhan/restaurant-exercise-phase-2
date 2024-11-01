package com.tma.restaurant_exercise_phase_2.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ExceptionResponse handleException(Exception ex) {
        ex.printStackTrace();
        return new ExceptionResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NoItemFoundException.class)
    public ExceptionResponse handleNoItemFoundException(NoItemFoundException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ItemNameAlreadyExistedException.class)
    public ExceptionResponse handleItemNameAlreadyExistedException(ItemNameAlreadyExistedException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidItemTypeException.class)
    public ExceptionResponse handleInvalidItemTypeException(InvalidItemTypeException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidParameterException.class)
    public ExceptionResponse handleInvalidParameterException(InvalidParameterException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CannotAddItemToBillException.class)
    public ExceptionResponse handleCannotAddItemToBillException(CannotAddItemToBillException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ItemAlreadyDeletedException.class)
    public ExceptionResponse handleItemAlreadyDeletedException(ItemAlreadyDeletedException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= UserNotFoundException.class)
    public ExceptionResponse handleUserNotFoundException(UserNotFoundException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserAlreadyExistedException.class)
    public ExceptionResponse handleUserReadyExistedException(UserAlreadyExistedException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = SecurityException.class)
    public ExceptionResponse handleSecurityException(SecurityException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ExceptionResponse handleExpiredJwtException(ExpiredJwtException ex) {
        return new ExceptionResponse("TOKEN HAS EXPIRED", HttpStatus.UNAUTHORIZED);
    }
}
