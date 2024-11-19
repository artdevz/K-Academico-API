package com.kacademico.configs.jwt;

import io.jsonwebtoken.SignatureAlgorithm;

public class JwtConfig {
    
    public static String SECRETKEY = "SECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEY";
    public static final SignatureAlgorithm SIGNATURE = SignatureAlgorithm.HS256;
    public static final int EXPIRATEDTOKENTIME = 1; 

}
