package ir.sharif.gamein2021.ClientHandler.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Map;

public class JWTUtil {
    private static final String secret = "ahd3qrcyqr8yrq8 q28cdcdaecyqncnoedicugqw qqondxqudgnoqundgDKJAHCFLSDAKCJثباشمنبتشاسباشص";
    private static final Algorithm algorithm = Algorithm.HMAC256(secret);
    private static final String issuer = "auth0";
    private static final JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build();


    public static String generateToken(Map<String, String> payload) {
        String token = JWT.create()
                .withIssuer(issuer)
                .withPayload(payload)
                .sign(algorithm);

        return token;
    }

    public static DecodedJWT decodeToken(String token) {
        return verifier.verify(token);
    }
}
