package fr.ag2rlamondiale.espacetiers.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class Schedule {
	private int planID;
	private int batchID;
	private String batchName;
	private Boolean active;
	private String years;
	private String months;
	private String weeks;
	private String wDays;
	private String mDays;
	private Integer freq;
	private Integer startTime;
	private Integer endTime;
	private String description;
}
