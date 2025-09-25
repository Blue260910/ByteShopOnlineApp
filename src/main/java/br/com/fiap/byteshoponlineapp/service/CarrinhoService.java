package br.com.fiap.byteshoponlineapp.service;

import br.com.fiap.byteshoponlineapp.domain.Carrinho;
import br.com.fiap.byteshoponlineapp.domain.CarrinhoRepository;
import br.com.fiap.byteshoponlineapp.domain.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CarrinhoService {
    @Autowired
    private CarrinhoRepository carrinhoRepository;

    public Optional<Carrinho> buscarPorId(Long id) {
        return carrinhoRepository.findById(id);
    }

    public Carrinho criarCarrinho(Cliente cliente) {
        if (carrinhoRepository.findByClienteId(cliente.getId()).isPresent()) {
            throw new IllegalArgumentException("Cliente já possui carrinho ativo");
        }
        Carrinho carrinho = new Carrinho();
        carrinho.setCliente(cliente);
        return carrinhoRepository.save(carrinho);
    }

    public void excluirCarrinho(Long id) {
        Carrinho carrinho = carrinhoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));
        carrinhoRepository.delete(carrinho);
    }

    public br.com.fiap.byteshoponlineapp.domain.ItemCarrinho adicionarItem(Long carrinhoId, br.com.fiap.byteshoponlineapp.domain.Produto produto, Integer quantidade) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
            .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));
        br.com.fiap.byteshoponlineapp.domain.ItemCarrinho item = new br.com.fiap.byteshoponlineapp.domain.ItemCarrinho();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setCarrinho(carrinho);
        carrinho.getItens().add(item);
        carrinhoRepository.save(carrinho);
        return item;
    }

    public br.com.fiap.byteshoponlineapp.domain.ItemCarrinho atualizarQuantidade(Long carrinhoId, Long itemId, Integer quantidade) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
            .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));
        br.com.fiap.byteshoponlineapp.domain.ItemCarrinho item = carrinho.getItens().stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Item não encontrado"));
        item.setQuantidade(quantidade);
        carrinhoRepository.save(carrinho);
        return item;
    }

    public void removerItem(Long carrinhoId, Long itemId) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
            .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));
        carrinho.getItens().removeIf(i -> i.getId().equals(itemId));
        carrinhoRepository.save(carrinho);
    }

    public java.util.List<br.com.fiap.byteshoponlineapp.domain.ItemCarrinho> listarItens(Long carrinhoId) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
            .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));
        return carrinho.getItens();
    }
}
