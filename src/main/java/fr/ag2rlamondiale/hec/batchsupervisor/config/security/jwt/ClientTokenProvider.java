package fr.ag2rlamondiale.hec.batchsupervisor.config.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.ag2rlamondiale.hec.jwtToken.util.JwtTokenUtil;

@Component
public class ClientTokenProvider {
    public static final String IDREN_KEY = "idren";

    @Value("${claim.idren}")
    private String supervisorIdren;

    @Value("${jwt.private.key}")
    private String privateKeyPath;

    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientTokenProvider.class);
    public String injectHeader() {
        Map<String, Object> claims=new HashMap<>();
        LOGGER.debug("Put idren in token to call microservice : " + supervisorIdren);
        claims.put(IDREN_KEY, supervisorIdren);
        Date date = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000);
        return JwtTokenUtil.createBearerToken(claims,date,privateKeyPath);
    }

}
