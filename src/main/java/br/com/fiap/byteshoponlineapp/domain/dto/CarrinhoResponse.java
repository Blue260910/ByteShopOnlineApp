package br.com.fiap.byteshoponlineapp.domain.dto;

import java.util.List;

public class CarrinhoResponse {
    private Long id;
    private Long clienteId;
    private Double total;
    private List<ItemCarrinhoResponse> itens;
    // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
        public Double getTotal() { return total; }
        public void setTotal(Double total) { this.total = total; }
        public List<ItemCarrinhoResponse> getItens() { return itens; }
        public void setItens(List<ItemCarrinhoResponse> itens) { this.itens = itens; }
}
