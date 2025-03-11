package sboot.service.order.consumer;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sboot.service.order.config.exceptions.OrderException;
import sboot.service.order.dto.OrderDTO;
import sboot.service.order.dto.ProductDTO;
import sboot.service.order.entities.Order;
import sboot.service.order.entities.Produtos;
import sboot.service.order.repository.OrderRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @Value("${app.kafka.producer.topics:default-topic}")
    private String topic;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @Transactional
    @KafkaListener(topics = "${app.kafka.consumer.topics}", groupId = "${app.kafka.consumer.group}")
    public void processOrder(@Payload OrderDTO orderDTO, Acknowledgment acknowledgment) {
        try {
            logger.info("Processando pedido recebido do Kafka: {}", orderDTO);

            Order order = new Order(orderDTO.getCodigo(), orderDTO.getProdutos().stream().map(p -> {
                Produtos produtos = new Produtos();
                produtos.setCodigo(p.getCodigo());
                produtos.setNome(p.getNome());
                produtos.setPrecoUnitario(p.getPrecoUnitario());
                produtos.setQuantidade(p.getQuantidade());
                return produtos;
            }).collect(Collectors.toList()));

            order.getProdutos().forEach(produtos -> produtos.setOrder(order));
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

            kafkaTemplate.send(topic, dto);
            logger.info("Pedido publicado no tópico {}", topic);
            acknowledgment.acknowledge();

        } catch (Exception e) {
            logger.error("Erro ao processar pedido do Kafka: {}", e.getMessage(), e);
            throw new OrderException(e.getMessage());
        }
    }
}

