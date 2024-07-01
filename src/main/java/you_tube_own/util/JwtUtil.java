package you_tube_own.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import you_tube_own.dto.JwtDTO;
import you_tube_own.enums.ProfileRole;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {
    private static String secretKey = "mazgi_bu_jwt_token_uchun_secret_key_sen_hayotda_tapolmaysan_buni_lashara_xaxaxa_vay_mazgi_bu_jwt_token";
    private static Integer tokenLiveTime = 86400000;

    public static String generateToken(Long profileId, String username, ProfileRole role) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), signatureAlgorithm.getJcaName());

        String token = Jwts.builder()
                .issuedAt(new Date())
                .signWith(secretKeySpec)
                .claim("id", profileId)
                .claim("username", username)
                .claim("role", role)
                .expiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .issuer("You Tube")
                .compact();
        return token;
    }

    public static JwtDTO decode(String token) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), signatureAlgorithm.getJcaName());

        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();

        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
        Claims claims = jws.getPayload();

        Integer id = (Integer) claims.get("id");
        String username = (String) claims.get("username");
        String role = (String) claims.get("role");
        ProfileRole profileRole = ProfileRole.valueOf(role);
        return new JwtDTO(id, username, profileRole);
    }
}
