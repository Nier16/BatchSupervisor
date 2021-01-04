package fr.ag2rlamondiale.espacetiers.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.temporal.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AdvancedReportTypes {

    NONE,
    DAILY("Quotidien", ChronoUnit.DAYS),
    WEEKLY("Abdomadaire", ChronoUnit.WEEKS),
    MONTHLY("Mensuel", ChronoUnit.MONTHS),
    YEARLY("Anuel", ChronoUnit.YEARS);

    private String text;
    private TemporalUnit period;

    public String toString(){
        return this.text;
    }

}
