package ir.maktab.finalprojectphase3.mapper;


import ir.maktab.finalprojectphase3.data.dto.ServiceDTO;
import ir.maktab.finalprojectphase3.data.model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    Service dtoToModel(ServiceDTO serviceDTO);

    ServiceDTO modelToDto(Service service);
}