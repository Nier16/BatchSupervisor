package fr.ag2rlamondiale.espacetiers;

import fr.ag2rlamondiale.espacetiers.service.SupervisorService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@SpringBootApplication
public class BatchSupervisorApplication {

	@Component
	@AllArgsConstructor
	public static class DataLoader implements ApplicationRunner {
		private final SupervisorService supervisorService;

		@Override
		public void run(ApplicationArguments args) {
			supervisorService.proceed();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BatchSupervisorApplication.class, args);
	}
	


}
