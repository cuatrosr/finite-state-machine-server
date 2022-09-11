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
        relations.forEach(relation -> {
            if (states.remove(relation))
                searchStatesFromInitial(states, relation, moore);
        });
    }

    private List<String> getStatesWithoutInitial(Moore moore, String initialState) {
        List<String> states = moore.getStates().stream().map(MooreState::getRoot).collect(Collectors.toList());
        states.remove(initialState);
        return states;
    }

    public Moore minimumMachine(Moore moore) {
        List<List<MooreState>> partition = getInitialPartition(moore);
        for (List<MooreState> l:partition) {
            System.out.print(l.stream().map(MooreState::getRoot).collect(Collectors.toList()));
        }
        System.out.println();
        do {
            List<List<MooreState>> newPartition = partitionedMachine(partition, moore);
            for (List<MooreState> l:newPartition) {
                System.out.print(l.stream().map(MooreState::getRoot).collect(Collectors.toList()));
            }
            System.out.println();
            if (!newPartition.equals(partition)) {
                partition = newPartition;
            } else {
                break;
            }
        } while (true);
        return null;
    }

    private List<List<MooreState>> getInitialPartition(Moore moore) {
        List<List<MooreState>> partition = new ArrayList<>();
        moore.getStimulus().forEach(stimulus -> {
                    List<MooreState> cluster = new ArrayList<>();
                    moore.getStates().forEach(state -> {
                        if (state.getResponse().equals(stimulus))
                            cluster.add(state);
                    });
                    partition.add(cluster);
                }
        );
        return partition;
    }

    private List<List<MooreState>> partitionedMachine(List<List<MooreState>> partition, Moore moore) {
        List<List<MooreState>> newPartition = new ArrayList<>();
        for (List<MooreState> list:partition) {
            List<List<MooreState>> groups = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getStates().size(); j++) {
                    String a = list.get(0).getStates().get(j);
                    String b = list.get(i).getStates().get(j);
                    boolean adjunct = searchGroups(a, b, partition);
                    if (!adjunct) {
                        if (groups.isEmpty()) {
                            groups.add(new ArrayList<>(Collections.nCopies(1, list.get(i))));
                        } else {
                            for (List<MooreState> group : groups) {
                                boolean added = false;
                                for (int k = 0; k < group.size() && !added; k++) {
                                    for (int l = 0; l < group.get(k).getStates().size() && !added; l++) {
                                        String c = list.get(i).getStates().get(l);
                                        String d = group.get(k).getStates().get(l);
                                        boolean result = searchGroups(c, d, partition);
                                        if (result) {
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
            groups.forEach(list::removeAll);
            newPartition.add(list);
            newPartition.addAll(groups);
        }
        return newPartition;
    }

    private boolean searchGroups(String a, String b, List<List<MooreState>> partition) {
        for (List<MooreState> list:partition) {
            if (list.stream().anyMatch(e -> e.getRoot().equals(a)) &&
                    list.stream().anyMatch(e -> e.getRoot().equals(b)))
                return true;
        }
        return false;
    }
}
