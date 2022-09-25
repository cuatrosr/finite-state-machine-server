package co.edu.icesi.machine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MealyStates {
    private String root;
    private List<MealyState> states;
}
