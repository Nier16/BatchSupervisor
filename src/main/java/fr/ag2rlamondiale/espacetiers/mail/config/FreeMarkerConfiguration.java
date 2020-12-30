package fr.ag2rlamondiale.espacetiers.mail.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

//@org.springframework.context.annotation.Configuration
public class FreeMarkerConfiguration {

    public static final String TEMPLATE_DIRECTORY = "/templates";

    public FreeMarkerConfiguration(FreeMarkerConfigurer freeMarkerConfigurer){
        Configuration configuration = new Configuration();
        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), TEMPLATE_DIRECTORY);
        configuration.setTemplateLoader(templateLoader);
        freeMarkerConfigurer.setConfiguration(configuration);
    }
}
