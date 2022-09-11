package co.edu.icesi.machine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class Moore {
    private String initialState;
    private List<String> stimulus;
    private List<MooreState> states;

    public List<String> searchRelationsStates(String from) {
        return Objects.requireNonNull(states.stream().filter(e -> e.getRoot().equals(from)).findFirst().orElse(null)).getStates();
    }

    public void removeRelationsState(String state) {
        states.remove(states.stream().filter(e -> e.getRoot().equals(state)).findFirst().orElse(null));
    }
}
