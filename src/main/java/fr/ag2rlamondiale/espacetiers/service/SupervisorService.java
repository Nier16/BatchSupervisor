package fr.ag2rlamondiale.espacetiers.service;

import java.time.LocalDateTime;
import java.util.List;

import fr.ag2rlamondiale.espacetiers.useful.Useful;

public class SupervisorService {
	public final static LocalDateTime startTime = Useful.getTodayWithTime(11, 45);//Useful.getNowWithoutSecondes();
	
	public void traitement() {
		List<Long> batchIds = PlannificationService.getAllBatchIds();
		for(int i = 0; i < batchIds.size() ; i++) {
			BatchService bService = new BatchService(batchIds.get(i));
			bService.traitement();
		}
	}
}
