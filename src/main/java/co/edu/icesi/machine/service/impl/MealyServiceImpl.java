package co.edu.icesi.machine.service.impl;

import co.edu.icesi.machine.model.*;
import co.edu.icesi.machine.service.MealyService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealyServiceImpl implements MealyService {

    @Override
    public Mealy relatedMachine(@RequestBody Mealy mealy) {
        String initialState = mealy.getInitialState();
        List<String> states = getStatesWithoutInitial(mealy, initialState);
        searchStatesFromInitial(states, initialState, mealy);
        states.forEach(mealy::removeRelationsState);
        return mealy;
    }

    private void searchStatesFromInitial(List<String> states, String actualState, Mealy mealy) {
        List<MealyState> relations = mealy.searchRelationsStates(actualState);
        relations.forEach(relation -> {
            if (states.remove(relation.getState()))
                searchStatesFromInitial(states, relation.getState(), mealy);
        });
    }

    private List<String> getStatesWithoutInitial(Mealy mealy, String initialState) {
        List<String> states = mealy.getStates().stream().map(MealyStates::getRoot).collect(Collectors.toList());
        states.remove(initialState);
        return states;
    }
}
