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
public class Report {
    private Map<String, SimpleReportGroup> reports = new HashMap<>();
    private Slot period = new Slot();
    private ReportType type;

    public void addReportLine(String groupName, SimpleReportLine reportLine){
        if(!this.reports.containsKey(groupName)){
            this.reports.put(groupName, new SimpleReportGroup());
        }
        this.reports.get(groupName).add(reportLine);
    }
}
