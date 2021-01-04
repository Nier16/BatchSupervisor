package fr.ag2rlamondiale.espacetiers.service.process;

import java.time.LocalDateTime;
import java.util.*;

import fr.ag2rlamondiale.espacetiers.model.ScheduleInformation;
import fr.ag2rlamondiale.espacetiers.service.data.ScheduleDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import fr.ag2rlamondiale.espacetiers.dto.Schedule;
import fr.ag2rlamondiale.espacetiers.model.Slot;
import fr.ag2rlamondiale.espacetiers.useful.Useful;


@Service
public class ScheduleService {
	private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);
	private final static int DELTA = 2;

	private final ScheduleDataService scheduleDataService;


	private List<Schedule> schedules;
	private List<Slot> slots;
	private int actElement;

	public ScheduleService(ScheduleDataService scheduleDataService){
		this.scheduleDataService = scheduleDataService;
	}

	public ScheduleService load(int idBatch) {
		this.actElement = 0;
		this.slots = new ArrayList<>();
		this.schedules = scheduleDataService.getBatchSchedules(idBatch);
		return this;
	}
	
	public void loadSlots(LocalDateTime start, LocalDateTime end){
		for (Schedule schedule : this.schedules) {
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
	// prev, next
	// tempAct
	// prev.end < tempAct

	public ScheduleInformation getScheduleInformation(int batchId){
		 this.load(batchId);
		 ScheduleInformation scheduleInformation = new ScheduleInformation();
		 StringBuilder descriptionBuilder = new StringBuilder();
		 Slot slot;
		 LocalDateTime time = SupervisorService.startTime;
		 for(Schedule schedule : this.schedules){
		 	descriptionBuilder.append(schedule.getDescription()).append("\n");

		 	// ACTIVE
		 	slot = getActiveSlotForSchedule(schedule, time);
		 	if(slot != null){
		 		scheduleInformation.setActive(slot);
			}

		 	// NEXT
		 	slot = getNextSlotForSchedule(schedule, time);
		 	if(slot != null &&
					(scheduleInformation.getNext() == null
							|| scheduleInformation.getNext().getStart().isAfter(slot.getStart()))){
		 		scheduleInformation.setNext(slot);
			}

			 // PREVIOUS
			 slot = getPreviousSlotForSchedule(schedule, time);
			 if(slot != null &&
					 (scheduleInformation.getPrevious() == null
							 || scheduleInformation.getPrevious().getEnd().isBefore(slot.getEnd()))){
				 scheduleInformation.setPrevious(slot);
			 }
		 }
		 scheduleInformation.setDescription(descriptionBuilder.toString());
		 if(this.schedules != null && this.schedules.size() != 0){
		 	scheduleInformation.setBatchName(this.schedules.get(0).getBatchName());
		 	scheduleInformation.setGroupName(this.schedules.get(0).getGroupName());
		 }
		return scheduleInformation;
	}

	public Slot getActiveSlotForSchedule(Schedule schedule, LocalDateTime time){
		List<Slot> slots = getSlotsForScheduleAndDay(schedule, time, time, time);
		if(slots.size() != 0){
			return slots.get(0);
		}
		return null;
	}

	public Slot getPreviousSlotForSchedule(Schedule schedule, LocalDateTime time){
		LocalDateTime start = Useful.getMidnightForDay(time);
		LocalDateTime end = time;
		List<Slot> slots;
		while (true){
			slots = getSlotsForScheduleAndDay(schedule, start, end, start);
			if(!slots.isEmpty()){
				int size = slots.size();
				if(!slots.get(size - 1).isActive(time)){
					return slots.get(size - 1);
				}else if(size >= 2){
					return slots.get(size -2);
				}
			}
			start = start.minusDays(1);
			end = start.plusDays(1).minusNanos(1);
		}
	}

	public Slot getNextSlotForSchedule(Schedule schedule, LocalDateTime time){
		LocalDateTime start = time;
		LocalDateTime end = Useful.getMidnightForDay(time).plusDays(1).minusNanos(1);
		List<Slot> slots;
		while (true){
			slots = getSlotsForScheduleAndDay(schedule, start, end, start);
			if(!slots.isEmpty()){
				if(slots.get(0).getStart().isBefore(start)){
					slots.remove(0);
				}
				if(!slots.get(0).isActive(time)){
					return slots.get(0);
				}else if(slots.size() >= 2){
					return slots.get(1);
				}
			}
			end = end.plusDays(1);
			start = Useful.getMidnightForDay(end);
		}
	}



	private List<Slot> getSlotsForSchedule(Schedule p, LocalDateTime start, LocalDateTime end) {
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

	private boolean checkDayAvailability(Schedule p, LocalDateTime date) {
		return !(
				/*years	*/			(p.getYears()  != null && !p.getYears().isEmpty()  && !p.getYears().contains(date.getYear())) ||
				/*months*/			(p.getMonths() != null && !p.getMonths().isEmpty() && !p.getMonths().contains(date.getMonth().getValue())) ||
				/*mDays */			(p.getMonthDays()  != null && !p.getMonthDays().isEmpty()  && !p.getMonthDays().contains(date.getDayOfMonth())) ||
				/*weeks */			(p.getWeeks()  != null && !p.getWeeks().isEmpty()  && !p.getWeeks().contains(Useful.getWeeksOfTime(date))) ||
				/*wDays */			(p.getWeekDays()  != null && !p.getWeekDays().isEmpty()  && !p.getWeekDays().contains(date.getDayOfWeek().getValue()))
		);
	}

	private List<Slot> getSlotsForScheduleAndDay(Schedule p, LocalDateTime start, LocalDateTime end, LocalDateTime day){
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
				LocalDateTime actStart = getRealFreqStart(pStart, start, p.getFreq());
				while(Useful.isAfterOrEquals(actStart, end) && Useful.isAfterOrEquals(actStart, pEnd)) {
					res.add(getSlotFromStartAndDelta(actStart));
					actStart = actStart.plusMinutes(p.getFreq());
				}
			}
		}
		return res;
	}
	
	private LocalDateTime getRealFreqStart(LocalDateTime slotStart, LocalDateTime start, int freq) {
		LocalDateTime res = slotStart;
		while(!res.plusMinutes(freq).isAfter(start)) {
			res = res.plusMinutes(freq);
		}
		return res;
	}

	private Slot getSlotFromStartAndDelta(LocalDateTime start) {
		return new Slot(start, start.plusMinutes(DELTA));
	}
}
