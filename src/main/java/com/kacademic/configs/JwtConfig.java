package com.kacademic.configs;

import io.jsonwebtoken.SignatureAlgorithm;

public class JwtConfig {
    
    // private static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");
    private static final String SECRET_KEY = generateSecretKey();

    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private static final long EXPIRATION_TIME_MS = 3600000L;

    public static String getSecretKey() {

        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) return "DEFAULT_SECRET_KEY";
        return SECRET_KEY;

    }

    public static long getExpirationTime() {
        return EXPIRATION_TIME_MS;
    }

    public static SignatureAlgorithm getSignatureAlgorithm() {
        return SIGNATURE_ALGORITHM;
    }

    private static String generateSecretKey() {
        
        byte[] keyBytes = new byte[32];
        new java.security.SecureRandom().nextBytes(keyBytes);
        return java.util.Base64.getEncoder().encodeToString(keyBytes);

    }

}
