package co.edu.icesi.machine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MealyState {
    private String state;
    private String response;
}
