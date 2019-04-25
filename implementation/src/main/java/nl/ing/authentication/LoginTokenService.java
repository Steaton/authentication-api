package nl.ing.authentication;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.ing.keystore.KeystoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Service
public class LoginTokenService {

    @Autowired
    private KeystoreService keystoreService;

    public String createLoginToken(String id, long ttlMillis) {
        Key signingKey = generateSigningKey(SignatureAlgorithm.HS256);
        JwtBuilder builder = buildJwtToken(id, ttlMillis, signingKey);
        return builder.compact();
    }

    private JwtBuilder buildJwtToken(String id, long ttlMillis, Key signingKey) {
        return Jwts.builder()
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
                .signWith(SignatureAlgorithm.HS256, signingKey);
    }

    private Key generateSigningKey(SignatureAlgorithm signatureAlgorithm) {
        byte[] apiKeySecretBytes = keystoreService.loadTokenGenerationKey().getEncoded();
        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }

}
