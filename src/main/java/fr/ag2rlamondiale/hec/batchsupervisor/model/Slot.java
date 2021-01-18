package fr.ag2rlamondiale.hec.batchsupervisor.model;

import java.time.LocalDateTime;

import fr.ag2rlamondiale.hec.batchsupervisor.service.process.SupervisorService;
import fr.ag2rlamondiale.hec.batchsupervisor.useful.Useful;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
	private LocalDateTime start;
	private LocalDateTime end;

	public Slot(LocalDateTime start, int delta){
		this.start = start;
		this.end = start.plusSeconds(delta);
	}

	public Slot(LocalDateTime start, LocalDateTime end, int delta){
		this.start = start;
		this.end = end.plusSeconds(delta);
	}

	public boolean isActive(LocalDateTime time) {
		return Useful.isAfterOrEquals(this.start, time) && Useful.isBeforeOrEquals(this.end, time);
	}

	public boolean isActive() {
		return this.isActive(SupervisorService.startTime);
	}

	public boolean isTimeBefore(LocalDateTime time) {
		return time.isBefore(this.start);
	}

	public boolean isTimeAfter(LocalDateTime time) {
		return time.isAfter(this.end);
	}

	public String toString(){
		if(start != null){
			if(end != null){
				if(start.getDayOfYear() == end.getDayOfYear()){
					return "le " + start.toLocalDate().toString() + " entre " + start.toLocalTime().toString() + " et " + end.toLocalTime().toString();
				}else{
					return "Entre le " + start.toString() + " et le " + end.toString();
				}
			}else{
				return "Le " + start.toString();
			}
		}else if(end != null){
			return "fini le " + end.toString();
		}else {
			return "la palge n'est pas set";
		}
	}

}
