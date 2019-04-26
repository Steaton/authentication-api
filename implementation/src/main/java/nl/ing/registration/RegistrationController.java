package nl.ing.registration;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value="Registration API", description="Allows a user to be registered in the system if they have a existing account")
@RestController
public class RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private AccountRegistrationService accountRegistrationService;

    @PostMapping(value = "/registerAccount", produces = MediaType.APPLICATION_JSON_VALUE)
    public void registerAccount(@RequestBody @Valid AccountRegistrationRequest registration) {
        LOGGER.info("Attempting to register an account for: {}", registration.getUsername());
        accountRegistrationService.registerAccount(registration.getAccountNumber(), registration.getUsername(), registration.getPassword());
        LOGGER.info("Registration successful for: {}", registration.getUsername());
    }

    /*
        private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        LOGGER.info("Login request made by " + loginRequest.getUsername());
        String token = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
        LOGGER.info("Login successful for " + loginRequest.getUsername());
        return new LoginResponse(token);
    }
     */
}
