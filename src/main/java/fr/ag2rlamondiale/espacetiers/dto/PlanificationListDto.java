package fr.ag2rlamondiale.espacetiers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanificationListDto {
    private List<Planification> values;
}
