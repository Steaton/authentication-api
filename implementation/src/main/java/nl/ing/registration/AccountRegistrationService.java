package nl.ing.registration;

import nl.ing.account.Account;
import nl.ing.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountRegistrationService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountNumberValidationService accountNumberValidationService;

    public void registerAccount(String accountNumber, String username, String password) {
        validateAccountNumer(accountNumber, username);
        validateUsernameAvailable(username);
        Account account = mapToAccount(accountNumber, username, password);
        accountRepository.save(account);
    }


    private void validateAccountNumer(String accountNumber, String username) {
        boolean valid = accountNumberValidationService.validateAccountExists(accountNumber);
        if (!valid) {
            throw new AccountNumberNotFoundException("The account number was not found: " + accountNumber + " for user: " + username);
        }
    }

    private void validateUsernameAvailable(String username) {
        Optional<Account> account = accountRepository.findById(username);
        if (account.isPresent()) {
            throw new AccountAlreadyExistsException("An account already exists for: " + username);
        }
    }

    private Account mapToAccount(String accountNumber, String username, String password) {
        String hashedPassword = encodePassword(password);
        return new Account(accountNumber, username, hashedPassword);
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

}
