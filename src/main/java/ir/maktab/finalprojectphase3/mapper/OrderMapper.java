package ir.maktab.finalprojectphase3.mapper;

import ir.maktab.finalprojectphase3.data.dto.OrderCreationDTO;
import ir.maktab.finalprojectphase3.data.dto.OrderDTO;
import ir.maktab.finalprojectphase3.data.model.Order;
import lombok.experimental.FieldNameConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "subService.name", source = "subServiceName")
    Order dtoToModel(OrderCreationDTO orderCreationDTO);

    @Mapping(target = "subServiceName", source = "subService.name")
    @Mapping(target = "expertUsername", expression = "java(order.getExpert() != null ? order.getExpert().getUsername() : null)")
    @Mapping(target = "customerUserName", expression = "java(order.getCustomer().getUsername())")
    OrderDTO modelToDto(Order order);

    List<OrderDTO> orderListsToDto(List<Order> orderList);
}
