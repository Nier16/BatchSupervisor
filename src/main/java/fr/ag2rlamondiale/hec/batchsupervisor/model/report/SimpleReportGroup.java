package fr.ag2rlamondiale.hec.batchsupervisor.model.report;

import fr.ag2rlamondiale.hec.batchsupervisor.model.SupervisorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleReportGroup{
    private boolean ok = true;
    private List<SimpleReportLine> reportLines = new ArrayList<>();

    public void add(SimpleReportLine reportLine){
        this.ok &= reportLine.getResult() == SupervisorResult.OK;
        this.reportLines.add(reportLine);
    }

    public String getResult(){
        return ok ? "OK" : "KO";
    }
}
