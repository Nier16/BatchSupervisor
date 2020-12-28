package fr.ag2rlamondiale.espacetiers.client;

import fr.ag2rlamondiale.espacetiers.dto.ScheduleListDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ScheduleClient {

    @Qualifier("schedule")
    private final WebClient client;

    public Mono<ScheduleListDto> getAllActiveSchedules() {
        return this.client.get().uri("/getSchedules")
                .retrieve()
                .bodyToMono(ScheduleListDto.class);
    }

}
