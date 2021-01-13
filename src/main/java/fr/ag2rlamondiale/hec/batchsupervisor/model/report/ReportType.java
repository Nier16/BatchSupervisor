package fr.ag2rlamondiale.hec.batchsupervisor.model.report;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReportType {
    INCIDENT("d'Incident"),
    SIMPLE_REPORT("Rapport Quotidien");

    private String text;

    public String toString(){
        return this.text;
    }
}
