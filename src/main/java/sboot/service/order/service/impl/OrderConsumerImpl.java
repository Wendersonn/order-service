package sboot.service.order.service.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sboot.service.order.dto.OrderSummaryDTO;
import sboot.service.order.entities.Order;
import sboot.service.order.repository.OrderRepository;
import sboot.service.order.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class OrderConsumerImpl {
    private static final Logger logger = LoggerFactory.getLogger(OrderConsumerImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, OrderSummaryDTO> kafkaTemplate;

    private static final String PROCESSED_ORDER_TOPIC = "processed-orders-topic";

    @KafkaListener(topics = "order-topic", groupId = "order-group")
    public void consume(ConsumerRecord<String, Order> record) {
    }

    private void publishOrderSummary(Order order) {
        OrderSummaryDTO summary = new OrderSummaryDTO(
                order.getCodigo(),
                order.getProdutos().stream()
                        .map(p -> new OrderSummaryDTO.ProductSummary(
                                p.getCodigo(),
                                p.getNome(),
                                p.getQuantidade(),
                                p.getPrecoUnitario()
                        ))
                        .collect(Collectors.toList()),
                order.getValorTotalPedido()
        );

        kafkaTemplate.send(PROCESSED_ORDER_TOPIC, summary);
        logger.info("Resumo enviado para o Kafka: {}", summary);
    }

    private BigDecimal calculateTotalAmount(Order order) {
        return order.getProdutos().stream()
                .map(p -> p.getPrecoUnitario().multiply(BigDecimal.valueOf(p.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
