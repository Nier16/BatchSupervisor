package fr.ag2rlamondiale.espacetiers.model.report;

import fr.ag2rlamondiale.espacetiers.model.SupervisorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleReportLine extends ReportLine {
    private SupervisorResult result;
}
