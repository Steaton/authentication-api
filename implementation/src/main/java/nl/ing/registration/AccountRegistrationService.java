package nl.ing.registration;

import nl.ing.account.Account;
import nl.ing.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountRegistrationService {

    @Autowired
    private AccountRepository accountRepository;

    public void registerAccount(String accountNumber, String username, String password) {
        // Validate Account Number
        // Check Does Not Already Exist
        // Hash password
        Account account = mapToAccount(accountNumber, username, password);
        accountRepository.save(account);
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
