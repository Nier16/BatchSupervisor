package fr.ag2rlamondiale.hec.batchsupervisor.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class Schedule {
	private int planID;
	private String batchID;
	private String batchName;
	private String groupName;
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

	public Schedule(int planID, String batchID){
		this.planID = planID;
		this.batchID = batchID;
	}

	public Schedule(int planID, String batchID, Integer freq, Integer startTime, Integer endTime){
		this(planID, batchID);
		this.freq = freq;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
