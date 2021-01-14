package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import fr.ag2rlamondiale.hec.batchsupervisor.mail.model.TemplatesNames;
import fr.ag2rlamondiale.hec.batchsupervisor.mail.service.EmailService;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.ReportMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.ScheduleMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.StateMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.SupervisorMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.model.report.Report;
import fr.ag2rlamondiale.hec.batchsupervisor.service.data.StateDataService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {

    @InjectMocks
    @Spy
    private ReportService reportService;

    @Mock
    private EmailService emailService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private StateDataService stateDataService;


    private final ScheduleMockData scheduleMockData = new ScheduleMockData();
    private final StateMockData stateMockData = new StateMockData();
    private final SupervisorMockData supervisorMockData = new SupervisorMockData();
    private final ReportMockData reportMockData = new ReportMockData();

    @SneakyThrows
    @Test
    public void proceed() {
        Report report = reportMockData.getReport(
                supervisorMockData.supervisorStartTime,
                true,
                stateMockData.bs3,
                scheduleMockData.scheduleInformation,
                stateMockData.bs3Create
        );
        ReflectionTestUtils.setField(
                reportService,
                "stateToReport",
                Collections.singletonList(stateMockData.bs3));
        ReflectionTestUtils.setField(reportService, "haveToReport", true);

        when(reportService.getStartTime()).thenReturn(supervisorMockData.supervisorStartTime);
        when(stateDataService.getLastStateCreateTimeForBatch(stateMockData.idBatch2)).thenReturn(stateMockData.bs3Create);
        when(scheduleService.getScheduleInformation(stateMockData.idBatch2)).thenReturn(scheduleMockData.scheduleInformation);
        doNothing().when(emailService).sendEmail("Report", TemplatesNames.SIMPLE_REPORT, report);

        reportService.proceed();

        verify(emailService).sendEmail("Report", TemplatesNames.SIMPLE_REPORT, report);
    }

    @Test
    public void addToReportList() {
    }

    @Test
    public void initReportNeeded() {

    }
}