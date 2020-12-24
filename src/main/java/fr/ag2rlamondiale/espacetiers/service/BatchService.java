package fr.ag2rlamondiale.espacetiers.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ag2rlamondiale.espacetiers.model.BatchState;
import fr.ag2rlamondiale.espacetiers.model.Plage;
import fr.ag2rlamondiale.espacetiers.model.SupervisorResult;
import fr.ag2rlamondiale.espacetiers.useful.Useful;

public class BatchService {
	private PlannificationService pService;
	private StateService sService;
	private static final Logger log = LoggerFactory.getLogger(BatchService.class);  
	
	public BatchService(long idBatch) {
		pService = new PlannificationService(idBatch);
		sService = new StateService(idBatch);
	}
	
	void traitement() {
		boolean firstWasProceed = true;
		pService.charger();
		sService.charger();
		BatchState sState = sService.next();
		Plage plage;
		if(sState == null) {
			return;
		}
		
		//LocalDateTime lastLunch = sState.getCreateDate();
		pService.loadPlages(sState.getCreateDate(), SupervisorService.startTime);
		/*
		if(Useful.isToday(lastLunch)) {
			pService.loadPlages(sState.getCreateDate(), SupervisorService.startTime);
		}else if(lastLunch.isAfter(SupervisorService.startTime.minusDays(1))) {
			pService.loadPlages(sState.getCreateDate(), Useful.getTodayMidnight().minusNanos(1));
			pService.loadPlages(Useful.getTodayMidnight(), SupervisorService.startTime);
		}else {
			pService.loadPlages(Useful.getTodayMidnight(), SupervisorService.startTime);
		}
		*/
			
		plage = pService.next();
		
		if(sState.wasProceed()) {
			firstWasProceed = false;
			sState = sService.next();
			plage = pService.next();
		}
		
		while(sState != null || plage != null) {
			if(sState == null) {
				if(!plage.isActif()) {
					sService.addState(SupervisorResult.KO_LANCEMENT, plage.getEnd());
				}
				plage = pService.next();
			}else if(plage == null) {
				sService.updateState(sState, SupervisorResult.KO_PLAGE);
				sState = sService.next();
			}else if(plage.isTimeBefore(sState.getCreateDate())) {
				sService.updateState(sState, SupervisorResult.KO_PLAGE);
				sState = sService.next();
			}else if(plage.isTimeAfter(sState.getCreateDate())) {
				sService.addState(SupervisorResult.KO_LANCEMENT, plage.getEnd());
				plage = pService.next();
			}else {
				sService.updateState(sState, SupervisorResult.OK);
				sState = sService.next();
				plage = pService.next();
			}
		}
		
		// dernier != null
		// 1er = ok && dernier = ko
		// 1er on la traité && dernier = ko
		// dernier != null && dernier ko && (on l'a traité || OK)
		if(sService.lastSaved() != null && sService.lastSaved().getResult() != SupervisorResult.OK
				&& (sService.first() == null || firstWasProceed || sService.first().getResult() == SupervisorResult.OK)) {
			// sauvgarder pour envoi mail
			log.info("Envoie notification KO pour " + sService.lastSaved().toString());
			
		}
		
	}
}
