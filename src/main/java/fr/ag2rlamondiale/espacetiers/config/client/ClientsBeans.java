package fr.ag2rlamondiale.espacetiers.config.client;

import fr.ag2rlamondiale.espacetiers.config.security.jwt.ClientTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientsBeans {

    @Value("${ag2r.hec.schedule.base-url}")
    private String scheduleBaseUrl;

    private final WebClient.Builder builder;
    private final ClientTokenProvider clientTokenProvider;

    public ClientsBeans(WebClient.Builder builder, ClientTokenProvider clientTokenProvider){
        this.builder = builder;
        this.clientTokenProvider = clientTokenProvider;
    }

    @Bean(name = "schedule")
    public WebClient scheduleWebClient() {
        return builder
                .baseUrl(scheduleBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, clientTokenProvider.injectHeader())
                .build();
    }

}
