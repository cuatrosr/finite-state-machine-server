package co.edu.icesi.machine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MooreState {
    private String root;
    private String response;
    private List<String> states;
}
