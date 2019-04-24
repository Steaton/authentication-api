package nl.ing.authentication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
                .setHandlerExceptionResolvers()
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
        this.mockMvc.perform(post("/login")
                .content(aLoginRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_fail_to_login_if_password_is_incorrect() throws Exception {
        mockPasswordIncorrect();
        this.mockMvc.perform(post("/login")
                .content(aLoginRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private String aLoginRequest() {
        return new LoginRequest("user", "password").toJson();
    }

    private void mockAccountDoesNotExist() {
        AccountDoesNotExistException accountDoesNotExistException = new AccountDoesNotExistException("Account does not exist. ");
        doThrow(accountDoesNotExistException).when(loginService).login(anyString(), anyString());
    }

    private void mockPasswordIncorrect() {
        PasswordIncorrectException passwordIncorrectException = new PasswordIncorrectException("Account does not exist. ");
        doThrow(passwordIncorrectException).when(loginService).login(anyString(), anyString());
    }
}