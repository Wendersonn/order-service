package sboot.service.order.service;

import sboot.service.order.dto.OrderDTO;
import sboot.service.order.entities.Order;

import java.util.List;

public interface OrderService {

    void saveOrder(Order order);

    List<OrderDTO> getAllOrders();
}
