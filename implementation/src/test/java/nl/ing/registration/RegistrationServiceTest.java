package nl.ing.registration;

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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountNumberValidationService accountNumberValidationService;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    public void should_throw_exception_if_account_number_invalid() {
        // Expect
        expectedEx.expect(AccountNumberNotFoundException.class);
        expectedEx.expectMessage("The account number was not found: 1234 for user: user");

        // Given
        when(accountNumberValidationService.validateAccountExists("1234")).thenReturn(false);

        // When
        registrationService.registerAccount("1234", "user", "password");
    }

    @Test
    public void should_throw_exception_if_username_already_exists() {
        // Expect
        expectedEx.expect(AccountAlreadyExistsException.class);
        expectedEx.expectMessage("An account already exists for: user");

        // Given
        when(accountNumberValidationService.validateAccountExists("1234")).thenReturn(true);
        when(accountRepository.findById("user")).thenReturn(Optional.of(new Account()));

        // When
        registrationService.registerAccount("1234", "user", "password");
    }

    @Test
    public void should_register_new_account() {
        // Given
        when(accountNumberValidationService.validateAccountExists("1234")).thenReturn(true);
        when(accountRepository.findById("user")).thenReturn(Optional.empty());

        // When
        registrationService.registerAccount("1234", "user", "password");

        // Then
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hahedPassword = encoder.encode("password");
        verify(accountRepository).save(eq(new Account("1234", "user", hahedPassword)));
    }
}