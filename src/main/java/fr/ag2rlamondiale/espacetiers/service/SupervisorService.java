package fr.ag2rlamondiale.espacetiers.service;

import java.time.LocalDateTime;
import java.util.List;

import fr.ag2rlamondiale.espacetiers.useful.Useful;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SupervisorService {
	public final static LocalDateTime startTime = Useful.getTodayWithTime(11, 45);//Useful.getNowWithoutSecondes();

	private final BatchService batchService;
	private final ScheduleService scheduleService;
	private final StateService stateService;

	public void proceed() {
		scheduleService.loadAllActiveSchedules();
		List<Integer> batchIds = scheduleService.getAllBatchIds();
		for (Integer batchId : batchIds) {
			batchService.proceed(batchId);
		}
		stateService.saveAllStates();
	}
}
