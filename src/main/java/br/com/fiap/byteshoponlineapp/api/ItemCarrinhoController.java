package br.com.fiap.byteshoponlineapp.api;

import br.com.fiap.byteshoponlineapp.domain.Carrinho;
import br.com.fiap.byteshoponlineapp.domain.ItemCarrinho;
import br.com.fiap.byteshoponlineapp.domain.Produto;
import br.com.fiap.byteshoponlineapp.domain.ProdutoRepository;
import br.com.fiap.byteshoponlineapp.domain.dto.ItemCarrinhoRequest;
import br.com.fiap.byteshoponlineapp.domain.dto.ItemCarrinhoResponse;
import br.com.fiap.byteshoponlineapp.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carrinhos/{carrinhoId}/itens")
public class ItemCarrinhoController {
    @Autowired
    private CarrinhoService carrinhoService;
    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    public ResponseEntity<ItemCarrinhoResponse> adicionarItem(@PathVariable Long carrinhoId, @RequestBody ItemCarrinhoRequest request) {
        if (request.getQuantidade() == null || request.getQuantidade() < 1) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Quantidade inválida");
        }
        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Produto não encontrado"));
            if (!produto.isAtivo()) {
                throw new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "Produto inativo");
        }
        ItemCarrinho item = carrinhoService.adicionarItem(carrinhoId, produto, request.getQuantidade());
        return ResponseEntity.status(201).body(toResponse(item));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemCarrinhoResponse> atualizarQuantidade(@PathVariable Long carrinhoId, @PathVariable Long itemId, @RequestBody ItemCarrinhoRequest request) {
        if (request.getQuantidade() == null || request.getQuantidade() < 1) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Quantidade inválida");
        }
        ItemCarrinho item = carrinhoService.atualizarQuantidade(carrinhoId, itemId, request.getQuantidade());
        return ResponseEntity.ok(toResponse(item));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removerItem(@PathVariable Long carrinhoId, @PathVariable Long itemId) {
        carrinhoService.removerItem(carrinhoId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ItemCarrinhoResponse>> listarItens(@PathVariable Long carrinhoId) {
        List<ItemCarrinho> itens = carrinhoService.listarItens(carrinhoId);
        List<ItemCarrinhoResponse> resposta = itens.stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(resposta);
    }

    private ItemCarrinhoResponse toResponse(ItemCarrinho item) {
        ItemCarrinhoResponse dto = new ItemCarrinhoResponse();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setNomeProduto(item.getProduto().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getProduto().getPreco().doubleValue());
        dto.setSubtotal(item.getProduto().getPreco().multiply(java.math.BigDecimal.valueOf(item.getQuantidade())).doubleValue());
        return dto;
    }
}
