package nl.ing.authentication;

import nl.ing.exception.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =  HttpStatus.NOT_FOUND)
public class AccountDoesNotExistException extends RestException {

    public AccountDoesNotExistException(String message) {
        super(message);
    }
}
