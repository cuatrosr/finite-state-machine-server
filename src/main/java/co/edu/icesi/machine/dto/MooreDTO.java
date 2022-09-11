package co.edu.icesi.machine.dto;

import co.edu.icesi.machine.model.MooreState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MooreDTO {
    private String initialState;
    private List<String> stimulus;
    private List<MooreState> states;
}
