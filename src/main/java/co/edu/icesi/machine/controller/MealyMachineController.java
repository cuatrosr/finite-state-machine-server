package co.edu.icesi.machine.controller;

import co.edu.icesi.machine.api.MealyMachineAPI;
import co.edu.icesi.machine.dto.MealyDTO;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MealyMachineController implements MealyMachineAPI {

    @Override
    public MealyDTO createMachine(MealyDTO mealyDTO) {
        System.out.println(mealyDTO);
        return null;
    }
}
