package nl.ing.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login")
    public void login(@RequestBody @Valid LoginRequest loginRequest) {
        logger.info("Login request made by " + loginRequest.getUsername());
        loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
        logger.info("Login successful for " + loginRequest.getUsername());
    }
}
