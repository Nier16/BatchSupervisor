package fr.ag2rlamondiale.espacetiers.config.supervisor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorInformation implements Serializable {
    private LocalDateTime lastExec;
    private LocalDateTime lastSaveTime;
    private LocalDateTime lastReportTime;
}
