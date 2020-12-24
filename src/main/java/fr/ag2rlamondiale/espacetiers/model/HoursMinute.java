package fr.ag2rlamondiale.espacetiers.model;

public class HoursMinute {
	private Integer hours;
	private Integer minutes;
	
	public HoursMinute() {}
	
	public HoursMinute(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}
	
	public Integer getHours() {
		return hours;
	}
	public void setHours(Integer hours) {
		this.hours = hours;
	}
	public Integer getMinutes() {
		return minutes;
	}
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}
	
	
}
