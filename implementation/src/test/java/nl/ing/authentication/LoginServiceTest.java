package nl.ing.authentication;

import nl.ing.account.Account;
import nl.ing.account.AccountRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Mock
    private LoginTokenService loginTokenService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    public void should_login_and_obtain_token() {
        // Given
        String hashedPassword = new BCryptPasswordEncoder().encode("password");
        when(accountRepository.findById(anyString())).thenReturn(
                Optional.of(new Account("accountNumber", "user", hashedPassword)));
        when(loginTokenService.createLoginToken("user", 3600000)).thenReturn("TOKEN");

        // When
        String token = loginService.login("user", "password");

        // Then
        assertEquals("TOKEN", token);
    }

    @Test
    public void should_throw_exception_if_user_doesnt_exist() {
        // Expect
        expectedEx.expect(AccountDoesNotExistException.class);
        expectedEx.expectMessage("Login failed - account does not exist for user");

        // Given
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        loginService.login("user", "password");
    }

    @Test
    public void should_throw_exception_if_password_incorrect() {
        // Expect
        expectedEx.expect(PasswordIncorrectException.class);
        expectedEx.expectMessage("Login failed - password is incorrect for user");

        // Given
        String hashedPassword = new BCryptPasswordEncoder().encode("doesntmatch");
        when(accountRepository.findById(anyString())).thenReturn(
                Optional.of(new Account("accountNumber", "user", hashedPassword)));

        // When
        loginService.login("user", "password");
    }

    @Test
    public void should_handle_null_username_correctly() {
        // Expect
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Username cannot be empty");

        // Given
        loginService.login(null, "password");
    }

    @Test
    public void should_handle_empty_password_correctly() {
        // Expect
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Password cannot be empty");

        // Given
        loginService.login("user", "");
    }
}