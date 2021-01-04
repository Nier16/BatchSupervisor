package fr.ag2rlamondiale.espacetiers.model.report;

import fr.ag2rlamondiale.espacetiers.model.Slot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report<V extends ReportGroup> {
    private Map<String, V> reports = new HashMap<>();
    private Slot period = new Slot();
    private ReportType type;
    private AdvancedReportTypes advancedReportTypes;

    public void addReportLine(String groupName, ReportLine reportLine){
        if(!this.reports.containsKey(groupName)){
            if(this.type == ReportType.INCIDENT || this.type == ReportType.SIMPLE_REPORT)
                this.reports.put(groupName, new SimpleReportGroup());
        }
        this.reports.get(groupName).add(reportLine);
    }
}
