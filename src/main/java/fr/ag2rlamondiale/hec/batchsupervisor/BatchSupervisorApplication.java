package fr.ag2rlamondiale.hec.batchsupervisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BatchSupervisorApplication {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(BatchSupervisorApplication.class, args)));
    }
}
