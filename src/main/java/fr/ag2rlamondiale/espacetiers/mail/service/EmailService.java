package fr.ag2rlamondiale.espacetiers.mail.service;

import fr.ag2rlamondiale.espacetiers.mail.model.TemplatesNames;

public interface EmailService {
    void sendEmail(String subject, TemplatesNames templateName, Object model) throws Exception;
}
