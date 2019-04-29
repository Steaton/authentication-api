package nl.ing.keystore;

import org.springframework.core.io.ClassPathResource;

import javax.crypto.KeyGenerator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;

public class InitialiseJwtKeyStoreTool {

    public static final String TOKEN_KEY = "jwt-key";

    public static final void main(String args[]) {
        try {
            File keystoreFile = new ClassPathResource("local/jwt-keystore.p12").getFile();
            keystoreFile.delete();
        } catch (IOException e) {
            // Do nothing - continue
        }
        createKeyStore("changeit".toCharArray(), "implementation/src/main/resources/jwt-keystore.p12");
    }

    public static KeyStore createKeyStore(char[] keyStorePassword, String clientKeystorePath) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            Key key = keyGen.generateKey();
            keyStore.setKeyEntry(TOKEN_KEY, key, keyStorePassword, null);
            FileOutputStream outputStream = new FileOutputStream(clientKeystorePath);
            keyStore.store(outputStream, keyStorePassword);
            System.out.println("Keystore Created at " + clientKeystorePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
