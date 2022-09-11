package co.edu.icesi.machine.api;

import co.edu.icesi.machine.dto.MooreDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/machine/moore")
public interface MooreMachineAPI {

    @PostMapping()
    MooreDTO relatedMachine(@RequestBody MooreDTO machineDTO);
}
