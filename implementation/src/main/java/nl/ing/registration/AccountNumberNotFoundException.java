package nl.ing.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =  HttpStatus.NOT_FOUND)
public class AccountNumberNotFoundException extends RuntimeException {

    public AccountNumberNotFoundException(String message) {
        super(message);
    }
}