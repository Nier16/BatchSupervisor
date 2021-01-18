package fr.ag2rlamondiale.hec.batchsupervisor.mockdata;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import fr.ag2rlamondiale.hec.batchsupervisor.model.ScheduleInformation;
import fr.ag2rlamondiale.hec.batchsupervisor.model.report.Report;
import fr.ag2rlamondiale.hec.batchsupervisor.model.report.SimpleReportLine;
import fr.ag2rlamondiale.hec.batchsupervisor.service.process.BatchService;
import fr.ag2rlamondiale.hec.batchsupervisor.service.process.SupervisorService;

import java.time.LocalDateTime;

public class ReportMockData {
    public Report getReport(LocalDateTime startTime,
                            boolean haveToReport,
                            BatchState batchState,
                            ScheduleInformation scheduleInformation,
                            LocalDateTime lastExecTime){
        Report report = new Report();
        report.getPeriod().setEnd(startTime);
        if(haveToReport){
            report.getPeriod().setStart(report.getPeriod().getEnd().minusDays(1));
        }
        report.addReportLine(scheduleInformation.getGroupName(),
                scheduleInformation.toReportLine(
                        new SimpleReportLine(batchState.getResult()), lastExecTime));
        return report;
    }
}
