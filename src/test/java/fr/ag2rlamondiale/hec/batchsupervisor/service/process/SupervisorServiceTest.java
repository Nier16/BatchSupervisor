package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.ScheduleMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.service.data.ScheduleDataService;
import fr.ag2rlamondiale.hec.batchsupervisor.service.data.StateDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SupervisorServiceTest {
    @InjectMocks
    @Spy
    private SupervisorService supervisorService;

    @Mock
    private BatchService batchService;

    @Mock
    private StateDataService stateDataService;

    @Mock
    private ReportService reportService;

    @Mock
    private ScheduleDataService scheduleDataService;

    private final ScheduleMockData scheduleMockData = new ScheduleMockData();

    @Test
    public void proceed() {
        doReturn(null).when(stateDataService).loadLastStateForEachBatch();
        doNothing().when(scheduleDataService).loadAllActiveSchedules();
        doNothing().when(batchService).proceed(scheduleMockData.idBatch1);
        doNothing().when(reportService).proceed();
        doNothing().when(stateDataService).saveAllStates();

        when(scheduleDataService.getAllBatchIds())
                .thenReturn(scheduleMockData.batchesId);

        supervisorService.proceed();

        verify(batchService).proceed(scheduleMockData.idBatch1);
        verify(batchService).proceed(scheduleMockData.idBatch2);
    }
}