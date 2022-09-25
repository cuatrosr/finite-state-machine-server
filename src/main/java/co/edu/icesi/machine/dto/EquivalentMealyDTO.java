package co.edu.icesi.machine.dto;

import co.edu.icesi.machine.model.MealyStates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EquivalentMealyDTO {
    private boolean response;
    private List<String> initialStates;
    private List<String> stimulus;
    private List<MealyStates> states;
}