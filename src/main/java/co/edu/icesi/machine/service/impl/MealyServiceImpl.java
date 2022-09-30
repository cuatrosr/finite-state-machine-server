package co.edu.icesi.machine.service.impl;

import co.edu.icesi.machine.model.*;
import co.edu.icesi.machine.service.MealyService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealyServiceImpl implements MealyService {

    /**
     * Return Mealy's machine with inaccessible states removed
     * @param mealy the moore machine to relate
     * @return the mealy machine related without inaccessible states
     */

    @Override
    public Mealy relatedMachine(@RequestBody Mealy mealy) {
        String initialState = mealy.getInitialState();
        List<String> states = getStatesWithoutInitial(mealy, initialState);
        searchStatesFromInitial(states, initialState, mealy);
        states.forEach(mealy::removeRelationsState);
        return mealy;
    }

    /**
     * Returns the minimized Mealy's machine
     @param mealy the moore machine to minimize
     @return the mealy machine minimized
     */

    @Override
    public Mealy minimumMachine(Mealy mealy) {
        mealy = relatedMachine(mealy);
        List<List<MealyStates>> partition = partitionMealy(mealy);
        return createNewMealy(partition, mealy);
    }

    /**
     * Base method that calls the recursive method that performs the partitioning of the Mealy machine
     * @param mealy Mealy machine to be partitioned
     * @return Set of state blocks of the Mealy machine representing the final partition
     */

    private List<List<MealyStates>> partitionMealy(Mealy mealy) {
        return getPartitions(getInitialPartition(mealy));
    }

    /**
     * Recursive method that implements the partitioning algorithm,
     * reviewing the outputs of each state and grouping them together.
     * @param partition Set of state blocks of the Mealy machine representing each one of the partitions
     * @return the set of state blocks of the Mealy machine representing the partition in each interaction.
     */

    private List<List<MealyStates>> getPartitions(List<List<MealyStates>> partition) {
        List<List<MealyStates>> newPartition = partitionedMachine(partition);
        if (!newPartition.equals(partition))
            return getPartitions(newPartition);
        return newPartition;
    }

    /**
     *
     * @param mealy Mealy's machine from which you want to obtain the input alphabet.
     * @return
     */

    private List<List<String>> getStimulus(Mealy mealy) {
        List<List<String>> stimulus = new ArrayList<>();
        mealy.getStates().forEach(states -> {
            List<String> temp = new ArrayList<>();
            states.getStates().forEach(s -> temp.add(s.getResponse()));
            if (!stimulus.contains(temp))
                stimulus.add(temp);
        });
        return stimulus;
    }

    /**
     * Returns the list of inputs or stimuli received by the states of the Mealy machine.
     * @param mealyStates Initial state of the Mealy machine state list
     * @return the list of inputs of the Mealy machine.
     */

    private List<String> getStimulus(MealyStates mealyStates) {
        List<String> stimulus = new ArrayList<>();
        mealyStates.getStates().forEach(s -> stimulus.add(s.getResponse()));
        return stimulus;
    }

    /**
     * Returns Mealy's machine with the first partition according to the outputs of the states
     * @param mealy Mealy machine to be partitioned
     * @return Set of state blocks of the Mealy machine representing the initial partition
     */

    private List<List<MealyStates>> getInitialPartition(Mealy mealy) {
        List<List<MealyStates>> partition = new ArrayList<>();
        List<List<String>> stimulus = getStimulus(mealy);
        stimulus.forEach(s -> {
            List<MealyStates> cluster = new ArrayList<>();
            mealy.getStates().forEach(state -> {
                if (getStimulus(state).equals(s))
                    cluster.add(state);
            });
            if (!cluster.isEmpty())
                partition.add(cluster);
        });
        return partition;
    }

    /**
     *
     * @param partition
     * @return
     */

    private List<List<MealyStates>> partitionedMachine(List<List<MealyStates>> partition) {
        List<List<MealyStates>> newPartition = new ArrayList<>();
        for (List<MealyStates> list : partition) {
            List<List<MealyStates>> groups = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getStates().size(); j++) {
                    MealyState a = list.get(0).getStates().get(j);
                    MealyState b = list.get(i).getStates().get(j);
                    if (!searchGroups(a, b, partition)) {
                        if (groups.isEmpty()) {
                            groups.add(new ArrayList<>(Collections.nCopies(1, list.get(i))));
                        } else {
                            for (List<MealyStates> group : groups) {
                                boolean added = false;
                                for (int k = 0; k < group.size() && !added; k++) {
                                    for (int l = 0; l < group.get(k).getStates().size() && !added; l++) {
                                        MealyState c = list.get(i).getStates().get(l);
                                        MealyState d = group.get(k).getStates().get(l);
                                        if (searchGroups(c, d, partition)) {
                                            group.add(list.get(i));
                                            added = true;
                                        }
                                    }
                                }
                                if (added)
                                    break;
                            }
                        }
                        break;
                    }
                }
            }
            List<MealyStates> temp = new ArrayList<>(list);
            groups.forEach(temp::removeAll);
            newPartition.add(temp);
            newPartition.addAll(groups);
        }
        return newPartition;
    }

    /**
     *
     * @param a
     * @param b
     * @param partition
     * @return
     */

    private boolean searchGroups(MealyState a, MealyState b, List<List<MealyStates>> partition) {
        for (List<MealyStates> list : partition) {
            if (list.stream().anyMatch(e -> e.getRoot().equals(a.getState())) &&
                    list.stream().anyMatch(e -> e.getRoot().equals(b.getState())))
                return true;
        }
        return false;
    }

    /**
     *
     * @param states
     * @param actualState
     * @param mealy
     */

    private void searchStatesFromInitial(List<String> states, String actualState, Mealy mealy) {
        List<MealyState> relations = mealy.searchRelationsStates(actualState);
        relations.forEach(relation -> {
            if (states.remove(relation.getState()))
                searchStatesFromInitial(states, relation.getState(), mealy);
        });
    }

    /**
     *
     * @param mealy
     * @param initialState
     * @return
     */

    private List<String> getStatesWithoutInitial(Mealy mealy, String initialState) {
        List<String> states = mealy.getStates().stream().map(MealyStates::getRoot).collect(Collectors.toList());
        states.remove(initialState);
        return states;
    }

    /**
     *
     * @param partition
     * @param mealy
     * @return
     */

    private Mealy createNewMealy(List<List<MealyStates>> partition, Mealy mealy) {
        String prefix = "q";
        int count = 0;
        List<String> names = new ArrayList<>();
        for (List<MealyStates> ignored : partition) {
            names.add(prefix + count++);
        }
        List<MealyStates> states = new ArrayList<>();
        for (int i = 0; i < partition.size(); i++) {
            List<MealyState> sets = new ArrayList<>();
            for (int j = 0; j < partition.get(i).get(0).getStates().size(); j++) {
                int aux = 0;
                for (List<MealyStates> s : partition) {
                    int finalI = i;
                    int finalJ = j;
                    if (s.stream().anyMatch(e -> e.getRoot().contains(partition.get(finalI).get(0).getStates().get(finalJ).getState()))) {
                        sets.add(MealyState.builder().state(names.get(aux)).response(partition.get(finalI).get(0).getStates().get(finalJ).getResponse()).build());
                        break;
                    }
                    aux++;
                }
            }
            states.add(MealyStates.builder().root(names.get(i)).states(sets).build());
        }
        return Mealy.builder().initialState(names.get(0)).stimulus(mealy.getStimulus()).states(states).build();
    }
}
