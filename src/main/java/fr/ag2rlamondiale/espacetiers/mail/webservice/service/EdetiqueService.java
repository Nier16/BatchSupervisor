package fr.ag2rlamondiale.espacetiers.mail.webservice.service;

import com.alm.esb.service.gesteditique_1.CreerDemCom1Fault;
import com.alm.esb.service.gesteditique_1.creerdemcom_1.CreerDemComResponseType;
import com.alm.esb.service.gesteditique_1.creerdemcom_1.CreerDemComType;
import fr.ag2rlamondiale.espacetiers.mail.webservice.factory.WebServiceFactory;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@AllArgsConstructor
public class EdetiqueService {
    private static final Logger log = LoggerFactory.getLogger(EdetiqueService.class);

    private final WebServiceFactory webServiceFactory;

    public void sendRequest(CreerDemComType creerDemComType) throws CreerDemCom1Fault {
        CreerDemComResponseType response = webServiceFactory
                .getWebServiceCreerDemCom()
                .creerDemCom1Op(creerDemComType);
        if (null != response.getCreerDemComFunc()){
            BigInteger responseCode = response.getCreerDemComFunc().getCodeRetour();

            if (responseCode.intValue() == 1) {
                log.info("Mail envoye avec succes !! ");
            }
        }
    }
}
