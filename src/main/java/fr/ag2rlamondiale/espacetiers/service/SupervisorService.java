package fr.ag2rlamondiale.espacetiers.service;

import java.time.LocalDateTime;
import java.util.List;

import fr.ag2rlamondiale.espacetiers.client.PlanificationClient;
import fr.ag2rlamondiale.espacetiers.useful.Useful;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupervisorService {
	public final static LocalDateTime startTime = Useful.getTodayWithTime(11, 45);//Useful.getNowWithoutSecondes();

	public void traitement(PlanificationClient planificationClient) {
		PlannificationService.planificationClient = planificationClient;
		List<Integer> batchIds = PlannificationService.getAllBatchIds();
		for(int i = 0; i < batchIds.size() ; i++) {
			BatchService bService = new BatchService(batchIds.get(i));
			bService.traitement();
		}
	}
}
