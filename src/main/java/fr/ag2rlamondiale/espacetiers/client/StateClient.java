package fr.ag2rlamondiale.espacetiers.client;

import fr.ag2rlamondiale.espacetiers.dto.BatchState;
import fr.ag2rlamondiale.espacetiers.dto.BatchStateListDto;
import fr.ag2rlamondiale.espacetiers.service.SupervisorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StateClient {

    @Autowired
    @Qualifier("state")
    private WebClient client;


    public Mono<BatchStateListDto> getLastStates(int batchId) {
        return this.client
                .get()
                .uri("/getStates?batchId=" + batchId + "&end" + SupervisorService.startTime.toString())
                .retrieve()
                .bodyToMono(BatchStateListDto.class);
    }

    public void saveAllStates(List<BatchState> states) {
        this.client
                .post()
                .uri("/saveStates")
                .bodyValue(new BatchStateListDto(states))
                .exchange()
                .block();
    }
}
