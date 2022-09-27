package co.edu.icesi.machine.controller;

import co.edu.icesi.machine.api.MealyMachineAPI;
import co.edu.icesi.machine.dto.MealyDTO;
import co.edu.icesi.machine.mapper.MachineMapper;
import co.edu.icesi.machine.service.MealyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MealyMachineController implements MealyMachineAPI {

    private final MealyService mealyService;
    private final MachineMapper machineMapper;

    @Override
    public MealyDTO relatedMachine(MealyDTO mealyDTO) {
        return machineMapper.fromMealy(mealyService.relatedMachine(machineMapper.fromMealyDTO(mealyDTO)));
    }

    @Override
    public MealyDTO minimumMachine(MealyDTO mealyDTO) {
        return machineMapper.fromMealy(mealyService.minimumMachine(machineMapper.fromMealyDTO(mealyDTO)));
    }
}
