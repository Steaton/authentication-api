package nl.ing.authentication;

import nl.ing.exception.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AccountIsLockedException extends RestException {

    public AccountIsLockedException(String message) {
        super(message);
    }
}