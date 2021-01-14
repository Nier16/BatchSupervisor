package fr.ag2rlamondiale.hec.batchsupervisor.service.data;

import fr.ag2rlamondiale.hec.batchsupervisor.client.StateReaderClient;
import fr.ag2rlamondiale.hec.batchsupervisor.client.StateWriterClient;
import fr.ag2rlamondiale.hec.batchsupervisor.dto.BatchState;
import fr.ag2rlamondiale.hec.batchsupervisor.model.Slot;
import fr.ag2rlamondiale.hec.batchsupervisor.model.SupervisorResult;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Service
public class StateDataService {
    private static final Logger log = LoggerFactory.getLogger(StateDataService.class);

    private final StateReaderClient stateReaderClient;
    private final StateWriterClient stateWriterClient;

    private BatchState lastSaved = null;
    private List<BatchState> statesToSave = new ArrayList<>();
    private List<BatchState> lastStateForEachBatch;
    private LocalDateTime retrievalTime;

    public StateDataService(StateReaderClient stateReaderClient, StateWriterClient stateWriterClient){
        this.stateReaderClient = stateReaderClient;
        this.stateWriterClient = stateWriterClient;
    }


    // Retourn la liste des states d'un batch depuis le dernier state traité par le supervisor
    public List<BatchState> getLastStatesForBatch(String idBatch){
        this.lastSaved = null;
        return Objects.requireNonNull(stateReaderClient
                .getLastStates(idBatch, this.retrievalTime)
                .block())
                .getValues();
    }

    // Retour le dernier passage d'un batch(dateCreation != null)
    public BatchState getLastStateForBatch(String batchId){
        List<BatchState> states = this.loadLastStateForEachBatch();
        for(BatchState state : states){
            if(state.getIdBatch().equals(batchId))
                return state;
        }
        return null;
    }

    // Retour le temps du dernier passage d'un batch(dateCreation != null)
    public LocalDateTime getLastStateCreateTimeForBatch(String batchId){
        BatchState state = getLastStateForBatch(batchId);
        return state != null ? state.getCreateDate() : null;
    }

    // Retourne le dernier passage de tout les batch(dateCreation != null)
    public List<BatchState> loadLastStateForEachBatch(){
        if(this.lastStateForEachBatch == null){
            this.retrievalTime = LocalDateTime.now();
            this.lastStateForEachBatch = stateReaderClient
                    .getLastStateForEachBatch()
                    .block()
                    .getValues();
        }
        return this.lastStateForEachBatch;
    }

    // Retourn tout les states de tout les batch dans une periode donnée
    public List<BatchState> getBatchStatesForPeriod(Slot period){
        return stateReaderClient
                .getStatesForPeriod(period)
                .block()
                .getValues();
    }

    // Retourn le dernier state de chaque batch qui est deja traité par le superviseur dans une période donnée
    public List<BatchState> getLastBatchesStateForPeriod(Slot period) {
        List<BatchState> states = getBatchStatesForPeriod(period);
        Map<String, BatchState> results = new HashMap<>();
        for (BatchState state : states) {
            if(state.getResult() != null){
                results.put(state.getIdBatch(), state);
            }
        }
        return new ArrayList<>(results.values());
    }

    // Creation d'un state et l'ajouter dans une liste qui sera sauvgarder a la fin de tout les traitements
    public void createStateLater(SupervisorResult result, String idBatch) {
        BatchState state = new BatchState();
        state.setIdBatch(idBatch);
        state.setResult(result);
        state.setSupervisorDate(LocalDateTime.now());

        saveStateLater(state);
    }

    // Update d'un state existant et l'ajouter dans une liste qui sera sauvgarder a la fin de tout les traitements
    public void updateStateLater(BatchState state, SupervisorResult result) {
        state.setResult(result);
        state.setSupervisorDate(LocalDateTime.now());
        this.lastSaved = state;
        saveStateLater(state);
    }

    // ajouter d'une state dans une liste qui sera sauvgarder a la fin de tout les traitements
    public void saveStateLater(BatchState state) {
        this.lastSaved = state;
        this.statesToSave.add(state);
        log.info(state.toString());
    }

    // Envoi d'une requette pour sauvgarder les state creés ou modifiés
    public void saveAllStates() {
        if(!this.statesToSave.isEmpty()){
            this.stateWriterClient.saveAllStates(this.statesToSave);
        }
    }
}
