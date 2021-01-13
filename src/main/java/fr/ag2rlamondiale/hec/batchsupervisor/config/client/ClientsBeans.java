package fr.ag2rlamondiale.hec.batchsupervisor.config.client;

import fr.ag2rlamondiale.hec.batchsupervisor.config.security.jwt.ClientTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientsBeans {

    @Value("${ag2r.hec.schedule.base-url}")
    private String scheduleBaseUrl;

    @Value("${ag2r.hec.state.writer.base-url}")
    private String stateWriterBaseUrl;

    @Value("${ag2r.hec.state.reader.base-url}")
    private String stateReaderBaseUrl;

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

    @Bean(name = "stateWriter")
    public WebClient stateWriteWebClient() {
        return builder
                .baseUrl(stateWriterBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, clientTokenProvider.injectHeader())
                .build();
    }

    @Bean(name = "stateReader")
    public WebClient stateWebClient() {
        return builder
                .baseUrl(stateReaderBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, clientTokenProvider.injectHeader())
                .build();
    }

}
