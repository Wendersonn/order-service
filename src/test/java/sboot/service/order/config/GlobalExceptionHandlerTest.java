package sboot.service.order.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import sboot.service.order.config.exceptions.ErrorResponse;
import sboot.service.order.config.exceptions.OrderException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldHandleIllegalStateException() {
        IllegalStateException exception = new IllegalStateException("Estado inválido");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalStateException(exception);

        assertNotNull(response);
        assertEquals(409, response.getStatusCode().value());
        assertEquals("Estado inválido", response.getBody().getMessage());
    }

    @Test
    void shouldHandleOrderException() {
        OrderException exception = new OrderException("Erro na ordem");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleOrderException(exception);

        assertNotNull(response);
        assertEquals(422, response.getStatusCode().value());
        assertEquals("UNPROCESSABLE_ENTITY", response.getBody().getMessage());
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new Exception("Erro interno inesperado");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(500, response.getStatusCode().value());
        assertEquals("Erro interno no servidor", response.getBody().getMessage());
    }
}
