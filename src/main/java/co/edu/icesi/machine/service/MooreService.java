package co.edu.icesi.machine.service;

import co.edu.icesi.machine.model.Moore;
import org.springframework.web.bind.annotation.RequestBody;

public interface MooreService {

    Moore relatedMachine(@RequestBody Moore moore);
}
