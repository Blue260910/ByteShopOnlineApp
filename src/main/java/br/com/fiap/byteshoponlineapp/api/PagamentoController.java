package br.com.fiap.byteshoponlineapp.api;

import br.com.fiap.byteshoponlineapp.domain.Pagamento;
import br.com.fiap.byteshoponlineapp.domain.Pedido;
import br.com.fiap.byteshoponlineapp.domain.dto.PagamentoRequest;
import br.com.fiap.byteshoponlineapp.domain.dto.PagamentoResponse;
import br.com.fiap.byteshoponlineapp.service.PagamentoService;
import br.com.fiap.byteshoponlineapp.domain.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
    @Autowired
    private PagamentoService pagamentoService;
    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagamentoResponse> criar(@RequestBody PagamentoRequest pagamentoRequest) {
        Pedido pedido = pedidoRepository.findById(pagamentoRequest.getPedidoId())
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValor(pagamentoRequest.getValor());
        pagamento.setMetodo(pagamentoRequest.getMetodo());
        Pagamento salvo = pagamentoService.criarPagamento(pagamento);
        return ResponseEntity.status(201).body(toResponse(salvo));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PagamentoResponse> atualizarStatus(@PathVariable Long id, @RequestBody String status) {
        Pagamento atualizado = pagamentoService.atualizarStatus(id, status);
        return ResponseEntity.ok(toResponse(atualizado));
    }

    private PagamentoResponse toResponse(Pagamento pagamento) {
        PagamentoResponse dto = new PagamentoResponse();
        dto.setId(pagamento.getId());
        dto.setPedidoId(pagamento.getPedido() != null ? pagamento.getPedido().getId() : null);
        dto.setValor(pagamento.getValor());
        dto.setMetodo(pagamento.getMetodo());
        dto.setStatus(pagamento.getStatus());
        return dto;
    }
}
