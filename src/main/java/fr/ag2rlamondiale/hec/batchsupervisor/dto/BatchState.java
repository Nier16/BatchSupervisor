package fr.ag2rlamondiale.hec.batchsupervisor.dto;

import fr.ag2rlamondiale.hec.batchsupervisor.model.SupervisorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BatchState {
	private Integer idExecution;
	private String idBatch;
	private LocalDateTime createDate;
	private LocalDateTime supervisorDate;
	private SupervisorResult result;

	public BatchState(){

	}

	public BatchState(String idBatch, LocalDateTime createDate, LocalDateTime supervisorDate, SupervisorResult result){
	}
	
	public BatchState(String idBatch, LocalDateTime createDate) {
		this.idBatch = idBatch;
		this.createDate = createDate;
	}

	public boolean wasProceed() {
		return this.supervisorDate != null && this.result != null;
	}
}
