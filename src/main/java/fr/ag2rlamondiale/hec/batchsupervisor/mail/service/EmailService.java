package fr.ag2rlamondiale.hec.batchsupervisor.mail.service;

import fr.ag2rlamondiale.hec.batchsupervisor.mail.model.TemplatesNames;

public interface EmailService {
    void sendEmail(String subject, TemplatesNames templateName, Object model) throws Exception;
}
