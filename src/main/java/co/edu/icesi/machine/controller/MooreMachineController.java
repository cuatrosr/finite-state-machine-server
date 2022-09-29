package co.edu.icesi.machine.controller;

import co.edu.icesi.machine.api.MooreMachineAPI;
import co.edu.icesi.machine.dto.MooreDTO;
import co.edu.icesi.machine.mapper.MachineMapper;
import co.edu.icesi.machine.service.MooreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MooreMachineController implements MooreMachineAPI {

    private final MooreService mooreService;
    private final MachineMapper machineMapper;

    /**
     * Return Moore's machine with inaccessible states removed
    @param mooreDTO the moore machine in Data Transfer Object
    @return the moore DTO with the moore machine
     */

    @Override
    public MooreDTO relatedMachine(MooreDTO mooreDTO) {
        return machineMapper.fromMoore(mooreService.relatedMachine(machineMapper.fromMooreDTO(mooreDTO)));
    }

    /**
     * Returns the minimized Moore's machine, by means of other auxiliary methods such as partitioning
     * that are in the minimization algorithm.
    @param mooreDTO the moore machine to minimize in Data Transfer Object
    @return the moore DTO with the moore machine minimized
     */

    @Override
    public MooreDTO minimumMachine(MooreDTO mooreDTO) {
        return machineMapper.fromMoore(mooreService.minimumMachine(machineMapper.fromMooreDTO(mooreDTO)));
    }
}