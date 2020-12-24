package fr.ag2rlamondiale.espacetiers.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ag2rlamondiale.espacetiers.model.BatchState;
import fr.ag2rlamondiale.espacetiers.model.Plage;
import fr.ag2rlamondiale.espacetiers.model.SupervisorResult;


public class BatchService {
	private PlannificationService pService;
	private StateService sService;
	private static final Logger log = LoggerFactory.getLogger(BatchService.class);  
	
	public BatchService(Integer idBatch) {
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

		pService.loadPlages(sState.getCreateDate(), SupervisorService.startTime);
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

		if(sService.lastSaved() != null && sService.lastSaved().getResult() != SupervisorResult.OK
				&& (sService.first() == null || firstWasProceed || sService.first().getResult() == SupervisorResult.OK)) {
			// sauvgarder pour envoi mail
			log.info("Envoie notification KO pour " + sService.lastSaved().toString());
		}
		
	}
}
