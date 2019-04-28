package nl.ing.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Configuration
@SuppressWarnings("deprecation")
public class SslConfiguration {

    @Value("${http.client.ssl.key-store}")
    private String keyStore;

    @Value("${http.client.ssl.key-store-password}")
    private String keyStorePassword;

    @Value("${http.client.ssl.trust-store}")
    private String trustStore;

    @Value("${http.client.ssl.trust-store-password}")
    private String triustStorePassword;

    @Bean
    RestTemplate restTemplate() throws Exception {
        HttpClient httpClient = instantiateHttpClient();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

    private CloseableHttpClient instantiateHttpClient() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {
        KeyStore keystore = getKeyStore(keyStore, keyStorePassword);
        KeyStore truststore = getKeyStore(trustStore, triustStorePassword);
        return getHttpClient(keystore, truststore, keyStorePassword);
    }

    private KeyStore getKeyStore(String resource, String keystorePassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keystore = KeyStore.getInstance("JKS");
        InputStream keystoreInput = new FileInputStream(resource);
        keystore.load(keystoreInput, keystorePassword.toCharArray());
        return keystore;
    }

    private DefaultHttpClient getHttpClient(KeyStore keystore, KeyStore truststore, String keystorePassword) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", new SSLSocketFactory(keystore, keystorePassword, truststore), 8444));
        BasicHttpParams httpParams = new BasicHttpParams();
        return new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, schemeRegistry), httpParams);
    }
}
