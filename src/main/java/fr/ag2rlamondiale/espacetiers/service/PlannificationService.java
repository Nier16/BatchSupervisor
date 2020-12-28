package fr.ag2rlamondiale.espacetiers.service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.ag2rlamondiale.espacetiers.client.PlanificationClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ag2rlamondiale.espacetiers.model.MyPlanification;
import fr.ag2rlamondiale.espacetiers.model.Plage;
import fr.ag2rlamondiale.espacetiers.useful.Useful;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PlannificationService {
	private static List<MyPlanification> allPlanification;
	public final static int DELTA = 2;
	private Integer idBatch;
	private List<MyPlanification> plannifications;
	private List<Plage> plages;
	private int actElement;
	private static final Logger log = LoggerFactory.getLogger(PlannificationService.class);

	public static PlanificationClient planificationClient;
	
	public PlannificationService(Integer idBatch) {
		this.idBatch = idBatch;
		this.actElement = 0;
		this.plages = new ArrayList<Plage>();
	}
	
	public void charger() {
		this.plannifications = allPlanification
				.stream()
				.filter(p -> p.getBatchID().equals(this.idBatch))
				.collect(Collectors.toList());
	}
	
	public static List<Integer> getAllBatchIds() {
		allPlanification = planificationClient
				.getAllActivePlanifications()
				.block()
				.getValues()
				.stream()
				.map(p -> new MyPlanification(p))
				.collect(Collectors.toList());

		Set<Integer> batchIds = allPlanification
				.stream()
				.map(p -> p.getBatchID())
				.collect(Collectors.toSet());

		return batchIds
				.stream()
				.collect(Collectors.toList());
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
		this.plages.sort(Comparator.comparing(Plage::getStart));
	}
	
	public Plage next() {
		if(this.actElement < this.plages.size()) {
			log.error("Plage actuel : " + this.plages.get(actElement).toString());
			return this.plages.get(actElement++);
		}
		log.error("Les plages sont finis");
		return null;
		
	}
	
	private boolean checkDayAviability(MyPlanification p, LocalDateTime date) {
		return !(
		/*years	*/			(p.getYears()  != null && !p.getYears().isEmpty()  && !p.getYears().contains(date.getYear())) ||
		/*months*/			(p.getMonths() != null && !p.getMonths().isEmpty() && !p.getMonths().contains(date.getMonth().getValue())) ||
		/*mDays */			(p.getMDays()  != null && !p.getMDays().isEmpty()  && !p.getMDays().contains(date.getDayOfMonth())) ||
		/*weeks */			(p.getWeeks()  != null && !p.getWeeks().isEmpty()  && !p.getWeeks().contains(getWeeksOfTime(date))) ||
		/*wDays */			(p.getWDays()  != null && !p.getWDays().isEmpty()  && !p.getWDays().contains(date.getDayOfWeek().getValue()))
		);
	}
	
	private List<Plage> extractPlagesForDay(MyPlanification p, LocalDateTime start, LocalDateTime end, LocalDateTime day){
		LocalDateTime pStart = Useful.setTimeFromMinutes(day, p.getStartTime());
		LocalDateTime pEnd = Useful.setTimeFromMinutes(day, p.getEndTime());
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
	
	private List<Plage> getPlageBeta(MyPlanification p, LocalDateTime start, LocalDateTime end) {
		LocalDateTime startOfStart = Useful.getMidnightForDay(start);
		LocalDateTime endOfEnd = Useful.getMidnightForDay(end.plusDays(1)).minusNanos(1);
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
			res = res.plusMinutes(each);
		}
	}
	
	
	private Plage getPlageFromStartAndDelta(LocalDateTime start) {
		return new Plage(start, start.plusMinutes(DELTA));
	}
	
	private int getWeeksOfTime(LocalDateTime date) {
		return date.get(WeekFields.ISO.weekOfMonth());
	}
}
