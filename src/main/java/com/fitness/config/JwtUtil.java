package com.fitness.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fitness.model.Usuario;
import java.util.Date;

// Aqui fica as funções de token jwt que serão reutilizadas em todo o projeto
public class JwtUtil {
    private static final String segredo = "A_RESPOSTA_PARA_A_VIDA_UNIVERSO_E_TUDO_MAIS_E_42";
    private static final String emissor = "fitness-api";
    private static final Algorithm algoritmo = Algorithm.HMAC256(segredo);

    public static String gerarToken(Usuario usuario) {
        // Duas horas para regerar o token
        long tempoExpiracao = System.currentTimeMillis() + 7200000;

        return JWT.create()
                .withIssuer(emissor)
                .withClaim("userId", usuario.getId())
                .withClaim("email", usuario.getEmail())
                .withClaim("tipo", usuario.getTipo())
                .withExpiresAt(new Date(tempoExpiracao))
                .sign(algoritmo);
    }

    public static DecodedJWT validarToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algoritmo)
                .withIssuer(emissor)
                .build();
        // Se o token foi adulterado ou já venceu, o verify lança exceção!
        return verifier.verify(token);
    }
}
