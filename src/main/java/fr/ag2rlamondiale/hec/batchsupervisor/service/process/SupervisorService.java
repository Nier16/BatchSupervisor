package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import java.time.LocalDateTime;
import java.util.List;

import fr.ag2rlamondiale.hec.batchsupervisor.service.data.ScheduleDataService;
import fr.ag2rlamondiale.hec.batchsupervisor.service.data.StateDataService;
import fr.ag2rlamondiale.hec.batchsupervisor.useful.Useful;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SupervisorService {
	public static LocalDateTime startTime = Useful.getTodayWithTime(12, 45);//Useful.getNowWithoutSecondes();

	private final BatchService batchService;
	private final ScheduleDataService scheduleDataService;
	private final StateDataService stateDataService;
	private final ReportService reportService;

	public void proceed() {
		stateDataService.loadLastStateForEachBatch();
		reportService.initReportNeeded();
		scheduleDataService.loadAllActiveSchedules();

		List<String> batchIds = scheduleDataService.getAllBatchIds();
		for (String batchId : batchIds) {
			batchService.proceed(batchId);
		}
		reportService.proceed();
		stateDataService.saveAllStates();
	}

	static public LocalDateTime getStartTime(){
		return startTime;
	}
}
