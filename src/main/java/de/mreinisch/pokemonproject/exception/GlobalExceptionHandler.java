package de.mreinisch.pokemonproject.exception;

import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode status,
                                                                            WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<FieldError> allErrors = ex.getBindingResult().getFieldErrors();

        allErrors.forEach(error -> {
            String fieldName = error.getField();
            String errorMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

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
