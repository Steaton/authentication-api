package nl.ing.authentication;

import nl.ing.account.Account;
import nl.ing.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoginTokenService loginTokenService;

    public String login(String username, String password) {
        Account account = findAccount(username);
        validatePassword(password, account.getHashedPassword(), username);
        return loginTokenService.createLoginToken(username, 3600000);
    }

    private Account findAccount(String username) {
        Optional<Account> account = accountRepository.findById(username);
        if (account.isPresent()) {
            return account.get();
        } else {
            throw new AccountDoesNotExistException("Login failed - account does not exist for " + username);
        }
    }

    private void validatePassword(String password, String hashedPassword, String username) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(password, hashedPassword);
        if (!matches) {
            throw new PasswordIncorrectException("Login failed - password is incorrect for " + username);
        }
    }
}
