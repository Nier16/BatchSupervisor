package fr.ag2rlamondiale.hec.batchsupervisor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
    private LocalDateTime start;
    private LocalDateTime end;

    public String toString() {
        if (start != null) {
            if (end != null) {
                if (start.getDayOfYear() == end.getDayOfYear()) {
                    return "le " + start.toLocalDate().toString() + " entre " + start.toLocalTime().toString() + " et " + end.toLocalTime().toString();
                } else {
                    return "Entre le " + start.toString() + " et le " + end.toString();
                }
            } else {
                return "Le " + start.toString();
            }
        } else if (end != null) {
            return "fini le " + end.toString();
        } else {
            return "la palge n'est pas set";
        }
    }

}
