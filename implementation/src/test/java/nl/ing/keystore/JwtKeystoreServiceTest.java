package nl.ing.keystore;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.SecretKey;
import java.io.IOException;

public class JwtKeystoreServiceTest {

    private static final String CLIENT_KEYSTORE_PASSWORD = "changeit";

    private JwtKeystoreService jwtKeystoreService;

    @Before
    public void setup() throws IOException {
        String clientKeystorePath = new ClassPathResource("jwt-keystore.p12").getFile().getAbsolutePath();
        jwtKeystoreService = new JwtKeystoreService(CLIENT_KEYSTORE_PASSWORD, clientKeystorePath);
    }

    @Test
    public void should_load_key_from_keystore() {
        SecretKey secretKey = jwtKeystoreService.loadJwtTokenGenerationKey();
        System.out.println(secretKey);
    }
}