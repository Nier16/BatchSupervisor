package fr.ag2rlamondiale.hec.batchsupervisor.mail.service.impl;

import fr.ag2rlamondiale.hec.batchsupervisor.mail.model.TemplatesNames;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.service.EmailService;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.service.FreeMarkerService;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.webservice.service.EdetiqueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Collections;

@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private static final String HTML_MODEL = "data";

    private final EdetiqueService edetiqueService;
    private final FreeMarkerService freeMarkerService;


    @Override
    public void sendEmail(String subject, TemplatesNames templateName, Object model) throws Exception {
        String htmlContent = HtmlUtils.htmlEscape(freeMarkerService.processTemplate(templateName, Collections.singletonMap(HTML_MODEL, model)), "UTF-8");
        htmlContent = htmlContent.replace("\"", "&quot;");
        System.out.println("Html Content : " + htmlContent);
        edetiqueService.sendRequest(subject, htmlContent);
    }


}

// Advanced
// advanced
// sumury.weekly.dayOfWeek=
//
// monthly
// yearly
// OK KO KO_PLAGE KO_TRAITEMENT