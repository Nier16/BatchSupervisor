package fr.ag2rlamondiale.espacetiers.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.ag2rlamondiale.espacetiers.client.ScheduleClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ag2rlamondiale.espacetiers.model.CustomSchedule;
import fr.ag2rlamondiale.espacetiers.model.Slot;
import fr.ag2rlamondiale.espacetiers.useful.Useful;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
	private static List<CustomSchedule> allSchedules;
	public final static int DELTA = 2;
	private int idBatch;
	private List<CustomSchedule> schedules;
	private List<Slot> slots;
	private int actElement;
	private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

	private final ScheduleClient scheduleClient;

	public ScheduleService(ScheduleClient scheduleClient){
		this.scheduleClient = scheduleClient;
	}

	public void loadAllActiveSchedules(){
		allSchedules = Objects.requireNonNull(scheduleClient
				.getAllActiveSchedules()
				.block())
				.getValues()
				.stream()
				.map(CustomSchedule::new)
				.collect(Collectors.toList());
	}

	public List<Integer> getAllBatchIds() {
		return allSchedules
				.stream()
				.map(CustomSchedule::getBatchID)
				.distinct()
				.collect(Collectors.toList());
	}

	public void load(int idBatch) {
		this.idBatch = idBatch;
		this.actElement = 0;
		this.slots = new ArrayList<>();

		this.schedules = allSchedules
				.stream()
				.filter(p -> p.getBatchID().equals(this.idBatch))
				.collect(Collectors.toList());
	}
	
	public void loadSlots(LocalDateTime start, LocalDateTime end){
		for (CustomSchedule schedule : this.schedules) {
			this.slots.addAll(this.getSlotsForSchedule(schedule, start, end));
		}
		this.slots.sort(Comparator.comparing(Slot::getStart));
	}
	
	public Slot next() {
		if(this.actElement < this.slots.size()) {
			log.error("Plage actuel : " + this.slots.get(actElement).toString());
			return this.slots.get(actElement++);
		}
		log.error("Les plages sont finis");
		return null;
		
	}
	
	private boolean checkDayAvailability(CustomSchedule p, LocalDateTime date) {
		return !(
		/*years	*/			(p.getYears()  != null && !p.getYears().isEmpty()  && !p.getYears().contains(date.getYear())) ||
		/*months*/			(p.getMonths() != null && !p.getMonths().isEmpty() && !p.getMonths().contains(date.getMonth().getValue())) ||
		/*mDays */			(p.getMDays()  != null && !p.getMDays().isEmpty()  && !p.getMDays().contains(date.getDayOfMonth())) ||
		/*weeks */			(p.getWeeks()  != null && !p.getWeeks().isEmpty()  && !p.getWeeks().contains(Useful.getWeeksOfTime(date))) ||
		/*wDays */			(p.getWDays()  != null && !p.getWDays().isEmpty()  && !p.getWDays().contains(date.getDayOfWeek().getValue()))
		);
	}
	
	private List<Slot> getSlotsForScheduleAndDay(CustomSchedule p, LocalDateTime start, LocalDateTime end, LocalDateTime day){
		LocalDateTime pStart = Useful.setTimeFromMinutes(day, p.getStartTime());
		LocalDateTime pEnd = Useful.setTimeFromMinutes(day, p.getEndTime());
		List<Slot> res = new ArrayList<>();
		
		//Si une plage commence aujourd'hui et se fini demain 		
		if(pStart.isAfter(pEnd)) {
			pEnd = pEnd.plusDays(1);
		}
		
		if(Useful.isAfterOrEquals(pStart, end) && Useful.isBeforeOrEquals(pEnd, start)) {
			if(p.getFreq() == null) {
				res.add(new Slot(pStart, pEnd.plusMinutes(DELTA)));
			}else {
				LocalDateTime actStart = getRealFreqStartBeta(pStart, start, p.getFreq());
				while(Useful.isAfterOrEquals(actStart, end) && Useful.isAfterOrEquals(actStart, pEnd)) {
					res.add(getSlotFromStartAndDelta(actStart));
					actStart = actStart.plusMinutes(p.getFreq());
				}
			}
		}
		return res;
	}
	
	private List<Slot> getSlotsForSchedule(CustomSchedule p, LocalDateTime start, LocalDateTime end) {
		LocalDateTime startOfStart = Useful.getMidnightForDay(start);
		LocalDateTime endOfEnd = Useful.getMidnightForDay(end.plusDays(1)).minusNanos(1);
		List<Slot> res = new ArrayList<>();
		LocalDateTime actDay = startOfStart.minusDays(1);
		while(actDay.isBefore(endOfEnd)) {
			if(checkDayAvailability(p, actDay)) {
				res.addAll(getSlotsForScheduleAndDay(p, start, end, actDay));
			}
			actDay = actDay.plusDays(1);
		}
		return res;
	}
	
	private LocalDateTime getRealFreqStartBeta(LocalDateTime startPlage, LocalDateTime start, int each) {
		LocalDateTime res = start;
		if(start.isBefore(startPlage)) {
			res = startPlage;
		}
		while(true) {
			if(res.plusMinutes(each).isAfter(startPlage)) {
				return res;
			}
			res = res.plusMinutes(each);
		}
	}
	
	
	private Slot getSlotFromStartAndDelta(LocalDateTime start) {
		return new Slot(start, start.plusMinutes(DELTA));
	}
}
