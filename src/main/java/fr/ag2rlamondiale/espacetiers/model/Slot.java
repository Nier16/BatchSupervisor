package fr.ag2rlamondiale.espacetiers.model;

import java.time.LocalDateTime;

import fr.ag2rlamondiale.espacetiers.service.process.SupervisorService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Slot {
	private LocalDateTime start;
	private LocalDateTime end;

	public boolean isActive(LocalDateTime time) {
		return time.isAfter(this.start) && time.isBefore(this.end);
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
		}else{
			return "fini le " + end.toString();
		}
	}

}
