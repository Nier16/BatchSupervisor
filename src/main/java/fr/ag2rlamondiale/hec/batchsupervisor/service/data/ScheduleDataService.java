package fr.ag2rlamondiale.hec.batchsupervisor.service.data;

import fr.ag2rlamondiale.hec.batchsupervisor.client.ScheduleClient;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.Schedule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
public class ScheduleDataService {
    private List<Schedule> allSchedules;
    private final ScheduleClient scheduleClient;

    ScheduleDataService(ScheduleClient scheduleClient){
        this.scheduleClient = scheduleClient;
    }

    public void loadAllActiveSchedules(){
        allSchedules = Objects.requireNonNull(scheduleClient
                .getAllActiveSchedules()
                .block())
                .getValues();
    }

    public List<String> getAllBatchIds() {
        return allSchedules
                .stream()
                .map(Schedule::getBatchID)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Schedule> getBatchSchedules(String batchId){
        return allSchedules
                .stream()
                .filter(p -> p.getBatchID().equals(batchId))
                .collect(Collectors.toList());
    }
}
