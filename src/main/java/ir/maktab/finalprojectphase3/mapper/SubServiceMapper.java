package ir.maktab.finalprojectphase3.mapper;

import ir.maktab.finalprojectphase3.data.dto.SubServiceDTO;
import ir.maktab.finalprojectphase3.data.model.SubService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubServiceMapper {

    SubServiceMapper INSTANCE = Mappers.getMapper(SubServiceMapper.class);

    @Mapping(target = "service.name", source = "serviceName")
    SubService dtoToModel(SubServiceDTO subServiceDTO);

    @Mapping(target = "serviceName", source = "service.name")
    SubServiceDTO modelToDto(SubService service);

    List<SubServiceDTO> subServiceListToDto(List<SubService> subServiceList);
}
