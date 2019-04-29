package nl.ing.keystore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.SecretKey;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JwtKeystoreServiceTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private static final String CLIENT_KEYSTORE_PASSWORD = "changeit";

    private JwtKeystoreService jwtKeystoreService;

    @Before
    public void setup() throws IOException {

    }

    @Test
    public void should_load_key_from_keystore() throws IOException {
        // Given
        String clientKeystorePath = new ClassPathResource("local/jwt-keystore.p12").getFile().getAbsolutePath();
        jwtKeystoreService = new JwtKeystoreService(CLIENT_KEYSTORE_PASSWORD, clientKeystorePath);

        // When
        SecretKey secretKey = jwtKeystoreService.loadJwtTokenGenerationKey();

        // Then
        assertEquals(32, secretKey.getEncoded().length);
    }

    @Test
    public void should_error_when_keystore_not_found() {
        // Expect
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("There was a problem loading the client key store from: nonexistentkeystore.p12");

        // Given
        jwtKeystoreService = new JwtKeystoreService(CLIENT_KEYSTORE_PASSWORD, "nonexistentkeystore.p12");

        // When
        jwtKeystoreService.loadJwtTokenGenerationKey();
    }

    @Test
    public void should_error_when_key_not_found_in_keystore() throws IOException {
        // Expect
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("There was a problem loading the client key from:");

        // Given
        String clientKeystorePath = new ClassPathResource("empty-keystore.p12").getFile().getAbsolutePath();
        jwtKeystoreService = new JwtKeystoreService(CLIENT_KEYSTORE_PASSWORD, clientKeystorePath);

        // When
        jwtKeystoreService.loadJwtTokenGenerationKey();
    }
}