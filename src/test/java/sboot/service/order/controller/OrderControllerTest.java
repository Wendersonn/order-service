package sboot.service.order.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import sboot.service.order.dto.OrderDTO;
import sboot.service.order.dto.ProductDTO;
import sboot.service.order.service.OrderService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    private OrderDTO mockOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockOrder = new OrderDTO();
        mockOrder.setProdutos(List.of(new ProductDTO()));
        mockOrder.setCodigo(1L);

        ReflectionTestUtils.setField(orderController, "topic", "test-topic");
    }

    @Test
    void testCreateOrder() {
        // Criando um mock para SendResult
        SendResult<String, OrderDTO> sendResultMock = mock(SendResult.class);

        // Criando um CompletableFuture para simular envio assíncrono bem-sucedido
        CompletableFuture<SendResult<String, OrderDTO>> futureMock = new CompletableFuture<>();
        futureMock.complete(sendResultMock); // Simula sucesso imediato

        // Garantindo que kafkaTemplate.send() retorna um CompletableFuture válido
        when(kafkaTemplate.send(anyString(), any(OrderDTO.class))).thenReturn(futureMock);

        ResponseEntity<String> response = orderController.createOrder(mockOrder);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Pedido enviado para processamento", response.getBody());

        // Verifica se o método do Kafka foi chamado corretamente
        verify(kafkaTemplate, times(1)).send(anyString(), any(OrderDTO.class));
    }

    @Test
    void shouldReturn500IfKafkaFails() {
        CompletableFuture<SendResult<String, OrderDTO>> futureMock = new CompletableFuture<>();
        futureMock.completeExceptionally(new RuntimeException("Kafka failure"));

        when(kafkaTemplate.send(anyString(), any(OrderDTO.class))).thenReturn(futureMock);
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> orderController.createOrder(mockOrder)
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Erro ao processar pedido"));

    }
    @Test
    void testGetAllOrders() {
        List<OrderDTO> mockOrders = Arrays.asList(mockOrder, new OrderDTO());
        when(orderService.getAllOrders()).thenReturn(mockOrders);

        List<OrderDTO> orders = orderController.getAllOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());

        verify(orderService, times(1)).getAllOrders();
    }
}
