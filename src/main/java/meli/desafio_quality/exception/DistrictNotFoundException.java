package meli.desafio_quality.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DistrictNotFoundException extends Exception {

    public DistrictNotFoundException(String message) {
        super(message);
    }
}
