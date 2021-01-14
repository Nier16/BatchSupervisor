package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import java.util.*;

import fr.ag2rlamondiale.hec.batchsupervisor.service.data.StateDataService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import org.springframework.stereotype.Service;

@Service
public class StateService {
	private static final Logger log = LoggerFactory.getLogger(StateService.class);

	private StateDataService stateDataService;

	private String idBatch;
	private List<BatchState> states;
	private int actElement;



	public StateService(StateDataService stateDataService){
		this.stateDataService = stateDataService;
	}

	public void load(String idBatch) {
		this.idBatch = idBatch;
		this.actElement = 0;
		this.states = stateDataService.getLastStatesForBatch(idBatch);
	}

	
	public BatchState next() {
		if(this.actElement < this.states.size()) {
			log.warn("State Actuel : " + this.states.get(this.actElement).toString());
			return this.states.get(this.actElement++);
		}
		log.warn("Les states sont fini pour le batch " + this.idBatch);
		return null;
	}
	
	public BatchState first() {
		if(!this.states.isEmpty()) {
			return this.states.get(0);
		}
		return null;
	}

}
