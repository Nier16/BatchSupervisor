package fr.ag2rlamondiale.hec.batchsupervisor.service.data;

import fr.ag2rlamondiale.hec.batchsupervisor.client.StateReaderClient;
import fr.ag2rlamondiale.hec.batchsupervisor.client.StateWriterClient;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchStateListDto;
import fr.ag2rlamondiale.hec.batchsupervisor.model.SupervisorResult;
import fr.ag2rlamondiale.hec.batchsupervisor.useful.Useful;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    private DataMock dm;

    @Before
    public void initDataMock(){
        this.dm = new DataMock();
    }

    static private class DataMock{
        public String idBatch1 = "1";
        public String idBatch2 = "2";

        public LocalDateTime bs1Create = Useful.getTodayWithTime(10, 0);
        public LocalDateTime bs2Create = Useful.getTodayWithTime(10, 30);
        public LocalDateTime bs3Create = Useful.getTodayWithTime(10, 10);
        public LocalDateTime supervisorDate = Useful.getTodayWithTime(10, 20);
        public SupervisorResult bs1Result = SupervisorResult.OK;
        public SupervisorResult bs3Result = SupervisorResult.LAUNCH_KO;

        public BatchState bs1 = new BatchState(idBatch1, bs1Create, supervisorDate, bs1Result);
        public BatchState bs2 = new BatchState(idBatch1, bs2Create);
        public BatchState bs3 = new BatchState(idBatch2, bs3Create, supervisorDate, bs3Result);

        public List<BatchState> lastList = Arrays.asList(bs2, bs3);
        public List<BatchState> b1List = Arrays.asList(bs1, bs2);
        public List<BatchState> allList = Arrays.asList(bs1, bs2, bs3);

        public Mono<BatchStateListDto> lastMono = Mono.just(new BatchStateListDto(lastList));
        public Mono<BatchStateListDto> b1Mono = Mono.just(new BatchStateListDto(b1List));
        public Mono<BatchStateListDto> allMono = Mono.just(new BatchStateListDto(allList));
    }

    @Before
    public void setRetrieveTime(){
        this.stateDataService.setRetrievalTime(dm.supervisorDate);
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
    }

    @Test
    public void updateStateLater() {
    }

    @Test
    public void saveStateLater() {
    }

    @Test
    public void saveAllStates() {
    }
}