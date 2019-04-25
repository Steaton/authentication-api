package nl.ing.keystore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Service
public class KeystoreService {

    public static final String TOKEN_KEY = "authTokenKey";

    private String clientKeystorePassword;

    private String clientKeystorePath;

    public KeystoreService(@Value("${keystore.client.password}") String clientKeystorePassword,
                           @Value("${keystore.client.path}") String clientKeystorePath) {
        this.clientKeystorePassword = clientKeystorePassword;
        this.clientKeystorePath = clientKeystorePath;
    }

    public SecretKey loadTokenGenerationKey() {
        KeyStore keyStore = loadClientKeyStore();
        KeyStore.SecretKeyEntry tokenKeyEntry = getTokenKey(keyStore);
        if (tokenKeyEntry == null) {
            tokenKeyEntry = generateAndStoreNewKey(keyStore);
        }
        return tokenKeyEntry.getSecretKey();
    }

    private KeyStore loadClientKeyStore() {
        char[] keyStorePassword = clientKeystorePassword.toCharArray();
        return doLoadKeystore(keyStorePassword);
    }

    private KeyStore doLoadKeystore(char[] keyStorePassword) {
        try(InputStream keyStoreData = new FileInputStream(clientKeystorePath)){
            return loadKeystoreData(keyStorePassword, keyStoreData);
        } catch (FileNotFoundException e) {
            return createKeyStore(keyStorePassword, clientKeystorePath);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem loading the client key store from " + clientKeystorePath, e);
        }
    }

    private KeyStore createKeyStore(char[] keyStorePassword, String clientKeystorePath) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);

            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            Key key = keyGen.generateKey();
            keyStore.setKeyEntry("secret", key, "changeit".toCharArray(), null);

            keyStore.store(new FileOutputStream("jwt-keystore.p12"), "changeit".toCharArray());
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e1) {
            e1.printStackTrace();
        }

        return null;
    }

    private KeyStore loadKeystoreData(char[] keyStorePassword, InputStream keyStoreData) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(keyStoreData, keyStorePassword);
        return keyStore;
    }

    private KeyStore.SecretKeyEntry getTokenKey(KeyStore keyStore) {
        KeyStore.SecretKeyEntry tokenKeyEntry;
        try {
            tokenKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(TOKEN_KEY,null);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem loading the client key from the key store " + TOKEN_KEY, e);
        }
        return tokenKeyEntry;
    }

    private KeyStore.SecretKeyEntry generateAndStoreNewKey(KeyStore keyStore) {
        try {
            SecretKey generatedKey = KeyGenerator.getInstance("AES").generateKey();
            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(generatedKey);
            KeyStore.ProtectionParameter entryPassword =
                    new KeyStore.PasswordProtection(clientKeystorePassword.toCharArray());
            keyStore.setEntry(TOKEN_KEY, secretKeyEntry, entryPassword);
            return secretKeyEntry;
        } catch (Exception e) {
            throw new RuntimeException("There was a problem generating and storing a new client key " + TOKEN_KEY, e);
        }
    }
}
