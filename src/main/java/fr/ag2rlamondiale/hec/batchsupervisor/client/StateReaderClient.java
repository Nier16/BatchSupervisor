package fr.ag2rlamondiale.hec.batchsupervisor.client;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchStateListDto;
import fr.ag2rlamondiale.hec.batchsupervisor.model.Slot;
import fr.ag2rlamondiale.hec.batchsupervisor.service.process.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class StateReaderClient {

    @Autowired
    @Qualifier("stateReader")
    private WebClient client;


    public Mono<BatchStateListDto> getLastStates(String batchId, LocalDateTime end) {
        return this.client
                .get()
                .uri("/getStates?batchId=" + batchId + "&end=" + end.toString())
                .retrieve()
                .bodyToMono(BatchStateListDto.class);
    }

    public Mono<BatchStateListDto> getLastStateForEachBatch() {
        return this.client
                .get()
                .uri("/getStates")
                .retrieve()
                .bodyToMono(BatchStateListDto.class);
    }

    public Mono<BatchStateListDto> getStatesForPeriod(Slot period){
        return this.client
                .get()
                .uri("/getStates?start=" + period.getStart().toString() + "&end=" + period.getEnd().toString())
                .retrieve()
                .bodyToMono(BatchStateListDto.class);
    }
}
