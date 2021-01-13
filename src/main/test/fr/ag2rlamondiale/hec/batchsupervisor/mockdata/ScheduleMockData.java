package fr.ag2rlamondiale.hec.batchsupervisor.mockdata;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.Schedule;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.ScheduleListDto;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class ScheduleMockData {
    public String idBatch1 = "1";
    public String idBatch2 = "2";

    public Schedule s1 = new Schedule(1, idBatch1, 15, 480, 660);
    public Schedule s2 = new Schedule(2, idBatch2, 20, 0, 360);

    public List<String> BatchesId = Arrays.asList(idBatch1, idBatch2);

    public List<Schedule> schedulesList = Arrays.asList(s1,s2);

    public Mono<ScheduleListDto> schedulesMono = Mono.just(new ScheduleListDto(schedulesList));
}
