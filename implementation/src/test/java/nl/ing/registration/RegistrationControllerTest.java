package nl.ing.registration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .build();
    }

    @Test
    public void should_register_account() throws Exception {
        doNothing().when(registrationService).registerAccount(anyString(), anyString(), anyString());
        String response = this.mockMvc.perform(post("/registerAccount")
                .content(aRegistrationRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals("", response);
    }

    @Test
    public void should_error_if_account_number_not_found() throws Exception {
        doThrow(new AccountNumberNotFoundException("Not found")).when(registrationService).registerAccount(anyString(), anyString(), anyString());
        String errorMessage = this.mockMvc.perform(post("/registerAccount")
                .content(aRegistrationRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException().getMessage();
        assertEquals("Not found", errorMessage);
    }

    @Test
    public void should_error_if_username_already_in_use() throws Exception {
        doThrow(new AccountAlreadyExistsException("Already exists")).when(registrationService).registerAccount(anyString(), anyString(), anyString());
        String errorMessage = this.mockMvc.perform(post("/registerAccount")
                .content(aRegistrationRequest())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn().getResolvedException().getMessage();
        assertEquals("Already exists", errorMessage);
    }

    @Test
    public void should_ensure_min_password_length_validated() throws Exception {
        this.mockMvc.perform(post("/registerAccount")
                .content(new RegistrationRequest("1234", "user", "pw").toJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException().getMessage();
    }

    @Test
    public void should_ensure_username_not_empty() throws Exception {
        this.mockMvc.perform(post("/registerAccount")
                .content(new RegistrationRequest("1234", "", "password").toJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException().getMessage();
    }

    @Test
    public void should_ensure_account_number_not_empty() throws Exception {
        this.mockMvc.perform(post("/registerAccount")
                .content(new RegistrationRequest("", "user", "password").toJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException().getMessage();
    }

    private String aRegistrationRequest() {
        return new RegistrationRequest("1234", "user", "password").toJson();
    }


}