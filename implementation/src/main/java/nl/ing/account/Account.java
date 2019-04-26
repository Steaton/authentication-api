package nl.ing.account;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Account {

    @Id
    private String username;

    private String hashedPassword;

    private String accountNumber;

    private int failedLoginAttempts;

    private LocalDateTime lockedUntil;

    public Account() {
    }

    public Account(String accountNumber, String username, String hashedPassword) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public void incrementFailedLoginCount() {
        if (!isLocked() && failedLoginAttempts < 3) {
            failedLoginAttempts++;
            if (failedLoginAttempts == 3) {
                lock();
            }
        }
    }

    public void lock() {
        lockedUntil = LocalDateTime.now().plusDays(1);
    }

    public void unlock() {
        failedLoginAttempts = 0;
        lockedUntil = null;
    }

    public boolean isLocked() {
        return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil);
    }
}
