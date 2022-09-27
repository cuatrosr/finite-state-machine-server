package co.edu.icesi.machine.api;

import co.edu.icesi.machine.dto.MealyDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/machine/mealy")
public interface MealyMachineAPI {

    @PostMapping("/related")
    MealyDTO relatedMachine(@RequestBody MealyDTO mealyDTO);

    @PostMapping("/minimum")
    MealyDTO minimumMachine(@RequestBody MealyDTO mealyDTO);
}
