package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.ScheduleMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.StateMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.SupervisorMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.model.SupervisorResult;
import fr.ag2rlamondiale.hec.batchsupervisor.service.data.StateDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;


import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceTest {
    @InjectMocks
    @Spy
    private BatchService batchService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private StateService stateService;

    @Mock
    private StateDataService stateDataService;

    @Mock
    private ReportService reportService;


    private final ScheduleMockData scheduleMockData = new ScheduleMockData();
    private final StateMockData stateMockData = new StateMockData();
    private final SupervisorMockData supervisorMockData = new SupervisorMockData();


    @Test
    public void proceed() {
        when(batchService.getStartTime())
                .thenReturn(supervisorMockData.supervisorStartTime);

        when(stateService.next())
                .thenReturn(stateMockData.bs2, null)
                .thenReturn(stateMockData.bs1, stateMockData.bs2, null);

        when(scheduleService.next())
                .thenReturn(null)
                .thenReturn(scheduleMockData.slot1, scheduleMockData.slot2, scheduleMockData.slot3, null);


        batchService.proceed(scheduleMockData.idBatch1);
        batchService.proceed(scheduleMockData.idBatch1);

        verify(stateDataService).updateStateLater(stateMockData.bs2, SupervisorResult.OUT_SLOT_KO);
        verify(stateDataService).updateStateLater(stateMockData.bs2, SupervisorResult.OK);
        verify(scheduleService, times(5)).next();
        verify(stateService, times(5)).next();
        verify(batchService).addToReport(false);
    }

    @Test
    public void addToReport() {
        BatchState lastSaved = stateMockData.bs2;

        when(reportService.haveToReport())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(stateDataService.getLastSaved())
                .thenReturn(lastSaved)
                .thenReturn(null)
                .thenReturn(lastSaved);

        batchService.addToReport(true);
        batchService.addToReport(true);
        batchService.addToReport(false);

        verify(reportService, times(2)).addToReportList(lastSaved);
        verify(reportService).addToReportList(null);
    }
}