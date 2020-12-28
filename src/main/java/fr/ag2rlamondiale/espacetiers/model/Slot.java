package fr.ag2rlamondiale.espacetiers.model;

import java.time.LocalDateTime;

import fr.ag2rlamondiale.espacetiers.service.SupervisorService;
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
		return time.isBefore(this.end);
	}

	public boolean isActive() {
		return this.isActive(SupervisorService.startTime);
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

}
