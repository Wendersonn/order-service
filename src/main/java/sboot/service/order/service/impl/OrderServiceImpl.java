package sboot.service.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sboot.service.order.dto.OrderDTO;
import sboot.service.order.dto.ProductDTO;
import sboot.service.order.entities.Order;
import sboot.service.order.repository.OrderRepository;
import sboot.service.order.repository.ProductRepository;
import sboot.service.order.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;


    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDTO(order.getCodigo(),
                        order.getProdutos().stream()
                                .map(p -> new ProductDTO(p.getCodigo(), p.getNome(), p.getPrecoUnitario(), p.getQuantidade()))
                                .collect(Collectors.toList()),
                        order.getProdutos().stream()
                                .map(p -> p.getPrecoUnitario().multiply(BigDecimal.valueOf(p.getQuantidade())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add))) // Soma total dos produtos
                .collect(Collectors.toList());
    }



}
