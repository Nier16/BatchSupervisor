package fr.ag2rlamondiale.espacetiers.client;

import fr.ag2rlamondiale.espacetiers.dto.PlanificationListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PlanificationClient {
        @Autowired
        @Qualifier("planification")
        private WebClient client;

        public Mono<PlanificationListDto> getAllActivePlanifications() {
            return this.client.get().uri("/planifications")
                    .retrieve()
                    .bodyToMono(PlanificationListDto.class);
        }

    }
