package fr.ag2rlamondiale.espacetiers;

import fr.ag2rlamondiale.espacetiers.service.SupervisorService;

//@SpringBootApplication
public class XibApplication {

	public static void main(String[] args) {
		//SpringApplication.run(XibApplication.class, args);
		
		new SupervisorService().traitement();
		
	}
	


}
