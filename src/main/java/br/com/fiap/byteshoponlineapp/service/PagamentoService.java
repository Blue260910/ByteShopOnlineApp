package br.com.fiap.byteshoponlineapp.service;

import br.com.fiap.byteshoponlineapp.domain.Pagamento;
import br.com.fiap.byteshoponlineapp.domain.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    public Optional<Pagamento> buscarPorId(Long id) {
        return pagamentoRepository.findById(id);
    }

    public Pagamento criarPagamento(Pagamento pagamento) {
        // Validação do método
        if (!(pagamento.getMetodo().equals("PIX") || pagamento.getMetodo().equals("CARTAO") || pagamento.getMetodo().equals("BOLETO"))) {
            throw new IllegalArgumentException("Método de pagamento inválido");
        }
        // Validação do valor
        if (pagamento.getPedido() == null || pagamento.getValor() == null || pagamento.getPedido().getTotal() == null) {
            throw new IllegalArgumentException("Pedido ou valor não informado");
        }
        if (!pagamento.getValor().equals(pagamento.getPedido().getTotal())) {
            throw new IllegalArgumentException("Valor do pagamento diferente do total do pedido");
        }
        pagamento.setStatus("PENDENTE");
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento atualizarStatus(Long id, String status) {
        Pagamento pagamento = pagamentoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado"));
        pagamento.setStatus(status);
        return pagamentoRepository.save(pagamento);
    }
}
