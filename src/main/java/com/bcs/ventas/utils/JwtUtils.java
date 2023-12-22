package com.bcs.ventas.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class JwtUtils {

    @Value("${security.signing-key}")
    private String signingKey;

    public Claims verify(String authorization) throws Exception {

        byte[] secret = signingKey.getBytes();

        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authorization).getBody();
            return claims;
        } catch(Exception e) {
            throw new AccessDeniedException("Access Denied");
        }

    }
}
