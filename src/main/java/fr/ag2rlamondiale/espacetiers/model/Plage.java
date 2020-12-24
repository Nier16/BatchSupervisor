package fr.ag2rlamondiale.espacetiers.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import fr.ag2rlamondiale.espacetiers.XibApplication;
import fr.ag2rlamondiale.espacetiers.service.SupervisorService;

public class Plage {
	private LocalDateTime start;
	private LocalDateTime end;
	
	public Plage() {
		
	}
	
	public Plage(LocalDateTime start, LocalDateTime end) {
		this.start = start;
		this.end = end;
	}
	
	public boolean isActif(LocalDateTime time) {
		return time.isBefore(this.end);
	}
	
	public boolean isActif() {
		return this.isActif(SupervisorService.startTime);
	}
	
	public boolean isTimeIn(LocalDateTime time) {
		return (time.isEqual(this.end) || time.isBefore(this.end)) && (time.isEqual(this.start) || time.isAfter(this.start));
	}
	
	public boolean isTimeBefore(LocalDateTime time) {
		return time.isBefore(this.start);
	}
	
	public boolean isTimeAfter(LocalDateTime time) {
		return time.isAfter(this.end);
	}
	
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	public LocalDateTime getStart() {
		return start;
	}
	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	@Override
	public String toString() {
		return "Plage [" + (start != null ? "start=" + start + ", " : "") + (end != null ? "end=" + end : "") + "]";
	}
	
	
}
