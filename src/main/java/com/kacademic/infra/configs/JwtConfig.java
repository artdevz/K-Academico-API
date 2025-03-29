package com.kacademic.infra.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtConfig {
    
    @Value("${jwt.secret}")
    private String secretKey;

    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public static final long EXPIRATION_TIME_MS = 3600000L;

    public String getSecretKey() {
        return (secretKey.isEmpty() || secretKey == null)? "DEFAULT_SECRET_KEY_WITH_32_BYTES" : secretKey;
    }

}
