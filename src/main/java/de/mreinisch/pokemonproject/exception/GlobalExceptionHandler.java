package de.mreinisch.pokemonproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IdNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleIdNotFoundException(IdNotFound ex){
        return "An error occurred during the operation to search by ID: " + ex.getLocalizedMessage();
    }
}
