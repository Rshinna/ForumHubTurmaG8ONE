package br.com.alura.ForumHub.infra.security;

import br.com.alura.ForumHub.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {

    @Value("${forumhub.jwt.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {

        return JWT.create()
                .withIssuer("ForumHub")
                .withSubject(usuario.getEmail())
                .withExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validarToken(String token) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("ForumHub")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

}
