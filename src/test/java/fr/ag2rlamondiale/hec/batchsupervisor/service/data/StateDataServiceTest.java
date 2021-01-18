package fr.ag2rlamondiale.hec.batchsupervisor.service.data;

import fr.ag2rlamondiale.hec.batchsupervisor.client.StateReaderClient;
import fr.ag2rlamondiale.hec.batchsupervisor.client.StateWriterClient;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.StateMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.model.SupervisorResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StateDataServiceTest {

    @Mock
    private StateReaderClient stateReaderClient;

    @Mock
    private StateWriterClient stateWriterClient;

    @InjectMocks
    @Spy
    private StateDataService stateDataService;

    private final StateMockData dm = new StateMockData();

    @Before
    public void setRetrieveTime(){
        ReflectionTestUtils.setField(stateDataService, "retrievalTime", dm.supervisorDate);
    }


    @Test
    public void getLastStatesForBatch() {
        when(stateReaderClient.getLastStates(dm.idBatch1, dm.supervisorDate)).thenReturn(dm.b1Mono);
        assertEquals(stateDataService.getLastStatesForBatch(dm.idBatch1), dm.b1List);
    }

    @Test
    public void getLastStateForBatch() {
       doReturn(dm.lastList).when(stateDataService).loadLastStateForEachBatch();
       assertEquals(stateDataService.getLastStateForBatch(dm.idBatch1), dm.bs2);
    }

    @Test
    public void getLastStateCreateTimeForBatch() {
        doReturn(dm.bs2).when(stateDataService).getLastStateForBatch(dm.idBatch1);
        assertEquals(stateDataService.getLastStateCreateTimeForBatch(dm.idBatch1), dm.bs2Create);
    }

    @Test
    public void loadLastStateForEachBatch() {
        when(stateReaderClient.getLastStateForEachBatch()).thenReturn(dm.lastMono);
        assertEquals(stateDataService.loadLastStateForEachBatch(), dm.lastList);
    }

    @Test
    public void createStateLater() {
        SupervisorResult result = SupervisorResult.OK;
        String idBatch = "1";
        stateDataService.createStateLater(result, idBatch);
        assertTrue(
                stateDataService.getLastSaved().getResult().equals(result) &&
                        stateDataService.getLastSaved().getIdBatch().equals(idBatch)
        );
    }

    @Test
    public void updateStateLater() {
        SupervisorResult result = SupervisorResult.OK;
        String idBatch = "1";
        BatchState state = new BatchState(idBatch, null);
        stateDataService.updateStateLater(state, result);
        assertTrue(
                stateDataService.getLastSaved().getResult().equals(result) &&
                        stateDataService.getLastSaved().getIdBatch().equals(idBatch)
        );
    }

    @Test
    public void saveAllStates() {
        List<BatchState> batchToSave = Collections.singletonList(dm.bs1);
        stateDataService.saveAllStates();
        ReflectionTestUtils.setField(stateDataService, "statesToSave", batchToSave);
        stateDataService.saveAllStates();
        verify(stateWriterClient, times(1)).saveAllStates(batchToSave);
    }
}