package com.tma.restaurant_exercise_phase_2.exceptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.security.InvalidParameterException;

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

    @ExceptionHandler(value = BadCredentialsException.class)
    public ExceptionResponse handleBadCredentialsException(BadCredentialsException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = JwtException.class)
    public ExceptionResponse handleJwtException(JwtException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = InternalAuthenticationServiceException.class)
    public ExceptionResponse handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ExceptionResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ExceptionResponse handleNoResourceFoundException(NoResourceFoundException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ExceptionResponse(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }
}
