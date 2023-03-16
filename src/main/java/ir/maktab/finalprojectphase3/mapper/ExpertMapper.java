package ir.maktab.finalprojectphase3.mapper;

import ir.maktab.finalprojectphase3.data.dto.ExpertDTO;
import ir.maktab.finalprojectphase3.data.dto.SubServiceDTO;
import ir.maktab.finalprojectphase3.data.dto.UserCreationDTO;
import ir.maktab.finalprojectphase3.data.model.Expert;
import ir.maktab.finalprojectphase3.data.model.SubService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ExpertMapper {

    ExpertMapper INSTANCE = Mappers.getMapper(ExpertMapper.class);

    Expert dtoToModel(UserCreationDTO userCreationDTO);

    @Mapping(target = "expertCredit", expression = "java(expert.getWallet().getCredit())")
    ExpertDTO modelToDto(Expert expert);

    @Mapping(target = "serviceName", source = "service.name")
    SubServiceDTO modelToDto(SubService service);

    List<SubServiceDTO> subServiceListToDto(List<SubService> subServiceList);

    List<ExpertDTO> expertListsToDto(List<Expert> expertList);
}
