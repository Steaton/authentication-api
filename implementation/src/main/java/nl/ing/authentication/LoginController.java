package nl.ing.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        logger.info("Login request made by " + loginRequest.getUsername());
        String token = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
        logger.info("Login successful for " + loginRequest.getUsername());
        return new LoginResponse(token);
    }
}
