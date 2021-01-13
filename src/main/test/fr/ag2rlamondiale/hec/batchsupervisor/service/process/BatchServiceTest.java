package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.ScheduleMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.StateMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.service.data.StateDataService;
import fr.ag2rlamondiale.hec.batchsupervisor.useful.Useful;
import org.apache.tomcat.jni.Local;
import org.junit.Test;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.doReturn;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceTest {
    @InjectMocks
    private BatchService batchService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private StateService stateService;

    @Mock
    private StateDataService stateDataService;

    @Mock
    private ReportService reportService;


    private ScheduleMockData scheduleMockData = new ScheduleMockData();
    private StateMockData stateMockData = new StateMockData();
    private DataMock dm = new DataMock();

    static public class DataMock{
        LocalDateTime supervisorStartTime = Useful.getTodayWithTime(10, 35);
    }

    @Before
    public void init(){
        scheduleService.setSchedules(scheduleMockData.schedulesList);
        stateService.setStates(stateMockData.b1List);
        SupervisorService.startTime = dm.supervisorStartTime;
    }

    @Test
    public void proceed() {
        Mockito.doNothing().when(stateService).load(scheduleMockData.idBatch1);
        Mockito.doNothing().when(scheduleService).load(scheduleMockData.idBatch1);

        batchService.proceed(scheduleMockData.idBatch2);

        assertEquals();
    }

    @Test
    public void addToReport() {
    }
}