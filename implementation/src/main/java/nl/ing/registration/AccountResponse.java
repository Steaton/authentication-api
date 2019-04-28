package nl.ing.registration;

public class AccountResponse {

    private String iban;

    private String ownerId;

    public AccountResponse() {
    }

    public AccountResponse(String iban, String ownerId) {
        this.iban = iban;
        this.ownerId = ownerId;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
