package sboot.service.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sboot.service.order.dto.OrderDTO;
import sboot.service.order.entities.Order;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    OrderDTO orderToOrderDTO(Order order);

    Order orderDTOtoOrder(OrderDTO orderDTO);
}
