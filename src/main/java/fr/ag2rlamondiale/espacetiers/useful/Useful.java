package fr.ag2rlamondiale.espacetiers.useful;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final public class Useful {
	
	static public LocalDateTime getTodayWithTime(int hours, int minutes) {
		return LocalDateTime.now().withHour(hours).withMinute(minutes).withSecond(0).withNano(0);
	}

	static public boolean isAfterOrEquals(LocalDateTime t1, LocalDateTime t2) {
		return t1.isEqual(t2) || t2.isAfter(t1);
	}
	
	static public boolean isBeforeOrEquals(LocalDateTime t1, LocalDateTime t2) {
		return t1.isEqual(t2) || t2.isBefore(t1);
	}

	static public LocalDateTime getMidnightForDay(LocalDateTime day){
		return day.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	static public LocalDateTime setTimeFromMinutes(LocalDateTime day, int minutes){
		return day.withMinute(minutes % 60).withHour(minutes / 60).withSecond(0).withNano(0);
	}

	static public int getWeeksOfTime(LocalDateTime date) {
		return date.get(WeekFields.ISO.weekOfMonth());
	}

	static public List<Integer> extractIntegersFromString(String text, String separator) {
		if(text != null ) {
			return Arrays
					.stream(text.split(separator))
					.map(Integer::parseInt)
					.collect(Collectors.toList());
		}
		return null;
	}
}
