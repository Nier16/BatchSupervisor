package fr.ag2rlamondiale.espacetiers.model;

import fr.ag2rlamondiale.espacetiers.model.report.SimpleReportLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInformation {
    private Slot previous;
    private Slot active;
    private Slot next;
    private String batchName;
    private String groupName;
    private String description;

    public SimpleReportLine toReportLine(SimpleReportLine reportLine, LocalDateTime lastBatchExec){
        reportLine.setBatchName(batchName);
        reportLine.setInformation(description);
        if(this.active != null){
            if(this.active.isActive(lastBatchExec)){
                reportLine.setLastSlot(this.active);
                reportLine.setNextSlot(this.next);
            }else{
                reportLine.setLastSlot(this.previous);
                reportLine.setNextSlot(this.active);
            }
        }else{
            reportLine.setLastSlot(this.previous);
            reportLine.setNextSlot(this.next);
        }
        return reportLine;
    }
}
