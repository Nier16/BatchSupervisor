package fr.ag2rlamondiale.hec.batchsupervisor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleReportGroup {
    private boolean ok;
    private List<SimpleReportLine> reportLines;

    public String getResult() {
        return ok ? "OK" : "KO";
    }
}
