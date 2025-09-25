package br.com.fiap.byteshoponlineapp.api;

import br.com.fiap.byteshoponlineapp.domain.Pedido;
import br.com.fiap.byteshoponlineapp.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<br.com.fiap.byteshoponlineapp.domain.dto.PedidoResponse>> listarPorCliente(@RequestParam(required = false) Long clienteId,
                                                        @RequestParam(required = false) String status) {
        List<Pedido> pedidos;
        if (clienteId != null) {
            pedidos = pedidoService.listarPorCliente(clienteId);
        } else if (status != null) {
            pedidos = pedidoService.listarPorStatus(status);
        } else {
            pedidos = pedidoService.listarPorStatus("CRIADO");
        }
        List<br.com.fiap.byteshoponlineapp.domain.dto.PedidoResponse> resposta = pedidos.stream().map(this::toResponse).toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<br.com.fiap.byteshoponlineapp.domain.dto.PedidoResponse> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(p -> ResponseEntity.ok(toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<br.com.fiap.byteshoponlineapp.domain.dto.PedidoResponse> criar(@RequestBody br.com.fiap.byteshoponlineapp.domain.dto.PedidoCreateRequest request) {
        Pedido salvo = pedidoService.criarPedido(request);
        return ResponseEntity.status(201).body(toResponse(salvo));
    }

    public static class StatusUpdate {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<br.com.fiap.byteshoponlineapp.domain.dto.PedidoResponse> atualizarStatus(@PathVariable Long id, @RequestBody StatusUpdate req) {
        Pedido atualizado = pedidoService.atualizarStatus(id, req.getStatus());
        return ResponseEntity.ok(toResponse(atualizado));
    }

    private br.com.fiap.byteshoponlineapp.domain.dto.PedidoResponse toResponse(Pedido pedido) {
        br.com.fiap.byteshoponlineapp.domain.dto.PedidoResponse dto = new br.com.fiap.byteshoponlineapp.domain.dto.PedidoResponse();
        dto.setId(pedido.getId());
        dto.setClienteId(pedido.getCliente() != null ? pedido.getCliente().getId() : null);
        dto.setTotal(pedido.getTotal());
        dto.setStatus(pedido.getStatus());
        // Mapear itens do pedido para ItemPedidoResponse
        if (pedido.getItens() != null) {
            java.util.List<br.com.fiap.byteshoponlineapp.domain.dto.ItemPedidoResponse> itensDto = pedido.getItens().stream().map(item -> {
                br.com.fiap.byteshoponlineapp.domain.dto.ItemPedidoResponse dtoItem = new br.com.fiap.byteshoponlineapp.domain.dto.ItemPedidoResponse();
                dtoItem.setId(item.getId());
                dtoItem.setProdutoId(item.getProduto() != null ? item.getProduto().getId() : null);
                dtoItem.setNomeProduto(item.getProduto() != null ? item.getProduto().getNome() : null);
                dtoItem.setQuantidade(item.getQuantidade());
                dtoItem.setPrecoUnitario(item.getPrecoUnitario());
                dtoItem.setSubtotal(item.getSubtotal());
                return dtoItem;
            }).toList();
            dto.setItens(itensDto);
        }
        // dto.setPagamento(...)
        return dto;
    }
}
