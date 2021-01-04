package fr.ag2rlamondiale.espacetiers.mail.webservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlConfiguration {
    private String requestId;
    private String channel;
    private String from;
    private List<String> recipients;
    private String subject;
    private String type;
    private String htmlContent;
}
