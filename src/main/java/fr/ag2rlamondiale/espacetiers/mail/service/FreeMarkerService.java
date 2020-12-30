package fr.ag2rlamondiale.espacetiers.mail.service;

import fr.ag2rlamondiale.espacetiers.mail.model.TemplatesNames;

import java.util.Map;

public interface FreeMarkerService {
    String processTemplate(TemplatesNames templatesNames, Map<String, Object> templateModel);
}
