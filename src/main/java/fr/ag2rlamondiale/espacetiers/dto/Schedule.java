package fr.ag2rlamondiale.espacetiers.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class Schedule {
	private int planID;
	private int batchID;
	private String batchName;
	private Boolean active;
	private List<Integer> years;
	private List<Integer> months;
	private List<Integer> weeks;
	private List<Integer> weekDays;
	private List<Integer> monthDays;
	private Integer freq;
	private Integer startTime;
	private Integer endTime;
	private String description;
}
