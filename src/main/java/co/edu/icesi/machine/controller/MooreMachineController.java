package co.edu.icesi.machine.controller;

import co.edu.icesi.machine.api.MooreMachineAPI;
import co.edu.icesi.machine.dto.EquivalentMooreDTO;
import co.edu.icesi.machine.dto.MooreDTO;
import co.edu.icesi.machine.mapper.MachineMapper;
import co.edu.icesi.machine.model.MooreState;
import co.edu.icesi.machine.service.MooreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MooreMachineController implements MooreMachineAPI {

    private final MooreService mooreService;
    private final MachineMapper machineMapper;

    @Override
    public MooreDTO relatedMachine(MooreDTO mooreDTO) {
        return machineMapper.fromMoore(mooreService.relatedMachine(machineMapper.fromMooreDTO(mooreDTO)));
    }

    @Override
    public MooreDTO minimumMachine(MooreDTO mooreDTO) {
        return machineMapper.fromMoore(mooreService.minimumMachine(machineMapper.fromMooreDTO(mooreDTO)));
    }

    @Override
    public EquivalentMooreDTO equivalentMachine(List<MooreDTO> mooreDTOList) {
        renameStates(mooreDTOList);
        List<MooreState> states = groupStatesByMachine(mooreDTOList);
        List<List<String>> roots = groupRoots(mooreDTOList);
        List<String> initialStates = groupInitialStates(mooreDTOList);
        EquivalentMooreDTO equivalentMooreDTO = EquivalentMooreDTO.builder().initialStates(initialStates).stimulus(mooreDTOList.get(0).getStimulus()).states(states).build();
        return mooreService.equivalentMachine(equivalentMooreDTO, roots);
    }

    private void renameStates(List<MooreDTO> mooreDTOList) {
        int lengthFirst = mooreDTOList.get(0).getStates().size();
        mooreDTOList.get(1).setInitialState("q" + lengthFirst);
        for (MooreState item : mooreDTOList.get(1).getStates()) {
            item.setRoot("q" + (Integer.parseInt(item.getRoot().substring(1)) + lengthFirst));
            List<String> newStates = new ArrayList<>();
            for (String states : item.getStates()) {
                newStates.add("q" + (Integer.parseInt(states.substring(1)) + lengthFirst));
            }
            item.setStates(newStates);
        }
    }

    private List<MooreState> groupStatesByMachine(List<MooreDTO> mooreDTOList) {
        List<MooreState> states = new ArrayList<>();
        states.addAll(mooreDTOList.get(0).getStates());
        states.addAll(mooreDTOList.get(1).getStates());
        return states;
    }

    private List<List<String>> groupRoots(List<MooreDTO> mooreDTOList) {
        List<String> rootFirst = mooreDTOList.get(0).getStates().stream().map(MooreState::getRoot).collect(Collectors.toList());
        List<String> rootSecond = mooreDTOList.get(1).getStates().stream().map(MooreState::getRoot).collect(Collectors.toList());
        return Arrays.asList(rootFirst, rootSecond);
    }

    private List<String> groupInitialStates(List<MooreDTO> mooreDTOList) {
        return Arrays.asList(mooreDTOList.get(0).getInitialState(), mooreDTOList.get(1).getInitialState());
    }
}