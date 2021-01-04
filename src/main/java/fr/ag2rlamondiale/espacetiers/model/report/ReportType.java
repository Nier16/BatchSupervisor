package fr.ag2rlamondiale.espacetiers.model.report;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReportType {
    INCIDENT("d'Incident"),
    SIMPLE_REPORT("Rapport Quotidien"),
    ADVANCED_REPORT("Rapport avancé");

    private String text;

    public String toString(){
        return this.text;
    }
}
