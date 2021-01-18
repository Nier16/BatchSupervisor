package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import fr.ag2rlamondiale.hec.batchsupervisor.service.data.StateDataService;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import fr.ag2rlamondiale.hec.batchsupervisor.model.Slot;
import fr.ag2rlamondiale.hec.batchsupervisor.model.SupervisorResult;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BatchService {
	private static final Logger log = LoggerFactory.getLogger(BatchService.class);

	private final ScheduleService scheduleService;
	private final StateService stateService;
	private final StateDataService stateDataService;
	private final ReportService reportService;

	public void proceed(String idBatch) {
		boolean firstWasProceed = true;
		Slot slot;
		BatchState batchState;

		scheduleService.load(idBatch);
		stateService.load(idBatch);

		batchState = stateService.next();

		if(batchState == null) {
			return;
		}

		scheduleService.loadSlots(batchState.getCreateDate(), getStartTime());
		slot = scheduleService.next();
		
		if(batchState.wasProceed()) {
			firstWasProceed = false;
			batchState = stateService.next();
			slot = scheduleService.next();
		}
		
		while(batchState != null || slot != null) {
			if(batchState == null) {
				if(!slot.isActive()) {
					stateDataService.createStateLater(SupervisorResult.LAUNCH_KO, idBatch);
				}
				slot = scheduleService.next();
			}
			else if(slot == null) {
				stateDataService.updateStateLater(batchState, SupervisorResult.OUT_SLOT_KO);
				batchState = stateService.next();
			}else if(slot.isTimeBefore(batchState.getCreateDate())) {
				stateDataService.updateStateLater(batchState, SupervisorResult.OUT_SLOT_KO);
				batchState = stateService.next();
			}else if(slot.isTimeAfter(batchState.getCreateDate())) {
				stateDataService.createStateLater(SupervisorResult.LAUNCH_KO, idBatch);
				slot = scheduleService.next();
			}else {
				stateDataService.updateStateLater(batchState, SupervisorResult.OK);
				batchState = stateService.next();
				slot = scheduleService.next();
			}
		}
		this.addToReport(firstWasProceed);
	}

	public LocalDateTime getStartTime(){
		return SupervisorService.startTime;
	}

	public void addToReport(boolean firstWasProceed){
		BatchState lastSaved = stateDataService.getLastSaved();
		if(reportService.haveToReport()){
			if(stateDataService.getLastSaved() != null){
				reportService.addToReportList(lastSaved);
			}else{
				reportService.addToReportList(stateService.first());
			}
		}
		else if(lastSaved != null && lastSaved.getResult() != SupervisorResult.OK
				&& (stateService.first() == null || firstWasProceed || stateService.first().getResult() == SupervisorResult.OK)) {
			// sauvgarder pour envoi mail
			reportService.addToReportList(lastSaved);
			log.info("Envoie notification KO pour " + lastSaved.toString());
		}
	}
}
