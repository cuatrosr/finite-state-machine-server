package co.edu.icesi.machine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Mealy {
    private String initialState;
    private List<String> stimulus;
    private List<MealyStates> states;

    public List<MealyState> searchRelationsStates(String from) {
        return Objects.requireNonNull(states.stream().filter(e -> e.getRoot().equals(from)).findFirst().orElse(null)).getStates();
    }

    public void removeRelationsState(String state) {
        states.remove(states.stream().filter(e -> e.getRoot().equals(state)).findFirst().orElse(null));
    }
}
