package fr.ag2rlamondiale.hec.batchsupervisor.service.impl;

import fr.ag2rlamondiale.hec.batchsupervisor.client.OrchestratorClient;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.Report;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.ReportType;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.model.TemplatesNames;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.service.EmailService;
import fr.ag2rlamondiale.hec.batchsupervisor.service.SupervisorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class SupervisorServiceImpl implements SupervisorService {
    private final OrchestratorClient orchestratorClient;
    private final EmailService emailService;

    public void proceed() throws Exception {
        Report report = orchestratorClient
                .getOrchestratorReport()
                .block();
        if (report != null) {
            emailService.sendEmail(
                    report.getType().toString(),
                    report.getType().equals(ReportType.INCIDENT) ? TemplatesNames.INCIDENT : TemplatesNames.SIMPLE_REPORT,
                    report
            );
        }
    }
}
