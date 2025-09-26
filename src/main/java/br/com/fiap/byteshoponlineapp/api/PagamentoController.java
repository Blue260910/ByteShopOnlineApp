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
    private br.com.fiap.byteshoponlineapp.domain.PagamentoRepository pagamentoRepository;
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
        // Verifica se já existe pagamento para o pedido
        if (pagamentoRepository.findByPedidoId(pedido.getId()).isPresent()) {
            throw new IllegalArgumentException("Já existe pagamento para este pedido");
        }
        // Valida método
        String metodo = pagamentoRequest.getMetodo();
        if (!("PIX".equals(metodo) || "CARTAO".equals(metodo) || "BOLETO".equals(metodo))) {
            throw new IllegalArgumentException("Método de pagamento inválido. Permitidos: PIX, CARTAO, BOLETO");
        }
        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValor(pagamentoRequest.getValor());
        pagamento.setMetodo(metodo);
        Pagamento salvo = pagamentoService.criarPagamento(pagamento);
        return ResponseEntity.status(201).body(toResponse(salvo));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PagamentoResponse> atualizarStatus(@PathVariable Long id, @RequestBody StatusUpdate statusUpdate) {
        Pagamento atualizado = pagamentoService.atualizarStatus(id, statusUpdate.getStatus());
        return ResponseEntity.ok(toResponse(atualizado));
    }

    public static class StatusUpdate {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
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
