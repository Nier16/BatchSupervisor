package fr.ag2rlamondiale.espacetiers.model.report;

import fr.ag2rlamondiale.espacetiers.model.report.ReportLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedReportLine extends ReportLine {
    private int ok;
    private int ko;
    private int launchKo;
    private int proceedKo;
    private int outOfSlotKo;
}
