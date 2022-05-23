package com.creato.Utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.creato.Entities.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	private static String secret_key = "The king in the north is Jon";
	
	private static Long expiration_time =  (long) (30 * 60); //30 minutese
	
	public String generateJwt(UserEntity user) {
		
		Long currentTimeInMilli = System.currentTimeMillis();
		
		Long expirationTimeInMilli = currentTimeInMilli + expiration_time * 1000;
		
		Date issuedAt = new Date(currentTimeInMilli); 
		
		Date expirationTime = new Date(expirationTimeInMilli);
		
		//claims
		Claims claims = Jwts.claims().setId(user.getUsername()).setIssuedAt(issuedAt).setExpiration(expirationTime);
		
		//payload
		claims.put("isAdmin", user.getIsAdmin());
		claims.put("username", user.getUsername());
		
		return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret_key)
                .compact();
	}
	
	public Claims verify(String authorization) throws Exception {

        try {
            Claims claims = Jwts.parser().setSigningKey(secret_key).parseClaimsJws(authorization).getBody();
            return claims;
        } catch(Exception e) {
            throw new Exception("Access Denied");
        }

    }
}
