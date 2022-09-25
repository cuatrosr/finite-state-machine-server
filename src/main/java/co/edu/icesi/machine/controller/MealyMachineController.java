package co.edu.icesi.machine.controller;

import co.edu.icesi.machine.api.MealyMachineAPI;
import co.edu.icesi.machine.dto.EquivalentMealyDTO;
import co.edu.icesi.machine.dto.MealyDTO;
import co.edu.icesi.machine.mapper.MachineMapper;
import co.edu.icesi.machine.model.MealyState;
import co.edu.icesi.machine.model.MealyStates;
import co.edu.icesi.machine.service.MealyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public EquivalentMealyDTO equivalentMachine(List<MealyDTO> mealyDTOList) {
        renameStates(mealyDTOList);
        List<MealyStates> states = groupStatesByMachine(mealyDTOList);
        List<List<String>> roots = groupRoots(mealyDTOList);
        List<String> initialStates = groupInitialStates(mealyDTOList);
        EquivalentMealyDTO equivalentMooreDTO = EquivalentMealyDTO.builder().initialStates(initialStates).stimulus(mealyDTOList.get(0).getStimulus()).states(states).build();
        return mealyService.equivalentMachine(equivalentMooreDTO, roots);
    }

    private List<String> groupInitialStates(List<MealyDTO> mealyDTOList) {
        return Arrays.asList(mealyDTOList.get(0).getInitialState(), mealyDTOList.get(1).getInitialState());
    }

    private List<List<String>> groupRoots(List<MealyDTO> mealyDTOList) {
        List<String> rootFirst = mealyDTOList.get(0).getStates().stream().map(MealyStates::getRoot).collect(Collectors.toList());
        List<String> rootSecond = mealyDTOList.get(1).getStates().stream().map(MealyStates::getRoot).collect(Collectors.toList());
        return Arrays.asList(rootFirst, rootSecond);
    }

    private List<MealyStates> groupStatesByMachine(List<MealyDTO> mealyDTOList) {
        List<MealyStates> states = new ArrayList<>();
        states.addAll(mealyDTOList.get(0).getStates());
        states.addAll(mealyDTOList.get(1).getStates());
        return states;
    }

    private void renameStates(List<MealyDTO> mealyDTOList) {
        int lengthFirst = mealyDTOList.get(0).getStates().size();
        mealyDTOList.get(1).setInitialState("q" + lengthFirst);
        for (MealyStates item : mealyDTOList.get(1).getStates()) {
            item.setRoot("q" + (Integer.parseInt(item.getRoot().substring(1)) + lengthFirst));
            List<MealyState> newStates = new ArrayList<>();
            for (MealyState states : item.getStates()) {
                newStates.add(MealyState.builder().state("q" + (Integer.parseInt(states.getState().substring(1)) + lengthFirst)).response(states.getResponse()).build());
            }
            item.setStates(newStates);
        }
    }
}
