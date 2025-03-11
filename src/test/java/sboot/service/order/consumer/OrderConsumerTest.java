package sboot.service.order.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import sboot.service.order.config.exceptions.OrderException;
import sboot.service.order.dto.OrderDTO;
import sboot.service.order.dto.ProductDTO;
import sboot.service.order.entities.Order;
import sboot.service.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderConsumerTest {

    @InjectMocks
    private OrderConsumer orderConsumer;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @Mock
    private Acknowledgment acknowledgment;

    private OrderDTO sampleOrder;

    @BeforeEach
    void setup() {
        sampleOrder = new OrderDTO(
                123L,
                Collections.singletonList(new ProductDTO(456L, "Produto Teste", BigDecimal.TEN, 2)),
                BigDecimal.valueOf(20)
        );
    }

    @Test
    void shouldProcessOrderSuccessfully() {

        when(orderRepository.findByCodigo(sampleOrder.getCodigo())).thenReturn(Optional.empty());

        orderConsumer.processOrder(sampleOrder, acknowledgment);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(kafkaTemplate, times(1)).send(eq("processed-orders-topic"), any(OrderDTO.class));
        verify(acknowledgment, times(1)).acknowledge();
    }


    @Test
    void shouldIgnoreDuplicateOrder() {
        ConsumerRecord<String, OrderDTO> record = mock(ConsumerRecord.class);

        when(orderRepository.findByCodigo(sampleOrder.getCodigo())).thenReturn(Optional.of(new Order()));

        OrderException thrownException = assertThrows(OrderException.class, () -> {
            orderConsumer.processOrder(sampleOrder, acknowledgment);
        });
        assertEquals("Pedido: 123 já existe", thrownException.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
        verify(kafkaTemplate, never()).send(anyString(), any(OrderDTO.class));
        verify(acknowledgment, never()).acknowledge();
    }

}
