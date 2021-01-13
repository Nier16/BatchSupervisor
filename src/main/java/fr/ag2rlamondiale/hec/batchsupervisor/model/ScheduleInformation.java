package fr.ag2rlamondiale.hec.batchsupervisor.model;

import fr.ag2rlamondiale.hec.batchsupervisor.model.report.SimpleReportLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInformation {
    private Slot active;
    private Slot next;
    private String batchName;
    private String groupName;
    private List<String> description = new ArrayList<>();

    public SimpleReportLine toReportLine(SimpleReportLine reportLine, LocalDateTime lastBatchExec){
        reportLine.setBatchName(batchName);
        reportLine.setInformations(description);
        reportLine.setLastExec(lastBatchExec);
        if(this.active != null){
            if(this.active.isActive(lastBatchExec)){
                reportLine.setNextSlot(this.next);
            }else{
                reportLine.setNextSlot(this.active);
            }
        }else{
            reportLine.setNextSlot(this.next);
        }
        return reportLine;
    }
}
