package fr.ag2rlamondiale.hec.batchsupervisor.mail.webservice.config;

import com.alm.esb.service.gesteditique_1.creerdemcom_1.CreerDemComType;
import com.alm.esb.service.gesteditique_1.creerdemcom_1.DemEditionType;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.webservice.model.XmlConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RequestConfigurer {

    private static final String RECIPIENTS_SEPARATOR = ",";

    @Value("${mail.xml.conf.requestId}")
    private String requestId;

    @Value("${mail.xml.conf.channel}")
    private String channel;

    @Value("${mail.xml.conf.from}")
    private String from;

    @Value("${mail.xml.conf.recipients}")
    private String recipients;

    @Value("${mail.xml.conf.type}")
    private String type;

    @Value("${demande.type.config.refDoc}")
    private String refDoc;

    @Value("${demande.type.config.codeAppliEmet}")
    private String codeAppliEmet;

    @Value("${demande.type.config.codeAppli}")
    private String codeAppli;

    @Value("${demande.type.config.codeFacturation}")
    private String codeFacturation;

    @Value("${demande.type.config.typeDemande}")
    private String typeDemande;

    @Bean
    public XmlConfiguration prepareXmlConfiguration(){
        XmlConfiguration xmlConfiguration = new XmlConfiguration();

        xmlConfiguration.setRequestId(requestId);
        xmlConfiguration.setChannel(channel);
        xmlConfiguration.setFrom(from);
        xmlConfiguration.setRecipients(extractRecipients());
        xmlConfiguration.setType(type);

        return xmlConfiguration;
    }

    @Bean
    public CreerDemComType createDemandeType() {
        CreerDemComType creerDemComType = new CreerDemComType();

        DemEditionType demEditionType = new DemEditionType();
        demEditionType.setRefDoc(refDoc);
        demEditionType.setCodeAppliEmet(codeAppliEmet);

        creerDemComType.setDemEdition(demEditionType);
        creerDemComType.setCodeAppli(codeAppli);
        creerDemComType.setCodeFacturation(codeFacturation);
        creerDemComType.setTypeDemande(typeDemande);

        return creerDemComType;
    }

    private List<String> extractRecipients(){
        return Arrays.asList(recipients.split(RECIPIENTS_SEPARATOR));
    }

}
