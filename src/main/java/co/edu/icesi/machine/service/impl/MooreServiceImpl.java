package co.edu.icesi.machine.service.impl;

import co.edu.icesi.machine.model.Moore;
import co.edu.icesi.machine.model.MooreState;
import co.edu.icesi.machine.service.MooreService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MooreServiceImpl implements MooreService {

    @Override
    public Moore relatedMachine(Moore moore) {
        String initialState = moore.getInitialState();
        List<String> states = getStatesWithoutInitial(moore, initialState);
        searchStatesFromInitial(states, initialState, moore);
        states.forEach(moore::removeRelationsState);
        return moore;
    }

    private void searchStatesFromInitial(List<String> states, String actualState, Moore moore) {
        List<String> relations = moore.searchRelationsStates(actualState);
        relations.forEach(relation -> {if (states.remove(relation)) {
            searchStatesFromInitial(states, relation, moore);
        }});
    }

    private List<String> getStatesWithoutInitial(Moore moore, String initialState) {
        List<String> states = moore.getStates().stream().map(MooreState::getRoot).collect(Collectors.toList());
        states.remove(initialState);
        return states;
    }
}
