package co.edu.icesi.machine.dto;

import co.edu.icesi.machine.model.MealyStates;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MealyDTO {
    private String initialState;
    private List<String> stimulus;
    private List<MealyStates> states;
}
