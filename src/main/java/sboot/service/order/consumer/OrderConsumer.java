package sboot.service.order.consumer;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sboot.service.order.config.exceptions.OrderException;
import sboot.service.order.dto.OrderDTO;
import sboot.service.order.dto.ProductDTO;
import sboot.service.order.entities.Order;
import sboot.service.order.entities.Product;
import sboot.service.order.repository.OrderRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);


    private static final String PROCESSED_ORDER_TOPIC = "processed-orders-topic";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @Transactional
    @KafkaListener(topics = "order-topic", groupId = "order-group")
    public void processOrder(@Payload OrderDTO orderDTO, Acknowledgment acknowledgment) {
        try {
            logger.info("Processando pedido recebido do Kafka: {}", orderDTO);

            Order order = new Order(orderDTO.getCodigo(), orderDTO.getProdutos().stream().map(p -> {
                Product product = new Product();
                product.setCodigo(p.getCodigo());
                product.setNome(p.getNome());
                product.setPrecoUnitario(p.getPrecoUnitario());
                product.setQuantidade(p.getQuantidade());
                return product;
            }).collect(Collectors.toList()));

            order.getProdutos().forEach(product -> product.setOrder(order));
            Optional<Order> result =  orderRepository.findByCodigo(order.getCodigo());
            if (result.isPresent()){
                logger.error("Pedido: {}, já existe na base", orderDTO.getCodigo());
                throw new OrderException(String.format("Pedido: %s já existe" ,order.getCodigo()));
            }
            orderRepository.save(order);
            logger.info("Pedido salvo no banco de dados com total: {}", order.getValorTotalPedido());

            OrderDTO dto = new OrderDTO(
                    order.getCodigo(),
                    order.getProdutos().stream()
                            .map(p -> new ProductDTO(p.getCodigo(), p.getNome(), p.getPrecoUnitario(), p.getQuantidade()))
                            .collect(Collectors.toList()),
                    order.getValorTotalPedido()
            );

            kafkaTemplate.send(PROCESSED_ORDER_TOPIC, dto);
            logger.info("Pedido publicado no tópico {}", PROCESSED_ORDER_TOPIC);
            acknowledgment.acknowledge();

        } catch (Exception e) {
            logger.error("Erro ao processar pedido do Kafka: {}", e.getMessage(), e);
            throw new OrderException(e.getMessage());
        }
    }
}

