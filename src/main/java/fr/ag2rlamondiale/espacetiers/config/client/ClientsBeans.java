package fr.ag2rlamondiale.espacetiers.config.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class ClientsBeans {

   // @Value("${ag2r.hec.planification.base-url}")
    private String planificationBaseUrl = "https://localhost:8080";

    @Autowired
    WebClient.Builder builder;

    @Bean(name = "planification")
    public WebClient planificationWebClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(planificationBaseUrl);
        return builder.uriBuilderFactory(factory).build();
    }

}
