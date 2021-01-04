package fr.ag2rlamondiale.espacetiers.service.process;

import java.time.LocalDateTime;
import java.util.List;

import fr.ag2rlamondiale.espacetiers.service.data.ScheduleDataService;
import fr.ag2rlamondiale.espacetiers.service.data.StateDataService;
import fr.ag2rlamondiale.espacetiers.useful.Useful;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SupervisorService {
	public final static LocalDateTime startTime = Useful.getTodayWithTime(11, 45);//Useful.getNowWithoutSecondes();

	private final BatchService batchService;
	private final ScheduleDataService scheduleDataService;
	private final StateDataService stateDataService;
	private final ReportService reportService;

	public void proceed() {
		List<Integer> batchIds = scheduleDataService.getAllBatchIds();
		for (Integer batchId : batchIds) {
			batchService.proceed(batchId);
		}
		stateDataService.saveAllStates();
		reportService.incidentReport();
	}
}
