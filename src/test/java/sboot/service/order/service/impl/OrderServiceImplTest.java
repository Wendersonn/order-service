package sboot.service.order.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sboot.service.order.dto.OrderDTO;
import sboot.service.order.dto.ProductDTO;
import sboot.service.order.entities.Order;
import sboot.service.order.entities.Produtos;
import sboot.service.order.repository.OrderRepository;
import sboot.service.order.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private Produtos produtos;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setCodigo(1L);

        produtos = new Produtos();
        produtos.setCodigo(1L);
        produtos.setNome("Produto Teste");
        produtos.setPrecoUnitario(new BigDecimal("10.0"));
        produtos.setQuantidade(2);

        order.setProdutos(List.of(produtos));
    }

    @Test
    void shouldSaveOrder() {
        orderService.saveOrder(order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void shouldReturnAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDTO> orders = orderService.getAllOrders();

        assertFalse(orders.isEmpty(), "A lista de pedidos não deve estar vazia");
        assertEquals(1, orders.size(), "Deve haver um único pedido na lista");
        assertEquals(1L, orders.get(0).getCodigo(), "O código do pedido deve ser 1");

        // Verificando total calculado corretamente
        BigDecimal expectedTotal = produtos.getPrecoUnitario().multiply(BigDecimal.valueOf(produtos.getQuantidade()));
        assertEquals(expectedTotal, orders.get(0).getValorTotalPedido(), "O total do pedido deve ser correto");

        // Verificando produtos dentro do pedido
        List<ProductDTO> products = orders.get(0).getProdutos();
        assertEquals(1, products.size(), "O pedido deve conter um produto");
        assertEquals(produtos.getCodigo(), products.get(0).getCodigo(), "O código do produto deve ser 1");
    }
}
