package fr.ag2rlamondiale.hec.batchsupervisor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private Map<String, SimpleReportGroup> reports;
    private Slot period;
    private ReportType type;
}
