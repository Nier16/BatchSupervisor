package fr.ag2rlamondiale.espacetiers.mail.service.impl;

import fr.ag2rlamondiale.espacetiers.mail.model.TemplatesNames;
import fr.ag2rlamondiale.espacetiers.mail.service.EmailService;
import fr.ag2rlamondiale.espacetiers.mail.service.FreeMarkerService;
import fr.ag2rlamondiale.espacetiers.mail.webservice.service.EdetiqueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private static final String HTML_MODEL = "data";

    private final EdetiqueService edetiqueService;
    private final FreeMarkerService freeMarkerService;


    @Override
    public void sendEmail(String subject, TemplatesNames templateName, Object model) throws Exception {
        edetiqueService.sendRequest(subject,
                freeMarkerService.processTemplate(templateName, Collections.singletonMap(HTML_MODEL, model)));
    }


}

// Advanced
// advanced
// sumury.weekly.dayOfWeek=
//
// monthly
// yearly
// OK KO KO_PLAGE KO_TRAITEMENT