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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;
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
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(anAccount(hash("password"))));
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
        expectedEx.expectMessage("Login failed - account does not exist for: user");

        // Given
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        loginService.login("user", "password");
    }

    @Test
    public void should_throw_exception_if_password_incorrect() {
        // Expect
        expectedEx.expect(PasswordIncorrectException.class);
        expectedEx.expectMessage("Login failed - password is incorrect for: user");

        // Given
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(anAccount(hash("incorrectPassword"))));

        // When
        loginService.login("user", "password");
    }

    @Test
    public void should_throw_exception_if_account_locked() {
        // Expect
        expectedEx.expect(AccountIsLockedException.class);
        expectedEx.expectMessage("Login failed - account is locked for: user");

        // Given
        Account account = anAccount(hash("incorrectPassword"));
        account.setLockedUntil(tomorrow());
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));

        // When
        loginService.login("user", "password");
    }

    @Test
    public void should_reset_failed_logon_attempts_on_successful_login() {
        // Given
        Account account = anAccount(hash("password"));
        account.setFailedLoginAttempts(3);
        account.setLockedUntil(anHourAgo());
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(loginTokenService.createLoginToken("user", 3600000)).thenReturn("TOKEN");

        // When
        String token = loginService.login("user", "password");

        // Then
        assertEquals("TOKEN", token);
        assertEquals(0, account.getFailedLoginAttempts());
        assertNull(account.getLockedUntil());
    }

    @Test
    public void should_increment_failed_login_attempts() {
        // Given
        Account account = anAccount(hash("incorrectPassword"));
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));

        // When
        try {
            loginService.login("user", "password");
        } catch (PasswordIncorrectException e) {
            // Continue
        }

        // Then
        assertEquals(1, account.getFailedLoginAttempts());
        assertFalse(account.isLocked());
    }

    @Test
    public void should_lock_account_on_3rd_failed_login_attempt() {
        // Given
        Account account = anAccount(hash("incorrectPassword"));
        account.setFailedLoginAttempts(2);
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));

        // When
        try {
            loginService.login("user", "password");
        } catch (PasswordIncorrectException e) {
            // Continue
        }

        // Then
        assertEquals(3, account.getFailedLoginAttempts());
        assertTrue(account.isLocked());
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

    private Account anAccount(String hashedPassword) {
        return new Account("accountNumber", "user", hashedPassword);
    }

    private String hash(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    private LocalDateTime anHourAgo() {
        return LocalDateTime.now().minusHours(1);
    }

    private LocalDateTime tomorrow() {
        return LocalDateTime.now().plusDays(1);
    }
}