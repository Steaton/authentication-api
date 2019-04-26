package nl.ing.authentication;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value="Login API", description="Allows a user to be authenticated")
@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully authenticated user"),
            @ApiResponse(code = 401, message = "User credentials incorrect or account locked, login failed"),
            @ApiResponse(code = 404, message = "Username specified is not found"),
            @ApiResponse(code = 400, message = "Request message is not valid"),
            @ApiResponse(code = 500, message = "Something technical went wrong")
    })
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        LOGGER.info("Login request made by: {}", loginRequest.getUsername());
        String token = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
        LOGGER.info("Login successful for: {}", loginRequest.getUsername());
        return new LoginResponse(token);
    }
}
