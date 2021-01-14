package fr.ag2rlamondiale.hec.batchsupervisor.mockdata;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.Schedule;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.ScheduleListDto;
import fr.ag2rlamondiale.hec.batchsupervisor.model.ScheduleInformation;
import fr.ag2rlamondiale.hec.batchsupervisor.model.Slot;
import fr.ag2rlamondiale.hec.batchsupervisor.useful.Useful;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ScheduleMockData {
    public String idBatch1 = "1";
    public String idBatch2 = "2";

    public Schedule s1 = new Schedule(1, idBatch1, 15, 480, 120, 660);
    public Schedule s2 = new Schedule(2, idBatch2, 20, 0, 120, 360);

    public LocalDateTime slotStart1 = Useful.getTodayWithTime(10, 0);
    public LocalDateTime slotStart2 = Useful.getTodayWithTime(10, 15);
    public LocalDateTime slotStart3 = Useful.getTodayWithTime(10, 30);

    public Slot slot1 = new Slot(slotStart1, slotStart1.plusSeconds(s1.getDelta()));
    public Slot slot2 = new Slot(slotStart2, slotStart2.plusSeconds(s1.getDelta()));
    public Slot slot3 = new Slot(slotStart3, slotStart3.plusSeconds(s1.getDelta()));

    public ScheduleInformation scheduleInformation = new ScheduleInformation(slot3);

    public List<Slot> slotList = Arrays.asList(slot1, slot2, slot3);

    public List<String> batchesId = Arrays.asList(idBatch1, idBatch2);

    public List<Schedule> schedulesList = Arrays.asList(s1,s2);

    public Mono<ScheduleListDto> schedulesMono = Mono.just(new ScheduleListDto(schedulesList));
}
