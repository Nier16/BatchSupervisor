package fr.ag2rlamondiale.hec.batchsupervisor.client;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.Report;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class OrchestratorClient {

    private WebClient client;

    public Mono<Report> getOrchestratorReport() {
        return this.client.get().uri("/proceed")
                .retrieve()
                .bodyToMono(Report.class);
    }

}
