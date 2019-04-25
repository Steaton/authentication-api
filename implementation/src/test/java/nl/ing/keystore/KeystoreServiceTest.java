package nl.ing.keystore;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class KeystoreServiceTest {

    private KeystoreService keystoreService;

    @Before
    public void setup() throws IOException {
        String clientKeystorePath = new ClassPathResource("test-client.jks").getFile().getAbsolutePath();
        keystoreService = new KeystoreService("changeit", "jwt-keystore.p12");
    }

    @Test
    public void should_load_key_from_keystore() {
        keystoreService.loadTokenGenerationKey();
    }

}