package nl.ing.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(RestException.class);

    public RestException(String message) {
        super(message);
        logger.error(message, this);
    }
}
