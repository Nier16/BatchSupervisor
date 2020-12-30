package fr.ag2rlamondiale.espacetiers.mail.service.impl;

import fr.ag2rlamondiale.espacetiers.mail.model.TemplatesNames;
import fr.ag2rlamondiale.espacetiers.mail.service.FreeMarkerService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@Service
public class FreeMarkerServiceImpl implements FreeMarkerService {
    public static final String TEMPLATE_DIRECTORY = "/templates";

    //@Autowired
    //private FreeMarkerConfigurer FreeMarkerConfigurer;
    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public String processTemplate(TemplatesNames templatesNames, Map<String, Object> templateModel) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), TEMPLATE_DIRECTORY);
        configuration.setTemplateLoader(templateLoader);
        freeMarkerConfigurer.setConfiguration(configuration);
        try {
            Template freemarkerTemplate = freeMarkerConfigurer
                    .getConfiguration()
                    .getTemplate(templatesNames.toString());
            Writer out = new StringWriter();
            freemarkerTemplate.process(templateModel, out);
            return out.toString();
        } catch (IOException | TemplateException e) {
            System.out.println("Exception lors de l'ecriture" + e.getMessage());
        }
        return null;
    }
}
