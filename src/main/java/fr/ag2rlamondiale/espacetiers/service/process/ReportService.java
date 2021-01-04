package fr.ag2rlamondiale.espacetiers.service.process;

import fr.ag2rlamondiale.espacetiers.client.StateClient;
import fr.ag2rlamondiale.espacetiers.config.supervisor.SupervisorInformation;
import fr.ag2rlamondiale.espacetiers.dto.BatchState;
import fr.ag2rlamondiale.espacetiers.mail.model.TemplatesNames;
import fr.ag2rlamondiale.espacetiers.mail.service.EmailService;
import fr.ag2rlamondiale.espacetiers.mail.service.FreeMarkerService;
import fr.ag2rlamondiale.espacetiers.model.ScheduleInformation;
import fr.ag2rlamondiale.espacetiers.model.report.*;
import fr.ag2rlamondiale.espacetiers.useful.Useful;
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

    @Value("${report.advanced:false}")
    private boolean reportAdvanced;

    @Value("${report.weekDay:0}")
    private int weekDay;

    @Value("${report.monthDay:0}")
    private int monthDay;

    @Value("${report.yearDay:0}")
    private int yearDay;

    private final EmailService emailService;
    private final SupervisorInformation supervisorInformation;
    private final ScheduleService scheduleService;
    private final StateClient stateClient;
    private final FreeMarkerService freeMarkerService;

    private final List<BatchState> incidents;

    public ReportService(EmailService emailService,
                         SupervisorInformation supervisorInformation,
                         ScheduleService scheduleService,
                         StateClient stateClient,
                         FreeMarkerService freeMarkerService){
        this.emailService = emailService;
        this.supervisorInformation = supervisorInformation;
        this.scheduleService = scheduleService;
        this.stateClient = stateClient;
        this.incidents = new ArrayList<>();
        this.freeMarkerService = freeMarkerService;
    }

    public void proceed() {
        if(this.haveToReport()){
            Report report = new Report();
            report.getPeriod().setEnd(LocalDateTime.now());
            if(this.reportAdvanced){
                this.advancedReport(report);
            }else{
                this.simpleReport(report);
            }
        }
    }

    public void incidentReport(){
        if(this.incidents.isEmpty())
            return;

        Report<SimpleReportGroup> report = new Report();
        Map<String, List<SimpleReportLine>> groups = new HashMap<>();
        ScheduleInformation scheduleInformation;
        SimpleReportLine reportLine;

        for(BatchState batchState : this.incidents){
            scheduleInformation = scheduleService
                    .getScheduleInformation(batchState.getIdBatch());

            reportLine = (SimpleReportLine) scheduleInformation
                    .toReportLine(new SimpleReportLine(false), batchState.getCreateDate());

            report.addReportLine(scheduleInformation.getGroupName(), reportLine);
        }

        try {
            freeMarkerService.processToFile(TemplatesNames.INCIDENT,
                    Collections.singletonMap("data", report),
                    "Hello.html");
        }catch (Exception e){
            log.error("Rapport d'incident non envoyé : " + e.getMessage());
        }

    }

    public void addIncident(BatchState state){
        this.incidents.add(state);
    }

    private void simpleReport(Report report){
        report.getPeriod().setStart(report.getPeriod().getEnd().minusDays(1));
    }

    private void advancedReport(Report report){
        AdvancedReportTypes advancedReportTypes = getAdvancedReportType();
        report.getPeriod().setStart(report.getPeriod().getEnd()
                .minus(1, advancedReportTypes.getPeriod()));
        List<BatchState> states = stateClient.getStatesForPeriod(
                report.getPeriod().getStart(), report.getPeriod().getEnd())
                .block().getValues();

        for(BatchState state : states){
            AdvancedReportLine reportLine = new AdvancedReportLine();
        }
    }

    private boolean haveToReport(){
        LocalDateTime timeToReport = Useful.getTodayWithTime(reportTimeInMinutes / 60, reportTimeInMinutes % 60);
        if(this.supervisorInformation.getLastReportTime() != null){
            return timeToReport.isBefore(timeToReport)
                    && Useful.isAfterOrEquals(SupervisorService.startTime, timeToReport);
        }else{
            return Useful.isAfterOrEquals(SupervisorService.startTime, timeToReport)
                    && Useful.isBeforeOrEquals(SupervisorService.startTime, SupervisorService.startTime.plusMinutes(DELTA));
        }
    }

    private AdvancedReportTypes getAdvancedReportType(){
        LocalDateTime startTime = SupervisorService.startTime;
        if(startTime.getDayOfYear() == yearDay){
            return AdvancedReportTypes.YEARLY;
        }else if(startTime.getDayOfMonth() == monthDay){
            return AdvancedReportTypes.MONTHLY;
        }else if(startTime.getDayOfWeek().getValue() == weekDay){
            return AdvancedReportTypes.WEEKLY;
        }else{
            return AdvancedReportTypes.DAILY;
        }
    }
}
