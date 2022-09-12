package co.edu.icesi.machine.service;

import co.edu.icesi.machine.dto.EquivalentMooreDTO;
import co.edu.icesi.machine.model.Moore;
import co.edu.icesi.machine.model.MooreState;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MooreService {

    Moore relatedMachine(@RequestBody Moore moore);
    Moore minimumMachine(@RequestBody Moore moore);
    EquivalentMooreDTO equivalentMachine(@RequestBody EquivalentMooreDTO mooreList, @RequestBody List<List<String>> states);
}
