package co.edu.icesi.machine.service;

import co.edu.icesi.machine.dto.EquivalentMealyDTO;
import co.edu.icesi.machine.model.Mealy;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MealyService {

    Mealy relatedMachine(@RequestBody Mealy mealy);

    Mealy minimumMachine(@RequestBody Mealy mealy);

    EquivalentMealyDTO equivalentMachine(EquivalentMealyDTO mealyDTO, List<List<String>> states);
}
