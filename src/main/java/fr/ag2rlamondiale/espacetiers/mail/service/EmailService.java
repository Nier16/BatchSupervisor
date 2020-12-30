package fr.ag2rlamondiale.espacetiers.mail.service;

import com.alm.esb.service.gesteditique_1.CreerDemCom1Fault;

public interface EmailService {
    void sendEmail(String subject) throws Exception;
}
