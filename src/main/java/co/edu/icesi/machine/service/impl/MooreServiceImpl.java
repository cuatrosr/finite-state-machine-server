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

    @Override
    public Moore minimumMachine(Moore moore) {
        moore = relatedMachine(moore);
        List<String> responses = getResponses(moore);
        List<List<MooreState>> partition = partitionMoore(moore, responses);
        return createNewMoore(partition, moore);
    }

    private List<String> getResponses(Moore moore) {
        List<String> res = new ArrayList<>();
        moore.getStates().forEach(s -> {
            if (!res.contains(s.getResponse()))
                res.add(s.getResponse());
        });
        return res;
    }

    private List<List<MooreState>> partitionMoore(Moore moore, List<String> responses) {
        return getPartitions(getInitialPartition(moore, responses));
    }

    private List<List<MooreState>> getPartitions(List<List<MooreState>> partition) {
        List<List<MooreState>> newPartition = partitionedMachine(partition);
        if (!newPartition.equals(partition))
            return getPartitions(newPartition);
        return newPartition;
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

    private List<List<MooreState>> getInitialPartition(Moore moore, List<String> responses) {
        List<List<MooreState>> partition = new ArrayList<>();
        responses.forEach(res -> {
                    List<MooreState> cluster = new ArrayList<>();
                    moore.getStates().forEach(state -> {
                        if (state.getResponse().equals(res))
                            cluster.add(state);
                    });
                    if (!cluster.isEmpty())
                        partition.add(cluster);
                }
        );
        return partition;
    }

    private List<List<MooreState>> partitionedMachine(List<List<MooreState>> partition) {
        List<List<MooreState>> newPartition = new ArrayList<>();
        for (List<MooreState> list : partition) {
            List<List<MooreState>> groups = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getStates().size(); j++) {
                    String a = list.get(0).getStates().get(j);
                    String b = list.get(i).getStates().get(j);
                    if (!searchGroups(a, b, partition)) {
                        if (groups.isEmpty()) {
                            groups.add(new ArrayList<>(Collections.nCopies(1, list.get(i))));
                        } else {
                            for (List<MooreState> group : groups) {
                                boolean added = false;
                                for (int k = 0; k < group.size() && !added; k++) {
                                    for (int l = 0; l < group.get(k).getStates().size() && !added; l++) {
                                        String c = list.get(i).getStates().get(l);
                                        String d = group.get(k).getStates().get(l);
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
            List<MooreState> temp = new ArrayList<>(list);
            groups.forEach(temp::removeAll);
            newPartition.add(temp);
            newPartition.addAll(groups);
        }
        return newPartition;
    }

    private boolean searchGroups(String a, String b, List<List<MooreState>> partition) {
        for (List<MooreState> list : partition) {
            if (list.stream().anyMatch(e -> e.getRoot().equals(a)) &&
                    list.stream().anyMatch(e -> e.getRoot().equals(b)))
                return true;
        }
        return false;
    }

    private Moore createNewMoore(List<List<MooreState>> partition, Moore moore) {
        String prefix = "q";
        int count = 0;
        List<String> names = new ArrayList<>();
        for (List<MooreState> ignored : partition) {
            names.add(prefix + count++);
        }
        List<MooreState> states = new ArrayList<>();
        for (int i = 0; i < partition.size(); i++) {
            List<String> sets = new ArrayList<>();
            for (int j = 0; j < partition.get(i).get(0).getStates().size(); j++) {
                int aux = 0;
                for (List<MooreState> s : partition) {
                    int finalI = i;
                    int finalJ = j;
                    if (s.stream().anyMatch(e -> e.getRoot().contains(partition.get(finalI).get(0).getStates().get(finalJ)))) {
                        sets.add(names.get(aux));
                        break;
                    }
                    aux++;
                }
            }
            states.add(MooreState.builder().root(names.get(i)).response(partition.get(i).get(0).getResponse()).states(sets).build());
        }
        return Moore.builder().initialState(names.get(0)).stimulus(moore.getStimulus()).states(states).build();
    }
}
