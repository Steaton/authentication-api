package nl.ing.authentication;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginTokenServiceTest {

    private LoginTokenService loginTokenService;

    @Test
    @Ignore
    public void should_create_login_token() {
        loginTokenService = new LoginTokenService();
        String loginToken = loginTokenService.createLoginToken("user", 60000);
        assertEquals("", loginToken);
    }
}