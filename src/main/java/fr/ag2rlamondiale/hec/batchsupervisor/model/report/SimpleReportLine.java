package fr.ag2rlamondiale.hec.batchsupervisor.model.report;

import fr.ag2rlamondiale.hec.batchsupervisor.model.Slot;
import fr.ag2rlamondiale.hec.batchsupervisor.model.SupervisorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SimpleReportLine{
    private String batchName;
    private LocalDateTime lastExec;
    private Slot nextSlot;
    private List<String> informations;
    private SupervisorResult result;

    public SimpleReportLine(SupervisorResult result){
        this.result = result;
    }

}
