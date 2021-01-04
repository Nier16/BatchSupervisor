package fr.ag2rlamondiale.espacetiers.model.report;

import java.util.ArrayList;
import java.util.List;

public abstract class ReportGroup<T extends ReportLine> {
    String name;
    List<T> reportLines = new ArrayList<>();

    void add(T reportLine){
        this.reportLines.add(reportLine);
    }
}
