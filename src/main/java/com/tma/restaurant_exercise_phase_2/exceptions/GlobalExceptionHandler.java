package com.tma.restaurant_exercise_phase_2.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
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
}
