package fr.ag2rlamondiale.espacetiers.useful;

import java.time.LocalDateTime;

final public class Useful {
	
	static public LocalDateTime getTodayWithTime(int hours, int minutes) {
		return LocalDateTime.now().withHour(hours).withMinute(minutes).withSecond(0).withNano(0);
	}
	
	static public LocalDateTime getNowWithoutSecondes() {
		return LocalDateTime.now().withSecond(0).withNano(0);
	}
	
	static public boolean isAfterOrEquals(LocalDateTime t1, LocalDateTime t2) {
		return t1.isEqual(t2) || t2.isAfter(t1);
	}
	
	static public boolean isBeforeOrEquals(LocalDateTime t1, LocalDateTime t2) {
		return t1.isEqual(t2) || t2.isBefore(t1);
	}
	
	static public boolean isToday(LocalDateTime t) {
		LocalDateTime todayStart = getTodayMidnight();
		return isAfterOrEquals(todayStart, t) && t.isBefore(todayStart.plusDays(1));
	}
	
	static public LocalDateTime getTodayMidnight() {
		return getTodayWithTime(0, 0);
	}
}
