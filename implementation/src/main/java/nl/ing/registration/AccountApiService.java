package nl.ing.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountApiService {

    @Autowired
    private RestTemplate restTemplate;

    public void getAccountInformation(String accountNumber) {
        restTemplate.getForEntity("https://wiremock:8444/accounts/" + accountNumber, String.class);
    }

//        try {
//            DefaultHttpClient httpClient = instantiateHttpClient();
//
//            HttpGet request = new HttpGet("https://wiremock:8444/accounts/77853449");
//            HttpResponse httpResponse = httpClient.execute(request);
//            httpResponse.getEntity();
//
//        } catch (KeyManagementException |
//                UnrecoverableKeyException |
//                NoSuchAlgorithmException |
//                KeyStoreException |
//                IOException |
//                CertificateException e) {
//
//            e.printStackTrace();
//        }
//    }

//    private DefaultHttpClient instantiateHttpClient() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {
//        KeyStore keystore = getKeyStore("C:/Development/authentication-api/client.jks");
//        KeyStore truststore = getKeyStore("C:/Development/authentication-api/conf/server-truststore.jks");
//        return getHttpClient(keystore, truststore);
//    }
//
//    private KeyStore getKeyStore(String s) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
//        KeyStore keystore = KeyStore.getInstance("JKS");
//        InputStream keystoreInput = new FileInputStream(s);
//        keystore.load(keystoreInput, "changeit".toCharArray());
//        return keystore;
//    }
//
//    private DefaultHttpClient getHttpClient(KeyStore keystore, KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
//        SchemeRegistry schemeRegistry = new SchemeRegistry();
//        schemeRegistry.register(new Scheme("https", new SSLSocketFactory(keystore, "changeit", truststore), 8444));
//        BasicHttpParams httpParams = new BasicHttpParams();
//        return new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, schemeRegistry), httpParams);
//    }
}
