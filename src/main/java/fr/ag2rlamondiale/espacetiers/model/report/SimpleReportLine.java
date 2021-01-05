package fr.ag2rlamondiale.espacetiers.model.report;

import fr.ag2rlamondiale.espacetiers.model.Slot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleReportLine{
    private String batchName;
    private Slot lastSlot;
    private Slot nextSlot;
    private String information;
    private boolean ok;

    public SimpleReportLine(boolean ok){
        this.ok = ok;
    }
}
