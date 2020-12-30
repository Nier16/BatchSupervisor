package fr.ag2rlamondiale.espacetiers.mail.webservice.handlers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.Set;

/**
 * @author benrajeb
 */

@Component
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {
    public static final String ESB = "esb";
    public static final String ESB_1 = "esb1";
    public static final String WS_HEADER = "WSHeader";
    public static final String UTILISATEUR = "Utilisateur";
    public static final String BUISINESS_FIELDS = "BusinessFields";
    private static final Logger log = LoggerFactory.getLogger(HeaderHandler.class);

    @Value("${demande.header.config.codeApplication}")
    private String codeApplication;

    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        final Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty) {
            try {
            	SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
                envelope.addNamespaceDeclaration(ESB, "http://www.fmk.com/ESB/Framework/ESBTechPivotSchema");
                envelope.addNamespaceDeclaration(ESB_1, "http://www.fmk.com/ESB/Framework/ESBBusinessFields");
                final SOAPElement wsHeader;
                if (envelope.getHeader() == null) {
                    wsHeader = envelope.addHeader().addChildElement(WS_HEADER, ESB);
                } else {
                    wsHeader = envelope.getHeader().addChildElement(WS_HEADER, ESB);
                }

                final SOAPElement businessFields = wsHeader.addChildElement(BUISINESS_FIELDS, ESB);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) {
                    businessFields.addChildElement(UTILISATEUR, ESB_1).setTextContent(codeApplication);
                }
                businessFields.addChildElement("Application", ESB_1).setTextContent(codeApplication);
                smc.getMessage().saveChanges();

            } catch (Exception e) {
                log.error("HeaderHandler - addChildElement : " + e.getMessage());
            }
        }

        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            smc.getMessage().writeTo(out);
            final String str = out.toString();
            if (outboundProperty) {
                log.info("HeaderHandler - REQUEST SOAP " + str);
            } else {
                log.info("HeaderHandler - RESPONSE SOAP " + str);
            }
            out.close();
        } catch (Exception ex) {
            log.error("HeaderHandler : " + ex.getMessage());
        }
        return outboundProperty;
    }

	@Override
    public boolean handleFault(SOAPMessageContext context) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            context.getMessage().writeTo(out);
            String str = out.toString();
            log.error("HeaderHandler Fault " + str);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return true;
    }
	
	@Override
    public void close(MessageContext context) {
    	//not used
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}
