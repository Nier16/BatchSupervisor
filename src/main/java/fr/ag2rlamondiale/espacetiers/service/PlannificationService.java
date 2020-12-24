package fr.ag2rlamondiale.espacetiers.service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ag2rlamondiale.espacetiers.entity.MyPlannification;
import fr.ag2rlamondiale.espacetiers.model.Plage;
import fr.ag2rlamondiale.espacetiers.useful.Useful;

public class PlannificationService {
	public final static int DELTA = 2;
	private long idBatch;
	private List<MyPlannification> plannifications;
	private List<Plage> plages;
	private int actElement;
	private static final Logger log = LoggerFactory.getLogger(PlannificationService.class);  
	
	public PlannificationService(long idBatch) {
		this.idBatch = idBatch;
		this.actElement = 0;
		this.plages = new ArrayList<Plage>();
	}
	
	public void charger() {
		// get plannification for idBatch && actif
		
		MyPlannification p1 = new MyPlannification();
		p1.setStartTime(480);
		p1.setEndTime(780);
		p1.setEach(20);
		p1.setBatchID(1L);
		
		MyPlannification p3 = new MyPlannification();
		p3.setStartTime(1200);
		p3.setEndTime(360);
		p3.setEach(60);
		p3.setBatchID(1L);
		
		MyPlannification p2 = new MyPlannification();
		p2.setStartTime(0);
		p2.setEndTime(420);
		p2.setBatchID(2L);
		
		if(this.idBatch == 1L)
			this.plannifications = Arrays.asList(p1, p3);
		else
			this.plannifications = Arrays.asList(p2);
	}
	
	public static List<Long> getAllBatchIds() {
		// get all plannification et extrait les id des batch en unique
		return Arrays.asList(1L, 2L);
	}
	
	public Plage getPlageForTime(LocalDateTime time) {
		for(int i = 0; i < this.plages.size(); i++) {
			if(this.plages.get(i).isTimeIn(time)) {
				return this.plages.get(i);
			}
		}
		return null;
	}
	
	public void loadPlages(LocalDateTime start, LocalDateTime end){
		for(int i = 0 ; i < this.plannifications.size() ; i++) {
			this.plages.addAll(this.getPlageBeta(this.plannifications.get(i), start, end));
		}
		this.plages.sort((p1,p2) -> p1.getStart().compareTo(p2.getStart()));
	}
	
	public Plage next() {
		if(this.actElement < this.plages.size()) {
			log.error("Plage actuel : " + this.plages.get(actElement).toString());
			return this.plages.get(actElement++);
		}
		log.error("Les plages sont finis");
		return null;
		
	}
	
	private boolean checkDayAviability(MyPlannification p, LocalDateTime date) {
		// YEARS
		if(p.getYears() != null && !p.getYears().isEmpty() && !p.getYears().contains(date.getYear()))
			return false;
		
		// MONTHS
		if(p.getMonths() != null && !p.getMonths().isEmpty() && !p.getMonths().contains(date.getMonth().getValue()))
			return false;
		
		// DAY OF MONTH
		if(p.getmDays() != null && !p.getmDays().isEmpty()) {
			if(!p.getmDays().contains(date.getDayOfMonth())) {
				return false;
			}
		}else{
			// WEEK OF MONTH
			if(p.getWeeks() != null && !p.getWeeks().isEmpty() && !p.getWeeks().contains(getWeeksOfTime(date))) {
				return false;
			}
			// DAY OF WEEK
			else if(p.getwDays() != null && !p.getwDays().isEmpty() && !p.getwDays().contains(date.getDayOfWeek().getValue())) {
				return false;
			}
		}
		return true;
	}
	
	private List<Plage> extractPlagesForDay(MyPlannification p, LocalDateTime start, LocalDateTime end, LocalDateTime day){
		LocalDateTime pStart = day.withMinute(p.getStartTime() % 60).withHour(p.getStartTime() / 60);
		LocalDateTime pEnd = day.withMinute(p.getEndTime() % 60).withHour(p.getEndTime() / 60);
		List<Plage> res = new ArrayList<Plage>();
		
		//Si une plage commence aujourd'hui et se fini demain 		
		if(pStart.isAfter(pEnd)) {
			pEnd = pEnd.plusDays(1);
		}
		
		if(Useful.isAfterOrEquals(pStart, end) && Useful.isBeforeOrEquals(pEnd, start)) {
			if(p.getEach() == null) {
				res.add(new Plage(pStart, pEnd.plusMinutes(DELTA)));
			}else {
				LocalDateTime actStart = getRealFreqStartBeta(pStart, start, p.getEach());
				while(Useful.isAfterOrEquals(actStart, end) && Useful.isAfterOrEquals(actStart, pEnd)) {
					res.add(getPlageFromStartAndDelta(actStart));
					actStart = actStart.plusMinutes(p.getEach());
				}
			}
		}
		return res;
	}
	
	private List<Plage> getPlageBeta(MyPlannification p, LocalDateTime start, LocalDateTime end) {
		LocalDateTime startOfStart = start.withMinute(0).withHour(0).withSecond(0).withNano(0);
		LocalDateTime endOfEnd = end.plusDays(1).withMinute(0).withHour(0).withSecond(0).withNano(0).minusNanos(1);
		List<Plage> res = new ArrayList<Plage>();
		LocalDateTime actDay = startOfStart.minusDays(1);
		while(actDay.isBefore(endOfEnd)) {
			if(checkDayAviability(p, actDay)) {
				res.addAll(extractPlagesForDay(p, start, end, actDay));
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
			res.plusMinutes(each);
		}
	}
	
	
	private Plage getPlageFromStartAndDelta(LocalDateTime start) {
		return new Plage(start, start.plusMinutes(DELTA));
	}
	
	private int getWeeksOfTime(LocalDateTime date) {
		return date.get(WeekFields.ISO.weekOfMonth());
	}
}
