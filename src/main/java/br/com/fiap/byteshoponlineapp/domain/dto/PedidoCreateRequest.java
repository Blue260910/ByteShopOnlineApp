package br.com.fiap.byteshoponlineapp.domain.dto;

public class PedidoCreateRequest {
    private Long carrinhoId;
    public Long getCarrinhoId() {
        return carrinhoId;
    }
    public void setCarrinhoId(Long carrinhoId) {
        this.carrinhoId = carrinhoId;
    }
}
