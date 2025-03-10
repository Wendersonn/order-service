package sboot.service.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderSummaryDTO implements Serializable {
    private Long codigo;
    private List<ProductSummary> produtos;
    private BigDecimal totalAmount;

    public OrderSummaryDTO() {}

    public OrderSummaryDTO(Long codigo, List<ProductSummary> produtos, BigDecimal totalAmount) {
        this.codigo = codigo;
        this.produtos = produtos;
        this.totalAmount = totalAmount;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public List<ProductSummary> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProductSummary> produtos) {
        this.produtos = produtos;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Classe interna para representar os produtos dentro do resumo
    public static class ProductSummary {
        private Long codigo;
        private String nome;
        private int quantidade;
        private BigDecimal precoUnitario;

        public ProductSummary() {}

        public ProductSummary(Long codigo, String nome, int quantidade, BigDecimal precoUnitario) {
            this.codigo = codigo;
            this.nome = nome;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
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

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public BigDecimal getPrecoUnitario() {
            return precoUnitario;
        }

        public void setPrecoUnitario(BigDecimal precoUnitario) {
            this.precoUnitario = precoUnitario;
        }
    }
}
