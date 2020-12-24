package fr.ag2rlamondiale.espacetiers.model;

import java.time.LocalDateTime;

public class BatchState {
	private long idBatch;
	private LocalDateTime createDate;
	private LocalDateTime supervisorDate;
	private SupervisorResult result;
	
	
	
	public BatchState() {
	}
	
	public BatchState(long idBatch, LocalDateTime createDate) {
		this.idBatch = idBatch;
		this.createDate = createDate;
	}

	public BatchState(long idBatch, LocalDateTime createDate, LocalDateTime supervisorDate, SupervisorResult result) {
		this.idBatch = idBatch;
		this.createDate = createDate;
		this.supervisorDate = supervisorDate;
		this.result = result;
	}

	public boolean wasProceed() {
		return this.supervisorDate != null && this.result != null;
	}
	
	public long getIdBatch() {
		return idBatch;
	}
	public void setIdBatch(long idBatch) {
		this.idBatch = idBatch;
	}
	public SupervisorResult getResult() {
		return result;
	}
	public void setResult(SupervisorResult result) {
		this.result = result;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getSupervisorDate() {
		return supervisorDate;
	}

	public void setSupervisorDate(LocalDateTime supervisorDate) {
		this.supervisorDate = supervisorDate;
	}

	@Override
	public String toString() {
		return "BatchState [idBatch=" + idBatch + ", " + (createDate != null ? "createDate=" + createDate + ", " : "")
				+ (supervisorDate != null ? "supervisorDate=" + supervisorDate + ", " : "")
				+ (result != null ? "result=" + result : "") + "]";
	}
	
	
	
	
}
