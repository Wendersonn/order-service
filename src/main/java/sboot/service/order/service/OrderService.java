package sboot.service.order.service;

import sboot.service.order.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders();
}
