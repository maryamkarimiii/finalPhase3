package ir.maktab.finalprojectphase3.mapper;

import ir.maktab.finalprojectphase3.data.dto.AdminDTO;
import ir.maktab.finalprojectphase3.data.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    Admin dtoToModel(AdminDTO adminDTO);

    @Mapping(target = "password", expression = ("java(null)"))
    AdminDTO modelToDto(Admin admin);
}
