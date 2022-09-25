package co.edu.icesi.machine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MealyStates {
    private String root;
    private List<MealyState> states;
}
