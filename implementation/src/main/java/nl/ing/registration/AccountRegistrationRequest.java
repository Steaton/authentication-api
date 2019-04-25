package nl.ing.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRegistrationRequest {

    @JsonProperty
    private String accountNumber;

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    public AccountRegistrationRequest() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
