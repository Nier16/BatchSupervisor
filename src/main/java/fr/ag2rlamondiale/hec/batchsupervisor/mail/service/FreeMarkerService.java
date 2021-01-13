package fr.ag2rlamondiale.hec.batchsupervisor.mail.service;

import fr.ag2rlamondiale.hec.batchsupervisor.mail.model.TemplatesNames;

import java.util.Map;

public interface FreeMarkerService {
    String processTemplate(TemplatesNames templatesNames, Map<String, Object> templateModel) throws Exception;
    void processToFile(TemplatesNames templatesNames, Map<String, Object> templateModel, String fileName) throws Exception;
}
