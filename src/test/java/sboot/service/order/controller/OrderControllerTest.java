package sboot.service.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sboot.service.order.dto.OrderDTO;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(OrderControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void createOrder_ShouldReturnCreatedOrder() throws Exception {
//        OrderDTO orderDTO = new OrderDTO("Test Product", "Smart_TV", BigDecimal.ONE, 2, BigDecimal.valueOf(2400));
//        logger.info("Testando criação de pedido com produto: {}", orderDTO.getProduct());
//
//        mockMvc.perform(post("/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.product").value("Test Product"))
//                .andExpect(jsonPath("$.calculatedPrice").value(240.00));
//
//        logger.info("Teste de criação de pedido concluído com sucesso.");
//    }

}

