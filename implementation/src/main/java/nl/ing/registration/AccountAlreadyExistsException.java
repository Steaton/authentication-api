package nl.ing.registration;


import nl.ing.exception.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =  HttpStatus.CONFLICT)
public class AccountAlreadyExistsException extends RestException {

    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
