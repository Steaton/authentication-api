package nl.ing.registration;

import nl.ing.exception.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =  HttpStatus.NOT_FOUND)
public class AccountNumberNotFoundException extends RestException {

    public AccountNumberNotFoundException(String message) {
        super(message);
    }
}