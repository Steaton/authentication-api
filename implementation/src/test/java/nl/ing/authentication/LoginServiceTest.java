package nl.ing.authentication;

import nl.ing.account.Account;
import nl.ing.account.AccountRepository;
import org.junit.Test;
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


        // Given
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        String token = loginService.login("user", "password");
    }
}