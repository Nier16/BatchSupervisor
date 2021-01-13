package fr.ag2rlamondiale.hec.batchsupervisor.config.supervisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.*;

@Configuration
public class SupervisorConfigurer {
    private static final Logger log = LoggerFactory.getLogger(SupervisorConfigurer.class);

    @Value("${report.path}")
    private String reportPath;

    private SupervisorInformation supervisorInformation;

    @Bean
    public SupervisorInformation loadSupervisorReport(){
        try {
            FileInputStream fi = new FileInputStream(reportPath);
            ObjectInputStream oi = new ObjectInputStream(fi);
            this.supervisorInformation = (SupervisorInformation) oi.readObject();
            oi.close();
            fi.close();
        }catch (Exception e){
            log.error("Erreur lors du chargement du dernier fichier de rapport : " + e.getMessage());
            this.supervisorInformation = new SupervisorInformation();
        }
        return this.supervisorInformation;
    }

    @PreDestroy
    public void saveSupervisorReport(){
        try{
            FileOutputStream f = new FileOutputStream(reportPath);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(this.supervisorInformation);
            o.close();
            f.close();
        }catch (Exception e){
            log.error("Erreur lors de l'ecriture du fichier de rapport : " + e.getMessage());
        }
    }
}
