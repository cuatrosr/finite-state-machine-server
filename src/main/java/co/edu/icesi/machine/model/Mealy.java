package co.edu.icesi.machine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Mealy {
    private String initialState;
    private List<String> stimulus;
    private List<MealyStates> states;
}
