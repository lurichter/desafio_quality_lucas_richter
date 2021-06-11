package meli.desafio_quality.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InputValidationException extends Exception {

    public InputValidationException(String message) {
        super(message);
    }
}
