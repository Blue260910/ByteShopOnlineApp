package br.com.fiap.byteshoponlineapp.service;
import br.com.fiap.byteshoponlineapp.domain.ItemPedido;

import br.com.fiap.byteshoponlineapp.domain.Pedido;
import br.com.fiap.byteshoponlineapp.domain.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        pedidoRepository.delete(pedido);
    }
    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> listarPorStatus(String status) {
        return pedidoRepository.findByStatus(status);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Autowired
    private br.com.fiap.byteshoponlineapp.domain.CarrinhoRepository carrinhoRepository;

    public Pedido criarPedido(br.com.fiap.byteshoponlineapp.domain.dto.PedidoCreateRequest request) {
        // Busca o carrinho pelo id
        br.com.fiap.byteshoponlineapp.domain.Carrinho carrinho = carrinhoRepository.findById(request.getCarrinhoId())
            .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));
        Pedido pedido = new Pedido();
        pedido.setCliente(carrinho.getCliente());
        Double total = carrinho.getTotal();
        if (total == null) {
            total = carrinho.getItens() != null ? carrinho.getItens().stream()
                .mapToDouble(item -> item.getProduto().getPreco().doubleValue() * item.getQuantidade())
                .sum() : 0.0;
        }
        pedido.setTotal(total);
        pedido.setStatus("CRIADO");
        // Copiar itens do carrinho para pedido
        if (carrinho.getItens() != null) {
            java.util.List<ItemPedido> itensPedido = new java.util.ArrayList<>();
            for (br.com.fiap.byteshoponlineapp.domain.ItemCarrinho itemCarrinho : carrinho.getItens()) {
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setProduto(itemCarrinho.getProduto());
                itemPedido.setQuantidade(itemCarrinho.getQuantidade());
                itemPedido.setPrecoUnitario(itemCarrinho.getProduto().getPreco().doubleValue());
                itemPedido.setSubtotal(itemCarrinho.getProduto().getPreco().doubleValue() * itemCarrinho.getQuantidade());
                itemPedido.setPedido(pedido);
                itensPedido.add(itemPedido);
            }
            pedido.setItens(itensPedido);
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido atualizarStatus(Long id, String status) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }
}
