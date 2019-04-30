package nl.ing.authentication;

import nl.ing.account.Account;
import nl.ing.account.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoginTokenService loginTokenService;

    public String login(String username, String password) {
        checkNotNullOrEmpty(username, password);
        Account account = findAccount(username);
        validateAccountNotLocked(account);
        updateFailedLoginAttempts(validatePassword(password, account.getHashedPassword()), account);
        return loginTokenService.createLoginToken(username, 3600000);
    }

    private void checkNotNullOrEmpty(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        } else if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    private Account findAccount(String username) {
        Optional<Account> account = accountRepository.findById(username);
        if (account.isPresent()) {
            LOGGER.info("Account found for: {}", account.get().getUsername());
            return account.get();
        } else {
            throw new AccountDoesNotExistException("Login failed - account does not exist for: " + username);
        }
    }

    private void validateAccountNotLocked(Account account) {
        if (account.isLocked()) {
            lockedAccountException(account);
        }
    }

    private void lockedAccountException(Account account) {
        throw new AccountIsLockedException("Login failed - account is locked for: " + account.getUsername());
    }

    private boolean validatePassword(String password, String hashedPassword) {
        return new BCryptPasswordEncoder().matches(password, hashedPassword);
    }

    private void updateFailedLoginAttempts(boolean passwordIsCorrect, Account account) {
        if (passwordIsCorrect) {
            loginSuccessful(account);
        } else {
            loginFailed(account);
        }
    }

    private void loginSuccessful(Account account) {
        LOGGER.info("Password validated successfully for: {}", account.getUsername());
        if (account.getFailedLoginAttempts() > 0) {
            resetAccountFailedLogins(account);
        }
    }

    private void resetAccountFailedLogins(Account account) {
        account.unlock();
        accountRepository.save(account);
    }

    private void loginFailed(Account account) {
        updateFailedLoginCount(account);
        passwordIncorrectException(account);
    }

    private void updateFailedLoginCount(Account account) {
        account.incrementFailedLoginCount();
        accountRepository.save(account);
    }

    private void passwordIncorrectException(Account account) {
        throw new PasswordIncorrectException("Login failed - password is incorrect for: " + account.getUsername());
    }
}
