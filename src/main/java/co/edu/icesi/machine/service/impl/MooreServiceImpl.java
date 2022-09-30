package co.edu.icesi.machine.service.impl;

import co.edu.icesi.machine.model.Moore;
import co.edu.icesi.machine.model.MooreState;
import co.edu.icesi.machine.service.MooreService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MooreServiceImpl implements MooreService {

    /**
     * Return Moore's machine with inaccessible states removed
     * @param moore the moore machine to relate
     * @return the moore machine related without inaccessible states
     */

    @Override
    public Moore relatedMachine(Moore moore) {
        String initialState = moore.getInitialState();
        List<String> states = getStatesWithoutInitial(moore, initialState);
        searchStatesFromInitial(states, initialState, moore);
        states.forEach(moore::removeRelationsState);
        return moore;
    }

    /**
     * Returns the minimized Moore's machine
     @param moore the moore machine to minimize
     @return the moore machine minimized
     */

    @Override
    public Moore minimumMachine(Moore moore) {
        moore = relatedMachine(moore);
        List<String> responses = getResponses(moore);
        List<List<MooreState>> partition = partitionMoore(moore, responses);
        return createNewMoore(partition, moore);
    }

    /**
     * Gets the set of state's outputs of the moore machine
     * @param moore moore machine from which the set of outputs is to be obtained
     * @return a list with the set of state's outputs of the moore machine
     */

    private List<String> getResponses(Moore moore) {
        List<String> res = new ArrayList<>();
        moore.getStates().forEach(s -> {
            if (!res.contains(s.getResponse()))
                res.add(s.getResponse());
        });
        return res;
    }

    /**
     * Base method that calls the recursive method that performs the partitioning of the Moore machine
     * @param moore Moore machine to be partitioned
     * @return Set of state blocks of the Moore machine representing the final partition
     */

    private List<List<MooreState>> partitionMoore(Moore moore, List<String> responses) {
        return getPartitions(getInitialPartition(moore, responses));
    }

    /**
     * Recursive method that implements the partitioning algorithm,
     * reviewing the outputs of each state and grouping them together.
     * @param partition Set of state blocks of the Moore machine representing each one of the partitions
     * @return the set of state blocks of the Moore machine representing the partition in each interaction.
     */

    private List<List<MooreState>> getPartitions(List<List<MooreState>> partition) {
        List<List<MooreState>> newPartition = partitionedMachine(partition);
        if (!newPartition.equals(partition))
            return getPartitions(newPartition);
        return newPartition;
    }

    /**
     * Search the relations between the Moore's machine states.
     * @param states set of states of the Moore's machine.
     * @param actualState actual state
     * @param moore Moore's machine.
     */

    private void searchStatesFromInitial(List<String> states, String actualState, Moore moore) {
        List<String> relations = moore.searchRelationsStates(actualState);
        relations.forEach(relation -> {
            if (states.remove(relation))
                searchStatesFromInitial(states, relation, moore);
        });
    }

    /**
     * Get the set o states of a moore machine without the initial state
     * @param moore Moore machine
     * @param initialState the initial's state value of the Moore machine
     * @return the states of a Moore machine without the initial state
     */

    private List<String> getStatesWithoutInitial(Moore moore, String initialState) {
        List<String> states = moore.getStates().stream().map(MooreState::getRoot).collect(Collectors.toList());
        states.remove(initialState);
        return states;
    }

    /**
     * Returns Moore's machine with the first partition according to the outputs of the states.
     * @param moore moore machine
     * @param responses outputs of each state of the moore machine.
     * @return Set of state blocks of the Mealy machine representing the initial partition.
     */

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

    /**
     * Partitioning algorithm
     * @param partition partition of the Moore's machine
     * @return set of Moore States's lists
     */

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

    /**
     *  Find the groups containing the initial states of each machine.
     * @param a list o Moore's states
     * @param b list o Moore's states
     * @param partition partition of the Moore's machine
     * @return a boolean value if the groups found have the initial states
     */

    private boolean searchGroups(String a, String b, List<List<MooreState>> partition) {
        for (List<MooreState> list : partition) {
            if (list.stream().anyMatch(e -> e.getRoot().equals(a)) &&
                    list.stream().anyMatch(e -> e.getRoot().equals(b)))
                return true;
        }
        return false;
    }

    /**
     * Create a new moore machine
     * @param partition
     * @param moore
     * @return the construction of the moore machine.
     */

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
