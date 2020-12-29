package fr.ag2rlamondiale.espacetiers.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ag2rlamondiale.espacetiers.client.StateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ag2rlamondiale.espacetiers.dto.BatchState;
import fr.ag2rlamondiale.espacetiers.model.SupervisorResult;
import fr.ag2rlamondiale.espacetiers.useful.Useful;
import org.springframework.stereotype.Service;

@Service
public class StateService {
	private static final Logger log = LoggerFactory.getLogger(StateService.class);

	private final StateClient stateClient;

	private int idBatch;
	private List<BatchState> states; 
	private int actElement;
	private BatchState lastSaved;
	private List<BatchState> statesToSave = new ArrayList<>();

	public StateService(StateClient stateClient){
		this.stateClient = stateClient;
	}

	public void load(int idBatch) {
		this.idBatch = idBatch;
		this.actElement = 0;
		this.lastSaved = null;
		this.states = stateClient.
				getLastStates(idBatch).
				block().
				getValues();
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
	
	public BatchState lastSaved() {
		return this.lastSaved;
	}
	
	public void createStateLater(SupervisorResult result, LocalDateTime createTime) {
		BatchState state = new BatchState();
		state.setIdBatch(this.idBatch);
		state.setResult(result);
		state.setSupervisorDate(LocalDateTime.now());
		state.setCreateDate(createTime);

		saveStateLater(state);
	}
	
	public void updateStateLater(BatchState state, SupervisorResult result) {
		state.setResult(result);
		state.setSupervisorDate(LocalDateTime.now());
		this.lastSaved = state;
		saveStateLater(state);
	}
	
	public void saveStateLater(BatchState state) {
		this.lastSaved = state;
		this.statesToSave.add(state);
		log.info(state.toString());
	}

	public void saveAllStates() {
		if(!this.statesToSave.isEmpty()){
			this.stateClient.saveAllStates(this.statesToSave);
		}
	}
	
}
