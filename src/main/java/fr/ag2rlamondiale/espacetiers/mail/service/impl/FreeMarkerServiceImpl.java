package fr.ag2rlamondiale.espacetiers.mail.service.impl;

import fr.ag2rlamondiale.espacetiers.mail.model.TemplatesNames;
import fr.ag2rlamondiale.espacetiers.mail.service.FreeMarkerService;
import fr.ag2rlamondiale.espacetiers.mail.webservice.service.EdetiqueService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

@Service
public class FreeMarkerServiceImpl implements FreeMarkerService {
    public static final String TEMPLATE_DIRECTORY = "/templates";

    private static final Logger log = LoggerFactory.getLogger(FreeMarkerServiceImpl.class);

    FreeMarkerConfigurer freeMarkerConfigurer;

    public FreeMarkerServiceImpl(FreeMarkerConfigurer freeMarkerConfigurer){
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), TEMPLATE_DIRECTORY);
        configuration.setTemplateLoader(templateLoader);
        freeMarkerConfigurer.setConfiguration(configuration);
    }

    @Override
    public String processTemplate(TemplatesNames templatesNames, Map<String, Object> templateModel) throws Exception {
        try {
            Template freemarkerTemplate = freeMarkerConfigurer
                    .getConfiguration()
                    .getTemplate(templatesNames.toString());
            Writer out = new StringWriter();
            freemarkerTemplate.process(templateModel, out);
            return out.toString();
        } catch (IOException | TemplateException e) {
            log.error("Erreur lors du traitement du template "
                    +  templatesNames.toString() + " par freemarker : "
                    +  e.getMessage());
            throw e;
        }
    }

    @Override
    public void processToFile(TemplatesNames templatesNames, Map<String, Object> templateModel, String fileName) {
        try {
            Template freemarkerTemplate = freeMarkerConfigurer
                    .getConfiguration()
                    .getTemplate(templatesNames.toString());
            Writer out = new FileWriter("./"+fileName);
            freemarkerTemplate.process(templateModel, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Erreur lors du traitement du template "
                    +  templatesNames.toString() + " par freemarker : "
                    + Arrays.toString(e.getStackTrace()));
        }
    }
}
