package co.edu.icesi.machine.mapper;

import co.edu.icesi.machine.dto.MealyDTO;
import co.edu.icesi.machine.dto.MooreDTO;
import co.edu.icesi.machine.model.Mealy;
import co.edu.icesi.machine.model.Moore;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MachineMapper {

    Moore fromMooreDTO(MooreDTO mooreDTO);
    MooreDTO fromMoore(Moore moore);
    MealyDTO fromMealy(Mealy mealy);
    Mealy fromMealyDTO(MealyDTO mealyDTO);
}
