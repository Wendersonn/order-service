package sboot.service.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    @NotNull(message = "Código do pedido não pode ser nulo")
    private Long codigo;

    @NotNull(message = "Lista de produtos não pode ser nula")
    private List<ProductDTO> produtos;

    private BigDecimal valorTotalPedido;

    public OrderDTO() {}

    public OrderDTO(Long codigo, List<ProductDTO> produtos, BigDecimal valorTotalPedido) {
        this.codigo = codigo;
        this.produtos = produtos;
        this.valorTotalPedido = valorTotalPedido;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public List<ProductDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProductDTO> produtos) {
        this.produtos = produtos;
    }

    public BigDecimal getValorTotalPedido() {
        return valorTotalPedido;
    }

    public void setValorTotalPedido(BigDecimal valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }
}