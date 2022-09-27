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

    @Override
    public MooreDTO relatedMachine(MooreDTO mooreDTO) {
        return machineMapper.fromMoore(mooreService.relatedMachine(machineMapper.fromMooreDTO(mooreDTO)));
    }

    @Override
    public MooreDTO minimumMachine(MooreDTO mooreDTO) {
        return machineMapper.fromMoore(mooreService.minimumMachine(machineMapper.fromMooreDTO(mooreDTO)));
    }
}