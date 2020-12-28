package fr.ag2rlamondiale.espacetiers.model;

import java.util.List;

import fr.ag2rlamondiale.espacetiers.dto.Schedule;
import fr.ag2rlamondiale.espacetiers.useful.Useful;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomSchedule {
	private final static String SEPARATOR = ",";

	private Integer batchID;
	private String batchName;
	private Boolean active;
	private List<Integer> years;
	private List<Integer> months;
	private List<Integer> weeks;
	private List<Integer> wDays;
	private List<Integer> mDays;
	private Integer freq;
	private Integer startTime;
	private Integer endTime;
	private String description;

	public CustomSchedule(Schedule p) {
		this.batchID = p.getBatchID();
		this.batchName = p.getBatchName();
		this.active = p.getActive();
		this.freq = p.getFreq();
		this.startTime = p.getStartTime();
		this.endTime = p.getEndTime();
		this.description = p.getDescription();
		this.years = Useful.extractIntegersFromString(p.getYears(), SEPARATOR);
		this.months = Useful.extractIntegersFromString(p.getMonths(), SEPARATOR);
		this.weeks = Useful.extractIntegersFromString(p.getWeeks(), SEPARATOR);
		this.mDays = Useful.extractIntegersFromString(p.getMDays(), SEPARATOR);
		this.wDays = Useful.extractIntegersFromString(p.getWDays(), SEPARATOR);
	}

	public static CustomSchedule fromSchedule(Schedule p) {
		return new CustomSchedule(p);
	}
	

}
