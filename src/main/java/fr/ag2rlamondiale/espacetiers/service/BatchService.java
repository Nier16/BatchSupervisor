package fr.ag2rlamondiale.espacetiers.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ag2rlamondiale.espacetiers.dto.BatchState;
import fr.ag2rlamondiale.espacetiers.model.Slot;
import fr.ag2rlamondiale.espacetiers.model.SupervisorResult;
import org.springframework.stereotype.Service;

@Service
public class BatchService {

	private final ScheduleService scheduleService;
	private final StateService stateService;
	private static final Logger log = LoggerFactory.getLogger(BatchService.class);

	public BatchService(ScheduleService scheduleService, StateService stateService){
		this.scheduleService = scheduleService;
		this.stateService = stateService;
	}

	void proceed(int idBatch) {
		boolean firstWasProceed = true;
		Slot slot;
		BatchState batchState;

		scheduleService.load(idBatch);
		stateService.load(idBatch);

		batchState = stateService.next();

		if(batchState == null) {
			return;
		}

		scheduleService.loadSlots(batchState.getCreateDate(), SupervisorService.startTime);
		slot = scheduleService.next();
		
		if(batchState.wasProceed()) {
			firstWasProceed = false;
			batchState = stateService.next();
			slot = scheduleService.next();
		}
		
		while(batchState != null || slot != null) {
			if(batchState == null) {
				if(!slot.isActive()) {
					stateService.createStateLater(SupervisorResult.LAUNCH_KO, slot.getEnd());
				}
				slot = scheduleService.next();
			}
			else if(slot == null) {
				stateService.updateStateLater(batchState, SupervisorResult.OUT_SLOT_KO);
				batchState = stateService.next();
			}else if(slot.isTimeBefore(batchState.getCreateDate())) {
				stateService.updateStateLater(batchState, SupervisorResult.OUT_SLOT_KO);
				batchState = stateService.next();
			}else if(slot.isTimeAfter(batchState.getCreateDate())) {
				stateService.createStateLater(SupervisorResult.LAUNCH_KO, slot.getEnd());
				slot = scheduleService.next();
			}else {
				stateService.updateStateLater(batchState, SupervisorResult.OK);
				batchState = stateService.next();
				slot = scheduleService.next();
			}
		}

		if(stateService.lastSaved() != null && stateService.lastSaved().getResult() != SupervisorResult.OK
				&& (stateService.first() == null || firstWasProceed || stateService.first().getResult() == SupervisorResult.OK)) {
			// sauvgarder pour envoi mail
			log.info("Envoie notification KO pour " + stateService.lastSaved().toString());
		}
		
	}
}
