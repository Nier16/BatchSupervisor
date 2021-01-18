package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import fr.ag2rlamondiale.hec.batchsupervisor.config.supervisor.SupervisorInformation;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.model.TemplatesNames;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.service.EmailService;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.service.FreeMarkerService;
import fr.ag2rlamondiale.hec.batchsupervisor.model.ScheduleInformation;
import fr.ag2rlamondiale.hec.batchsupervisor.service.data.StateDataService;
import fr.ag2rlamondiale.hec.batchsupervisor.useful.Useful;
import fr.ag2rlamondiale.hec.batchsupervisor.model.report.Report;
import fr.ag2rlamondiale.hec.batchsupervisor.model.report.SimpleReportLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private static final int DELTA = 4;

    @Value("${report.time:480}")
    private Integer reportTimeInMinutes;

    private final EmailService emailService;
    private final SupervisorInformation supervisorInformation;
    private final ScheduleService scheduleService;
    private final StateDataService stateDataService;
    private final FreeMarkerService freeMarkerService;

    private final List<BatchState> stateToReport;
    private Boolean haveToReport;

    public ReportService(EmailService emailService,
                         SupervisorInformation supervisorInformation,
                         ScheduleService scheduleService,
                         StateDataService stateDataService,
                         FreeMarkerService freeMarkerService){
        this.emailService = emailService;
        this.supervisorInformation = supervisorInformation;
        this.scheduleService = scheduleService;
        this.stateDataService = stateDataService;
        this.stateToReport = new ArrayList<>();
        this.freeMarkerService = freeMarkerService;

    }

    public void proceed() {
        if(this.stateToReport.isEmpty()){
            log.info("Aucun rapport a faire");
            return;
        }

        Report report = new Report();
        report.getPeriod().setEnd(getStartTime());
        if(this.haveToReport){
            report.getPeriod().setStart(report.getPeriod().getEnd().minusDays(1));
            this.report(false, report);
        }else{
            this.report(true, report);
        }
    }

    private void report(boolean incident, Report report){
        ScheduleInformation scheduleInformation;
        SimpleReportLine reportLine;
        LocalDateTime lastExecTime;

        for(BatchState batchState : this.stateToReport){
            lastExecTime = stateDataService.getLastStateCreateTimeForBatch(batchState.getIdBatch());
            scheduleInformation = scheduleService
                    .getScheduleInformation(batchState.getIdBatch(), getStartTime());

            reportLine = scheduleInformation.toReportLine(
                    new SimpleReportLine(batchState.getResult()), lastExecTime);

            report.addReportLine(scheduleInformation.getGroupName(), reportLine);
        }
        try {
            emailService.sendEmail("Report", incident ? TemplatesNames.INCIDENT : TemplatesNames.SIMPLE_REPORT, report);
            freeMarkerService.processToFile(
                    incident ? TemplatesNames.INCIDENT : TemplatesNames.SIMPLE_REPORT,
                    Collections.singletonMap("data", report),
                    "Hello.html");
        }catch (Exception e){
            log.error("Rapport d'incident non envoyé : " + e.getMessage());
        }

    }

    public void addToReportList(BatchState state){
        this.stateToReport.add(state);
    }

    public boolean haveToReport(){
        if(this.haveToReport == null){
            LocalDateTime lastReportTime = supervisorInformation.getLastReportTime();
            LocalDateTime timeToReport = Useful.getTodayWithTime(reportTimeInMinutes / 60, reportTimeInMinutes % 60);
            LocalDateTime startTime = getStartTime();
            if(lastReportTime != null){
                this.haveToReport =  lastReportTime.isBefore(timeToReport)
                        && Useful.isAfterOrEquals(timeToReport, startTime);
            }else{
                this.haveToReport = Useful.isAfterOrEquals(timeToReport, startTime)
                        && Useful.isAfterOrEquals(startTime, timeToReport.plusMinutes(DELTA));
            }
        }
        return this.haveToReport;
    }

    public LocalDateTime getStartTime(){
        return SupervisorService.getStartTime();
    }
}
