package co.edu.icesi.machine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MooreState {
    private String root;
    private String response;
    private List<String> states;
}
