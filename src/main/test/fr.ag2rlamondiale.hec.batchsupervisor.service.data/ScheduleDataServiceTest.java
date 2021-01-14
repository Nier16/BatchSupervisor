package fr.ag2rlamondiale.hec.batchsupervisor.service.data;

import fr.ag2rlamondiale.hec.batchsupervisor.client.ScheduleClient;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.ScheduleMockData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleDataServiceTest {

    @InjectMocks
    private ScheduleDataService scheduleDataService;

    @Mock
    private ScheduleClient scheduleClient;

    private final ScheduleMockData dm = new ScheduleMockData();

    @Before
    public void initAllSchedules(){
        this.scheduleDataService.setAllSchedules(dm.schedulesList);
    }

    @Test
    public void loadAllActiveSchedules(){
        when(scheduleClient.getAllActiveSchedules()).thenReturn(dm.schedulesMono);
        scheduleDataService.loadAllActiveSchedules();
        assertEquals(scheduleDataService.getAllSchedules(), dm.schedulesList);
    }

    @Test
    public void getAllBatchIds() {
        assertEquals(scheduleDataService.getAllBatchIds(), dm.batchesId);
    }

    @Test
    public void getBatchSchedules() {
        assertEquals(scheduleDataService.getBatchSchedules(dm.idBatch1), Collections.singletonList(dm.s1));
    }

}