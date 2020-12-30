package fr.ag2rlamondiale.espacetiers.mail.service.impl;

import com.alm.esb.service.gesteditique_1.creerdemcom_1.CreerDemComType;
import com.alm.esb.service.gesteditique_1.creerdemcom_1.FluxXMLType;
import fr.ag2rlamondiale.espacetiers.mail.model.TemplatesNames;
import fr.ag2rlamondiale.espacetiers.mail.model.XmlConfiguration;
import fr.ag2rlamondiale.espacetiers.mail.service.EmailService;
import fr.ag2rlamondiale.espacetiers.mail.service.FreeMarkerService;
import fr.ag2rlamondiale.espacetiers.mail.webservice.service.EdetiqueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private static final String XML_TEMPLATE_CONFIGURATION = "configuration";
    private static final String XML_TEMPLATE_HTML_CONTENT = "htmlContent";

    private final EdetiqueService edetiqueService;
    private final CreerDemComType creerDemComType;
    private final XmlConfiguration xmlConfiguration;
    private final FreeMarkerService freeMarkerService;


    @Override
    public void sendEmail(String subject) throws Exception {
        this.addXmlToRequest(subject);
        edetiqueService.sendRequest(creerDemComType);
    }

    private void addXmlToRequest(String subject) throws Exception{
        xmlConfiguration.setSubject(subject);
        String xml = freeMarkerService.processTemplate(TemplatesNames.XML, createTemplateModel());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        FluxXMLType fluxXMLType = new FluxXMLType();
        fluxXMLType.getAny().add(document.getDocumentElement());
        creerDemComType.setFormulaire(fluxXMLType);
    }

    private Map<String, Object> createTemplateModel(){
        Map<String, Object> res = new HashMap<>();
        res.put(XML_TEMPLATE_CONFIGURATION, xmlConfiguration);
        res.put(XML_TEMPLATE_HTML_CONTENT, "");
        return res;
    }

}
