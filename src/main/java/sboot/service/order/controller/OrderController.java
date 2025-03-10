package sboot.service.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import sboot.service.order.dto.OrderDTO;
import sboot.service.order.entities.Order;
import sboot.service.order.entities.Product;
import sboot.service.order.service.OrderService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        logger.info("Enviando pedido para o tópico Kafka: {}", orderDTO);
        kafkaTemplate.send("order-topic", orderDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Pedido enviado para processamento");
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        logger.info("Buscando todos os pedidos");
        return orderService.getAllOrders();
    }
}
