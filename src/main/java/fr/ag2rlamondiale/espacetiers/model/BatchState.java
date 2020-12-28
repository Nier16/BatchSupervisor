package fr.ag2rlamondiale.espacetiers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchState {
	private long idBatch;
	private LocalDateTime createDate;
	private LocalDateTime supervisorDate;
	private SupervisorResult result;
	
	public BatchState(long idBatch, LocalDateTime createDate) {
		this.idBatch = idBatch;
		this.createDate = createDate;
	}

	public boolean wasProceed() {
		return this.supervisorDate != null && this.result != null;
	}
}
