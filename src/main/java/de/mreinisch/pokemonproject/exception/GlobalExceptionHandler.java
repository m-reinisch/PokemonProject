package de.mreinisch.pokemonproject.exception;

import jakarta.validation.ConstraintViolationException;
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

    @ExceptionHandler(NameNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNameNotFoundException(NameNotFound ex){
        return "An error occurred during the search in PokémonAPI: " + ex.getLocalizedMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException ex) {
        return ex.getMessage().substring(19);
    }
}
