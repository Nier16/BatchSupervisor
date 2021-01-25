package fr.ag2rlamondiale.hec.batchsupervisor.config.security.jwt;

import fr.ag2rlamondiale.hec.jwtToken.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClientTokenProvider {
    public static final String IDREN_KEY = "idren";
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientTokenProvider.class);
    @Value("${claim.idren:123}")
    private String supervisorIdren;

    public String injectHeader(String privateKey) {
        Map<String, Object> claims = new HashMap<>();
        LOGGER.debug("Put idren in token to call microservice : " + supervisorIdren);
        claims.put(IDREN_KEY, supervisorIdren);
        Date date = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000);
        return JwtTokenUtil.createBearerToken(claims, date, privateKey);
    }

}
