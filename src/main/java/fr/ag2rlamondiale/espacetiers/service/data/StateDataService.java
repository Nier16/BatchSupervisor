package fr.ag2rlamondiale.espacetiers.service.data;

import fr.ag2rlamondiale.espacetiers.client.StateClient;
import fr.ag2rlamondiale.espacetiers.dto.BatchState;
import fr.ag2rlamondiale.espacetiers.model.SupervisorResult;
import fr.ag2rlamondiale.espacetiers.service.process.StateService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class StateDataService {
    private static final Logger log = LoggerFactory.getLogger(StateDataService.class);

    private final StateClient stateClient;

    private BatchState lastSaved = null;
    private List<BatchState> statesToSave = new ArrayList<>();

    public StateDataService(StateClient stateClient){
        this.stateClient = stateClient;
    }

    public List<BatchState> getLastStatesForBatch(int idBatch){
        this.lastSaved = null;
        return stateClient.
                getLastStates(idBatch).
                block().
                getValues();
    }

    public void createStateLater(SupervisorResult result, int idBatch, LocalDateTime createTime) {
        BatchState state = new BatchState();
        state.setIdBatch(idBatch);
        state.setResult(result);
        state.setSupervisorDate(LocalDateTime.now());
        state.setCreateDate(createTime);

        saveStateLater(state);
    }

    public void updateStateLater(BatchState state, SupervisorResult result) {
        state.setResult(result);
        state.setSupervisorDate(LocalDateTime.now());
        this.lastSaved = state;
        saveStateLater(state);
    }

    public void saveStateLater(BatchState state) {
        this.lastSaved = state;
        this.statesToSave.add(state);
        log.info(state.toString());
    }

    public void saveAllStates() {
        if(!this.statesToSave.isEmpty()){
            this.stateClient.saveAllStates(this.statesToSave);
        }
    }
}
