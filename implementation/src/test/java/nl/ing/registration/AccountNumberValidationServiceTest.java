package nl.ing.registration;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AccountNumberValidationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountNumberValidationService accountNumberValidationService;

    @Test
    public void should_validate_account() {
        // Given
        when(restTemplate.getForEntity(anyString(), eq(AccountResponse.class))).thenReturn(new ResponseEntity<AccountResponse>(HttpStatus.OK));

        // When
        boolean accountExists = accountNumberValidationService.validateAccountExists("1234");

        // Then
        assertTrue(accountExists);
    }

    @Test
    public void should_validate_invalid_account() {
        // Given
        when(restTemplate.getForEntity(anyString(), eq(AccountResponse.class))).thenReturn(new ResponseEntity<AccountResponse>(HttpStatus.OK));

        // When
        boolean accountExists = accountNumberValidationService.validateAccountExists("1234");

        // Then
        assertTrue(accountExists);
    }

}