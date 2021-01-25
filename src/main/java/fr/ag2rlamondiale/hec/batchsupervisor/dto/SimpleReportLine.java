package fr.ag2rlamondiale.hec.batchsupervisor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleReportLine {
    private String batchName;
    private LocalDateTime lastExec;
    private Slot nextSlot;
    private List<String> informations;
    private SupervisorResult result;
}
