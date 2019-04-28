package nl.ing.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountNumberValidationService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean validateAccountExists(String accountNumber) {
        try {
            ResponseEntity<AccountResponse> response = restTemplate.getForEntity("https://wiremock:8444/accounts/" + accountNumber, AccountResponse.class);
            return HttpStatus.OK.equals(response.getStatusCode());
        } catch (HttpClientErrorException e) {
            return false;
        }
    }
}
