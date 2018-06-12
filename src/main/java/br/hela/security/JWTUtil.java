package br.hela.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

//	@Value("${jwt.secret}")
	private static String secret = "MYSECRET";

	public static String create(String subject) {
		return Jwts.builder().setSubject(subject).signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
	}

	public static boolean tokenValido(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			String username = claims.getSubject();
			if (username != null) {
				return true;
			}
		}
		return false;
	}

	public static String getUsername(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}

	private static Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}
}
