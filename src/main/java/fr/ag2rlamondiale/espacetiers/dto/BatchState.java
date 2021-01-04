package fr.ag2rlamondiale.espacetiers.dto;

import fr.ag2rlamondiale.espacetiers.model.SupervisorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchState {
	private int idBatch;
	private LocalDateTime createDate;
	private LocalDateTime supervisorDate;
	private SupervisorResult result;
	
	public BatchState(int idBatch, LocalDateTime createDate) {
		this.idBatch = idBatch;
		this.createDate = createDate;
	}

	public boolean wasProceed() {
		return this.supervisorDate != null && this.result != null;
	}
}
