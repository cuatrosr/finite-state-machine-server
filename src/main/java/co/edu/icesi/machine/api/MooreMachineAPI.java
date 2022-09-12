package co.edu.icesi.machine.api;

import co.edu.icesi.machine.dto.EquivalentMooreDTO;
import co.edu.icesi.machine.dto.MooreDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/machine/moore")
public interface MooreMachineAPI {

    @PostMapping("/related")
    MooreDTO relatedMachine(@RequestBody MooreDTO mooreDTO);
    @PostMapping("/minimum")
    MooreDTO minimumMachine(@RequestBody MooreDTO mooreDTO);

    @PostMapping("/equivalent")
    EquivalentMooreDTO equivalentMachine(@RequestBody List<MooreDTO> mooreDTOList);
}
