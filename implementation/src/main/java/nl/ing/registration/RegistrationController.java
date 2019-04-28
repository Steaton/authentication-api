package nl.ing.registration;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered user"),
            @ApiResponse(code = 404, message = "Account number not found in Accounts API"),
            @ApiResponse(code = 409, message = "Username already in use"),
            @ApiResponse(code = 400, message = "Request message is not valid"),
            @ApiResponse(code = 500, message = "Something technical went wrong")
    })
    @PostMapping(value = "/registerAccount", produces = MediaType.APPLICATION_JSON_VALUE)
    public void registerAccount(@RequestBody @Valid AccountRegistrationRequest registration) {
        LOGGER.info("Attempting to register an account for: {}", registration.getUsername());
        accountRegistrationService.registerAccount(registration.getAccountNumber(), registration.getUsername(), registration.getPassword());
        LOGGER.info("Registration successful for: {}", registration.getUsername());
    }
}
