package sboot.service.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sboot.service.order.config.exceptions.OrderException;
import sboot.service.order.dto.OrderDTO;
import sboot.service.order.service.OrderService;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletionException;


@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Value(value = "${app.kafka.consumer.topics}")
    private String topic;

    @Autowired
    private OrderService orderService;

    @Autowired
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        logger.info("Enviando pedido para o tópico Kafka: {}", orderDTO);
        try {
            kafkaTemplate.send(topic, orderDTO).whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Erro ao enviar pedido para o Kafka", ex);
                    throw new CompletionException(ex);
                } else {
                    logger.info("Pedido enviado com sucesso para o Kafka: {}", result.getRecordMetadata());
                }
            }).join();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Pedido enviado para processamento");

        } catch (CompletionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar pedido.", e.getCause());
        }
    }



    @GetMapping
    public List<OrderDTO> getAllOrders() {
        logger.info("Buscando todos os pedidos");
        return orderService.getAllOrders();
    }
}
