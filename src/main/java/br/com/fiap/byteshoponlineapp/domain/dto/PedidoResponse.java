package br.com.fiap.byteshoponlineapp.domain.dto;

import java.util.List;

public class PedidoResponse {
    private Long id;
    private Long clienteId;
    private Double total;
    private String status;
    private List<ItemPedidoResponse> itens;
    private PagamentoResponse pagamento;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getClienteId() {
        return clienteId;
    }
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<ItemPedidoResponse> getItens() {
        return itens;
    }
    public void setItens(List<ItemPedidoResponse> itens) {
        this.itens = itens;
    }
    public PagamentoResponse getPagamento() {
        return pagamento;
    }
    public void setPagamento(PagamentoResponse pagamento) {
        this.pagamento = pagamento;
    }
}
