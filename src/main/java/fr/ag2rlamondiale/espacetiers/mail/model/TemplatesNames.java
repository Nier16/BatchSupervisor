package fr.ag2rlamondiale.espacetiers.mail.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TemplatesNames {
    XML("XmlRequest.ftl"),
    INCIDENT("HtmlIncident.ftl"),
    SIMPLE_REPORT("HtmlSimpleReport.ftl"),
    ADVANCED_REPORT("HtmlAdvancedReport.ftl");

    private String path;

    @Override
    public String toString() {
        return this.path;
    }
}
