package fr.ag2rlamondiale.hec.batchsupervisor.mockdata;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchStateListDto;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.Schedule;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.ScheduleListDto;
import fr.ag2rlamondiale.hec.batchsupervisor.model.SupervisorResult;
import fr.ag2rlamondiale.hec.batchsupervisor.useful.Useful;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class StateMockData {
    public String idBatch1 = "1";
    public String idBatch2 = "2";

    public LocalDateTime bs1Create = Useful.getTodayWithTime(10, 0);
    public LocalDateTime bs2Create = Useful.getTodayWithTime(10, 30);
    public LocalDateTime bs3Create = Useful.getTodayWithTime(10, 10);
    public LocalDateTime supervisorDate = Useful.getTodayWithTime(10, 20);
    public SupervisorResult bs1Result = SupervisorResult.OK;
    public SupervisorResult bs3Result = SupervisorResult.LAUNCH_KO;

    public BatchState bs1 = new BatchState(idBatch1, bs1Create, supervisorDate, bs1Result);
    public BatchState bs2 = new BatchState(idBatch1, bs2Create);
    public BatchState bs3 = new BatchState(idBatch2, bs3Create, supervisorDate, bs3Result);

    public List<BatchState> lastList = Arrays.asList(bs2, bs3);
    public List<BatchState> b1List = Arrays.asList(bs1, bs2);
    public List<BatchState> allList = Arrays.asList(bs1, bs2, bs3);

    public Mono<BatchStateListDto> lastMono = Mono.just(new BatchStateListDto(lastList));
    public Mono<BatchStateListDto> b1Mono = Mono.just(new BatchStateListDto(b1List));
    public Mono<BatchStateListDto> allMono = Mono.just(new BatchStateListDto(allList));
}
