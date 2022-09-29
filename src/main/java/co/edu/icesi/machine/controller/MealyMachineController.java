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

    /**
     * Return Mealy's machine with inaccessible states removed
     @param mealyDTO the moore machine in Data Transfer Object
     @return the mealy DTO with the mealy machine without inaccessible states
     */

    @Override
    public MealyDTO relatedMachine(MealyDTO mealyDTO) {
        return machineMapper.fromMealy(mealyService.relatedMachine(machineMapper.fromMealyDTO(mealyDTO)));
    }

    /**
     * Returns the minimized Mealy's machine, by means of other auxiliary methods such as partitioning
     * that are in the minimization algorithm.
     @param mealyDTO the mealy machine to minimize in Data Transfer Object
     @return the mealy DTO with the mealy machine minimized
     */

    @Override
    public MealyDTO minimumMachine(MealyDTO mealyDTO) {
        return machineMapper.fromMealy(mealyService.minimumMachine(machineMapper.fromMealyDTO(mealyDTO)));
    }
}
