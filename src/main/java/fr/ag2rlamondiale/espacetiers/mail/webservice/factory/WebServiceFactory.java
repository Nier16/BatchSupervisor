package fr.ag2rlamondiale.espacetiers.mail.webservice.factory;

import com.alm.esb.service.gesteditique_1.CreerDemCom1Port;
import com.alm.esb.service.gesteditique_1.GestEditique1SOAP;
import fr.ag2rlamondiale.espacetiers.mail.webservice.handlers.ServiceHandlerResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.util.*;


@Component("webServiceFactory")
public class WebServiceFactory {
    private static final Logger log = LoggerFactory.getLogger(WebServiceFactory.class);
	
    @Value("${webService.endpoint.creerDemCom}")
    private String creerDemComEndpoint;

    @Value("${demande.header.config.codeApplication}")
	private String codeApplication;


	private final ServiceHandlerResolver serviceHandlerResolver;
	private final GestEditique1SOAP gestEditiqueagent;

	public WebServiceFactory(ServiceHandlerResolver serviceHandlerResolver){
		this.serviceHandlerResolver = serviceHandlerResolver;
		this.gestEditiqueagent = new GestEditique1SOAP();

	}

	/*
	 * Fonction permettant d'ajouter l'entete http exigé par la pfs pour
	 * identifier notre applicatif
	 */
	private Map<String, List<String>> getRequestHeaders() {
		Map<String, List<String>> requestHeaders = new HashMap<>();
		
		requestHeaders.put("X-Forwarded-For", Collections.singletonList(codeApplication));
		
		return requestHeaders;
	}
	
	public CreerDemCom1Port getWebServiceCreerDemCom() {
		gestEditiqueagent.setHandlerResolver(serviceHandlerResolver);
		CreerDemCom1Port port = gestEditiqueagent.getCreerDemCom1S12Edpt();
		((BindingProvider) port).getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, getRequestHeaders());
		((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				creerDemComEndpoint);
		
		return port;
	}
	

}
