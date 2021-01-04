package fr.ag2rlamondiale.espacetiers.mail.webservice.service;

import com.alm.esb.service.gesteditique_1.CreerDemCom1Fault;
import com.alm.esb.service.gesteditique_1.creerdemcom_1.CreerDemComResponseType;
import com.alm.esb.service.gesteditique_1.creerdemcom_1.CreerDemComType;
import com.alm.esb.service.gesteditique_1.creerdemcom_1.FluxXMLType;
import fr.ag2rlamondiale.espacetiers.mail.model.TemplatesNames;
import fr.ag2rlamondiale.espacetiers.mail.service.FreeMarkerService;
import fr.ag2rlamondiale.espacetiers.mail.webservice.factory.WebServiceFactory;
import fr.ag2rlamondiale.espacetiers.mail.webservice.model.XmlConfiguration;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Collections;

@Service
@AllArgsConstructor
public class EdetiqueService {
    private static final String XML_TEMPLATE_CONFIGURATION = "configuration";
    private static final Logger log = LoggerFactory.getLogger(EdetiqueService.class);

    private final FreeMarkerService freeMarkerService;
    private final WebServiceFactory webServiceFactory;
    private final CreerDemComType creerDemComType;
    private final XmlConfiguration xmlConfiguration;

    public void sendRequest(String subject, String htmlContent) throws Exception{
        this.addXmlToRequest(subject, htmlContent);
        CreerDemComResponseType response = this.sendRequest();
        this.proceedResponse(response);
    }

    private void proceedResponse(CreerDemComResponseType response) throws Exception{
        if (null != response.getCreerDemComFunc()){
            BigInteger responseCode = response.getCreerDemComFunc().getCodeRetour();
            if (responseCode.intValue() == 1) {
                log.info("Mail envoyé avec succes");
            }else{
                log.error("Le code renvoyé par le server n'est pas bon, vous pouvez verifier avec le numero de la demande : "
                        + response.getCreerDemComFunc().getNumeroUnique());
                throw new Exception();
            }
        }else{
            log.error("Erreur lors de la reception de la reponse du serveur, elle est a null");
            throw new Exception();
        }
    }

    private CreerDemComResponseType sendRequest() throws Exception {
        try {
            return webServiceFactory
                    .getWebServiceCreerDemCom()
                    .creerDemCom1Op(creerDemComType);
        } catch (CreerDemCom1Fault e) {
            log.error("Erreur lors de l'envoi de la requette SOAP pour la creation de l'email : " + e.getMessage());
            throw e;
        }
    }

    private void addXmlToRequest(String subject, String htmlContent) throws Exception {
        xmlConfiguration.setSubject(subject);
        xmlConfiguration.setHtmlContent(htmlContent);
        String xml = freeMarkerService.
                processTemplate(TemplatesNames.XML, Collections.singletonMap(XML_TEMPLATE_CONFIGURATION, xmlConfiguration));
        creerDemComType.setFormulaire(this.getFluxXmlTypeFromString(xml));
    }

    private FluxXMLType getFluxXmlTypeFromString(String xml) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            FluxXMLType fluxXMLType = new FluxXMLType();
            fluxXMLType.getAny().add(document.getDocumentElement());
            return fluxXMLType;
        }catch (ParserConfigurationException | SAXException | IOException e){
            log.error("Erreur lors de la transformation du XML en FluxXmlType : " + e.getMessage());
            throw e;
        }
    }

}
