package nl.ing.keystore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Service
public class JwtKeystoreService {

    public static final String TOKEN_KEY = "jwt-key";

    private String clientKeystorePassword;

    private String clientKeystorePath;

    public JwtKeystoreService(@Value("${keystore.client.password}") String clientKeystorePassword,
                              @Value("${keystore.client.path}") String clientKeystorePath) {
        this.clientKeystorePassword = clientKeystorePassword;
        this.clientKeystorePath = clientKeystorePath;
    }

    public SecretKey loadJwtTokenGenerationKey() {
        KeyStore keyStore = loadClientKeyStore();
        KeyStore.SecretKeyEntry tokenKeyEntry = getTokenKey(keyStore);
        if (tokenKeyEntry == null) {
            throw new RuntimeException("There was a problem loading the client key from " + clientKeystorePath);
        }
        return tokenKeyEntry.getSecretKey();
    }

    private KeyStore loadClientKeyStore() {
        char[] keyStorePassword = clientKeystorePassword.toCharArray();
        return doLoadKeystore(keyStorePassword);
    }

    private KeyStore doLoadKeystore(char[] keyStorePassword) {
        try (InputStream keyStoreData = new FileInputStream(clientKeystorePath)) {
            return loadKeystoreData(keyStorePassword, keyStoreData);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem loading the client key store from " + clientKeystorePath, e);
        }
    }

    private KeyStore loadKeystoreData(char[] keyStorePassword, InputStream keyStoreData) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(keyStoreData, keyStorePassword);
        return keyStore;
    }

    private KeyStore.SecretKeyEntry getTokenKey(KeyStore keyStore) {
        KeyStore.SecretKeyEntry tokenKeyEntry;
        try {
            tokenKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(TOKEN_KEY, new KeyStore.PasswordProtection(clientKeystorePassword.toCharArray()));
        } catch (Exception e) {
            throw new RuntimeException("There was a problem loading the client key from the key store " + TOKEN_KEY, e);
        }
        return tokenKeyEntry;
    }
}
