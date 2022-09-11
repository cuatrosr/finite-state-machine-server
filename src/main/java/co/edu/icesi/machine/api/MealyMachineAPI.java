package co.edu.icesi.machine.api;

import co.edu.icesi.machine.dto.MooreDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/machine/mealy")
public interface MealyMachineAPI {

    @PostMapping()
    MooreDTO createMachine(@RequestBody MooreDTO machineDTO);
}
