package nl.ing.authentication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(loginController)
                .build();
    }

    @Test
    public void should_login() throws Exception {
        this.mockMvc.perform(post("/login")
                .content(aLoginRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_fail_to_login_if_account_does_not_exist() throws Exception {
        mockAccountDoesNotExist();
        String errorMessage = this.mockMvc.perform(post("/login")
                .content(aLoginRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException().getMessage();
        assertEquals("Login failed - account does not exist for user", errorMessage);
    }

    @Test
    public void should_fail_to_login_if_password_is_incorrect() throws Exception {
        mockPasswordIncorrect();
        String errorMessage = this.mockMvc.perform(post("/login")
                .content(aLoginRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn().getResolvedException().getMessage();
        assertEquals("Login failed - password is incorrect for user", errorMessage);
    }

    @Test
    public void should_ensure_username_not_empty() throws Exception {
        this.mockMvc.perform(post("/login")
                .content(emptyUsernameLoginRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_ensure_password_at_least_6_characters() throws Exception {
        this.mockMvc.perform(post("/login")
                .content(shortPasswordLoginRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String aLoginRequest() {
        return new LoginRequest("user", "password").toJson();
    }

    private String emptyUsernameLoginRequest() {
        return new LoginRequest("", "password").toJson();
    }

    private String shortPasswordLoginRequest() {
        return new LoginRequest("user", "pass").toJson();
    }

    private void mockAccountDoesNotExist() {
        AccountDoesNotExistException accountDoesNotExistException = new AccountDoesNotExistException("Login failed - account does not exist for user");
        doThrow(accountDoesNotExistException).when(loginService).login(anyString(), anyString());
    }

    private void mockPasswordIncorrect() {
        PasswordIncorrectException passwordIncorrectException = new PasswordIncorrectException("Login failed - password is incorrect for user");
        doThrow(passwordIncorrectException).when(loginService).login(anyString(), anyString());
    }
}