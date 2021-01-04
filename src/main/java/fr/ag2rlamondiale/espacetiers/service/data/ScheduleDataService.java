package fr.ag2rlamondiale.espacetiers.service.data;

import fr.ag2rlamondiale.espacetiers.client.ScheduleClient;
import fr.ag2rlamondiale.espacetiers.dto.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ScheduleDataService {
    private List<Schedule> allSchedules;
    private final ScheduleClient scheduleClient;

    ScheduleDataService(ScheduleClient scheduleClient){
        this.scheduleClient = scheduleClient;
        this.loadAllActiveSchedules();
    }

    private void loadAllActiveSchedules(){
        allSchedules = Objects.requireNonNull(scheduleClient
                .getAllActiveSchedules()
                .block())
                .getValues();
    }

    public List<Integer> getAllBatchIds() {
        return allSchedules
                .stream()
                .map(Schedule::getBatchID)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Schedule> getBatchSchedules(int batchId){
        return allSchedules
                .stream()
                .filter(p -> p.getBatchID() == batchId)
                .collect(Collectors.toList());
    }
}
