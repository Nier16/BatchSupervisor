package fr.ag2rlamondiale.hec.batchsupervisor.client;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchStateListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StateWriterClient {

    @Autowired
    @Qualifier("stateWriter")
    private WebClient client;

    public void saveAllStates(List<BatchState> states) {
        this.client
                .post()
                .uri("/saveStates")
                .body(Mono.just(new BatchStateListDto(states)), BatchState.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
