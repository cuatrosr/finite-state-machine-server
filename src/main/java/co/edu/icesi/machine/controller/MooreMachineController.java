package co.edu.icesi.machine.controller;

import co.edu.icesi.machine.api.MooreMachineAPI;
import co.edu.icesi.machine.dto.EquivalentMooreDTO;
import co.edu.icesi.machine.dto.MooreDTO;
import co.edu.icesi.machine.mapper.MooreMapper;
import co.edu.icesi.machine.model.MooreState;
import co.edu.icesi.machine.service.MooreService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class MooreMachineController implements MooreMachineAPI {

    public final MooreService mooreService;
    public final MooreMapper mooreMapper;

    @Override
    public MooreDTO relatedMachine(MooreDTO mooreDTO) {
        return mooreMapper.fromMoore(mooreService.relatedMachine(mooreMapper.fromDTO(mooreDTO)));
    }

    @Override
    public MooreDTO minimumMachine(MooreDTO mooreDTO) {
        return mooreMapper.fromMoore(mooreService.minimumMachine(mooreMapper.fromDTO(mooreDTO)));
    }

    @Override
    public EquivalentMooreDTO equivalentMachine(List<MooreDTO> mooreDTOList) {
        int lengthFirst = mooreDTOList.get(0).getStates().size();
        mooreDTOList.get(1).setInitialState("q" + lengthFirst);
        for (MooreState item:mooreDTOList.get(1).getStates()) {
            item.setRoot("q" + (Integer.parseInt(item.getRoot().substring(1)) + lengthFirst));
            List<String> newStates = new ArrayList<>();
            for (String states:item.getStates()) {
                newStates.add("q" + (Integer.parseInt(states.substring(1)) + lengthFirst));
            }
            item.setStates(newStates);
        }
        List<MooreState> states = new ArrayList<>();
        states.addAll(mooreDTOList.get(0).getStates());
        states.addAll(mooreDTOList.get(1).getStates());
        List<String> rootFirst = mooreDTOList.get(0).getStates().stream().map(MooreState::getRoot).collect(Collectors.toList());
        List<String> rootSecond = mooreDTOList.get(1).getStates().stream().map(MooreState::getRoot).collect(Collectors.toList());
        EquivalentMooreDTO equivalentMooreDTO = EquivalentMooreDTO.builder().initialStates(Arrays.asList(mooreDTOList.get(0).getInitialState(), mooreDTOList.get(1).getInitialState())).stimulus(mooreDTOList.get(0).getStimulus()).states(states).build();
        return mooreService.equivalentMachine(equivalentMooreDTO, Arrays.asList(rootFirst, rootSecond));
    }
}
