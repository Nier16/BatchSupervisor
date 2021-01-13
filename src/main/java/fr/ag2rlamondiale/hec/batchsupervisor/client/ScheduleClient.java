package fr.ag2rlamondiale.hec.batchsupervisor.client;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.ScheduleListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ScheduleClient {

    @Autowired
    @Qualifier("schedule")
    private WebClient client;

    public Mono<ScheduleListDto> getAllActiveSchedules() {
        return this.client.get().uri("/getSchedules?active=true")
                .retrieve()
                .bodyToMono(ScheduleListDto.class);
    }

}
