package fr.ag2rlamondiale.espacetiers.mail.webservice.handlers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.Collections;
import java.util.List;

/**
 * @author benrajeb
 */
@Component("serviceHandlerResolver")
@AllArgsConstructor
public class ServiceHandlerResolver implements HandlerResolver {

    private final HeaderHandler headerHandler;

    public List<Handler> getHandlerChain(PortInfo arg0) {
        return Collections.singletonList(headerHandler);
    }
}
