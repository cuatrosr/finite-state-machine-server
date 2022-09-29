package co.edu.icesi.machine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class Mealy {
    private String initialState;
    private List<String> stimulus;
    private List<MealyStates> states;

    /**
     * Returns the list of states of a Mealy machine that are inaccessible
     * @param from state from which the search for inaccessible states is being started
     * @return the list of inaccessible states
     */

    public List<MealyState> searchRelationsStates(String from) {
        return Objects.requireNonNull(states.stream().filter(e -> e.getRoot().equals(from)).findFirst().orElse(null)).getStates();
    }

    /**
     * Delete the inaccessible states from Mealy machine
     * @param state inaccessible state to remove
     */

    public void removeRelationsState(String state) {
        states.remove(states.stream().filter(e -> e.getRoot().equals(state)).findFirst().orElse(null));
    }
}
