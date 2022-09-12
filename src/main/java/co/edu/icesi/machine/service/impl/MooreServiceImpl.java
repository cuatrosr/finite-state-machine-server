package co.edu.icesi.machine.service.impl;

import co.edu.icesi.machine.dto.EquivalentMooreDTO;
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
        List<List<MooreState>> partition = partitionMoore(moore);
        return createNewMoore(partition, moore);
    }

    @Override
    public EquivalentMooreDTO equivalentMachine(EquivalentMooreDTO mooreDTO, List<List<String>> states) {
        Moore tempMoore = Moore.builder().stimulus(mooreDTO.getStimulus()).states(mooreDTO.getStates()).build();
        List<List<MooreState>> partition = partitionMoore(tempMoore);
        if (searchGroups(mooreDTO.getInitialStates().get(0), mooreDTO.getInitialStates().get(1), partition)) {
            for (List<MooreState> groups : partition) {
                if (groups.stream().noneMatch(e -> states.get(0).contains(e.getRoot())) ||
                        groups.stream().noneMatch(e -> states.get(1).contains(e.getRoot()))) {
                    mooreDTO.setResponse(false);
                    return mooreDTO;
                }
            }
            mooreDTO.setResponse(true);
            return mooreDTO;
        }
        mooreDTO.setResponse(false);
        return mooreDTO;
    }

    private List<List<MooreState>> partitionMoore(Moore moore) {
        List<List<MooreState>> partition = getInitialPartition(moore);
        List<List<MooreState>> newPartition;
        do {
            newPartition = partitionedMachine(partition);
            if (!newPartition.equals(partition)) {
                partition = newPartition;
            } else {
                break;
            }
        } while (true);
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

    private List<List<MooreState>> getInitialPartition(Moore moore) {
        List<List<MooreState>> partition = new ArrayList<>();
        moore.getStimulus().forEach(stimulus -> {
                    List<MooreState> cluster = new ArrayList<>();
                    moore.getStates().forEach(state -> {
                        if (state.getResponse().equals(stimulus))
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
