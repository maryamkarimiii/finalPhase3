package ir.maktab.finalprojectphase3.mapper;

import ir.maktab.finalprojectphase3.data.dto.UserCreationDTO;
import ir.maktab.finalprojectphase3.data.dto.UserDTO;
import ir.maktab.finalprojectphase3.data.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer dtoToModel(UserCreationDTO userCreationDTO);

    @Mapping(target = "credit", expression = "java(customer.getWallet().getCredit())")
    UserDTO modelToDto(Customer customer);
}
