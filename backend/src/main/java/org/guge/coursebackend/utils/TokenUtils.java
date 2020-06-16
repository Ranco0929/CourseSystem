package org.guge.coursebackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.Signature;
import java.util.Date;

public class TokenUtils {

    public static final String SECRET = "GugeNiubi!!!!!!!!";

    /**
     * Token Generation
     * @param email userEmail
     * @return
     */
    public static String createToken(String email) {
        String issuer = "guge-course.com";

        String subject = "hitrel@outlook.com";

        long ttlMillis = 3600000;

        return createToken(email, issuer, subject, ttlMillis);
    }

    public static String createVerifyToken(String emailWithCode) {
        String issuer = "guge-course.com";

        String subject = "hitrel@outlook.com";

        long ttlMillis = 3600000;

        return createToken(emailWithCode, issuer, subject, ttlMillis);
    }

    public static String createToken(String email, String issuer, String subject, long ttlMillis) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();

        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signedKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setId(email)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setSubject(subject)
                .signWith(signatureAlgorithm, signedKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
        }

        return builder.compact();
    }

    public static Claims parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(token).getBody();

        return claims;
    }
}
