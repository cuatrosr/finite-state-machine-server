package co.edu.icesi.machine.service;

import co.edu.icesi.machine.model.Mealy;
import org.springframework.web.bind.annotation.RequestBody;

public interface MealyService {

    Mealy relatedMachine(@RequestBody Mealy mealy);
}
