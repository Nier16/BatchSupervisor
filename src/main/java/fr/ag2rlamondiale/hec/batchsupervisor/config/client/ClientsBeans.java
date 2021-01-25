package fr.ag2rlamondiale.hec.batchsupervisor.config.client;

import fr.ag2rlamondiale.hec.batchsupervisor.config.security.jwt.ClientTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientsBeans {

    private final WebClient.Builder builder;
    private final ClientTokenProvider clientTokenProvider;
    @Value("${ag2r.hec.batch.orchestrator.base-url}")
    private String orchestratorBaseUrl;
    @Value("${jwt.private.batch.orchestrator.key}")
    private String privateKeyOrchestrator;

    public ClientsBeans(WebClient.Builder builder, ClientTokenProvider clientTokenProvider) {
        this.builder = builder;
        this.clientTokenProvider = clientTokenProvider;
    }

    @Bean
    public WebClient scheduleWebClient() {
        return builder
                .baseUrl(orchestratorBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, clientTokenProvider.injectHeader(privateKeyOrchestrator))
                .build();
    }

}
