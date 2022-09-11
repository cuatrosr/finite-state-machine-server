package co.edu.icesi.machine.controller;

import co.edu.icesi.machine.api.MooreMachineAPI;
import co.edu.icesi.machine.dto.MooreDTO;
import co.edu.icesi.machine.mapper.MooreMapper;
import co.edu.icesi.machine.service.MooreService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MooreMachineController implements MooreMachineAPI {

    public final MooreService mooreService;
    public final MooreMapper mooreMapper;

    @Override
    public MooreDTO relatedMachine(MooreDTO machineDTO) {
        System.out.println(mooreService.relatedMachine(mooreMapper.fromDTO(machineDTO)));
        return null;
    }
}
