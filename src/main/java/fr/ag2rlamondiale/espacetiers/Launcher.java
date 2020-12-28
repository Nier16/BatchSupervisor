package fr.ag2rlamondiale.espacetiers;

import fr.ag2rlamondiale.espacetiers.service.SupervisorService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class Launcher implements ApplicationRunner {
    private final SupervisorService supervisorService;

    @Override
    public void run(ApplicationArguments args) {
        supervisorService.proceed();
    }
}
