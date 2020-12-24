package fr.ag2rlamondiale.espacetiers.entity;

import java.util.List;

import javax.persistence.Entity;

import fr.ag2rlamondiale.espacetiers.model.Plannification;

@Entity
public class MyPlannification {
	private Long batchID;
	private String batchName;
	private Boolean actif;
	private List<Integer> years;
	private List<Integer> months;
	private List<Integer> weeks;
	private List<Integer> wDays;
	private List<Integer> mDays;
	private Integer each;
	private Integer startTime;
	private Integer endTime;
	private String description;
	
	public MyPlannification() {}
	
	public MyPlannification(Plannification p) {
		
	}
	
	public static MyPlannification fromPlannification(Plannification p) {
		return new MyPlannification(p);
	}
	
	public Long getBatchID() {
		return batchID;
	}
	public void setBatchID(Long batchID) {
		this.batchID = batchID;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public Boolean getActif() {
		return actif;
	}
	public void setActif(Boolean actif) {
		this.actif = actif;
	}
	public List<Integer> getYears() {
		return years;
	}
	public void setYears(List<Integer> years) {
		this.years = years;
	}
	public List<Integer> getMonths() {
		return months;
	}
	public void setMonths(List<Integer> months) {
		this.months = months;
	}
	public List<Integer> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<Integer> weeks) {
		this.weeks = weeks;
	}
	public List<Integer> getwDays() {
		return wDays;
	}
	public void setwDays(List<Integer> wDays) {
		this.wDays = wDays;
	}
	public List<Integer> getmDays() {
		return mDays;
	}
	public void setmDays(List<Integer> mDays) {
		this.mDays = mDays;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getEach() {
		return each;
	}

	public void setEach(Integer each) {
		this.each = each;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
}
