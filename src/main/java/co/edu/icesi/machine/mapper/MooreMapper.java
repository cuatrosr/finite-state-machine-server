package co.edu.icesi.machine.mapper;

import co.edu.icesi.machine.dto.MooreDTO;
import co.edu.icesi.machine.model.Moore;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MooreMapper {

    Moore fromDTO(MooreDTO machineDTO);
    MooreDTO fromMoore(Moore machineDTO);
}
