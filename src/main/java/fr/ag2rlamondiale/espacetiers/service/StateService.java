package fr.ag2rlamondiale.espacetiers.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ag2rlamondiale.espacetiers.model.BatchState;
import fr.ag2rlamondiale.espacetiers.model.SupervisorResult;
import fr.ag2rlamondiale.espacetiers.useful.Useful;
import org.springframework.stereotype.Service;

@Service
public class StateService {
	private long idBatch;
	private List<BatchState> states; 
	private int actElement;
	private static final Logger log = LoggerFactory.getLogger(StateService.class); 
	private BatchState lastSaved;
	
	public void load(long idBatch) {
		this.idBatch = idBatch;
		// get Last states for idBatch
		
		// 1L
		BatchState s0 = new BatchState(1L, Useful.getTodayWithTime(12, 0).minusDays(1), Useful.getTodayWithTime(13, 5).minusDays(1), SupervisorResult.OK);
		BatchState s1 = new BatchState(1L, Useful.getTodayWithTime(11, 7));
		BatchState s2 = new BatchState(1L, Useful.getTodayWithTime(11, 20));
		BatchState s3 = new BatchState(1L, Useful.getTodayWithTime(11, 40));
		//BatchState s4 = new BatchState(1L, Useful.getTodayWithTime(13, 40));
		
		// 2L
		BatchState s5 = new BatchState(2L, Useful.getTodayWithTime(5, 46));
		BatchState s6 = new BatchState(2L, Useful.getTodayWithTime(6, 30));
		BatchState s7 = new BatchState(2L, Useful.getTodayWithTime(7, 10));
		//BatchState s8 = new BatchState(2L, Useful.getTodayWithTime(11, 7));
		
		if(this.idBatch == 1L)
			states = Arrays.asList(s0, s1, s2, s3);
		else
			states = Arrays.asList(s5, s6, s7);

		this.actElement = 0;
		this.lastSaved = null;
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
	
	public void addState(SupervisorResult result, LocalDateTime createTime) {
		BatchState state = new BatchState();
		state.setIdBatch(this.idBatch);
		state.setResult(result);
		state.setSupervisorDate(LocalDateTime.now());
		state.setCreateDate(createTime);
		this.lastSaved = state;
		StateService.saveState(state);
	}
	
	public void updateState(BatchState state, SupervisorResult result) {
		state.setResult(result);
		state.setSupervisorDate(LocalDateTime.now());
		this.lastSaved = state;
		StateService.saveState(state);
	}
	
	static public void saveState(BatchState state) {
		// save to database
		log.info(state.toString());
	}
	
}
