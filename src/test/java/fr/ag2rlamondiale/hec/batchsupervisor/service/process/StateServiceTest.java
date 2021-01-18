package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.StateMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.service.data.StateDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StateServiceTest {

    @InjectMocks
    private StateService stateService;

    @Mock
    private StateDataService stateDataService;

    private StateMockData dm = new StateMockData();

    @Test
    public void load() {
        when(stateDataService.getLastStatesForBatch(dm.idBatch1))
                .thenReturn(dm.b1List);

        stateService.load(dm.idBatch1);

        assertEquals(ReflectionTestUtils.getField(stateService, "states"), dm.b1List);
    }

    @Test
    public void next() {
        // NULL
        ReflectionTestUtils.setField(
                stateService,
                "states",
                Collections.emptyList());
        assertNull(stateService.next());

        // BS1
        ReflectionTestUtils.setField(
                stateService,
                "states",
                dm.b1List);
        assertEquals(stateService.next(), dm.bs1);
    }

    @Test
    public void first() {
        ReflectionTestUtils.setField(
                stateService,
                "states",
                Collections.emptyList());
        assertNull(stateService.first());

        // BS1
        ReflectionTestUtils.setField(
                stateService,
                "states",
                dm.b1List);
        assertEquals(stateService.first(), dm.bs1);
    }
}