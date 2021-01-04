package fr.ag2rlamondiale.espacetiers.model.report;

import fr.ag2rlamondiale.espacetiers.model.Slot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportLine {
    private String batchName;
    private Slot lastSlot;
    private Slot nextSlot;
    private String information;
}
