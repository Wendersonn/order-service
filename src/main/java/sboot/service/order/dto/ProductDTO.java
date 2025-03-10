package sboot.service.order.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductDTO {

    @NotNull(message = "Código do produto não pode ser nulo")
    private Long codigo;

    @NotNull(message = "Nome do produto não pode ser nulo")
    private String nome;

    @NotNull(message = "Preço unitário não pode ser nulo")
    private BigDecimal precoUnitario;

    @NotNull(message = "Quantidade não pode ser nula")
    private int quantidade;

    public ProductDTO() {}

    public ProductDTO(Long codigo, String nome, BigDecimal precoUnitario, int quantidade) {
        this.codigo = codigo;
        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
