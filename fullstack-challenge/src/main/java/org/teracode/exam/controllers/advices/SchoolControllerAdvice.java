package org.teracode.exam.controllers.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.teracode.exam.exceptions.BadRequestException;
import org.teracode.exam.responses.dtos.ErrorDTO;

@RestControllerAdvice
public class SchoolControllerAdvice {
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity handleRuntimeException() {
        return new ResponseEntity<>(new ErrorDTO("Invalid last name initial"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity handleMethodArgumentTypeMismatchException() {
        return new ResponseEntity<>(new ErrorDTO("Invalid request parameter"), HttpStatus.BAD_REQUEST);
    }
}
