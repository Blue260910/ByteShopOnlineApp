package br.com.fiap.byteshoponlineapp.api;

import br.com.fiap.byteshoponlineapp.domain.Carrinho;
import br.com.fiap.byteshoponlineapp.api.dto.CarrinhoCreateRequest;
import br.com.fiap.byteshoponlineapp.domain.Cliente;
import br.com.fiap.byteshoponlineapp.domain.ClienteRepository;
import br.com.fiap.byteshoponlineapp.domain.dto.CarrinhoResponse;
import br.com.fiap.byteshoponlineapp.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinhos")
public class CarrinhoController {
    @Autowired
    private CarrinhoService carrinhoService;
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/{id}")
        public ResponseEntity<CarrinhoResponse> buscarPorId(@PathVariable Long id) {
            return carrinhoService.buscarPorId(id)
                    .map(carrinho -> ResponseEntity.ok(toResponse(carrinho)))
                    .orElse(ResponseEntity.notFound().build());
        }

        private CarrinhoResponse toResponse(Carrinho carrinho) {
            CarrinhoResponse dto = new CarrinhoResponse();
            dto.setId(carrinho.getId());
            dto.setClienteId(carrinho.getCliente() != null ? carrinho.getCliente().getId() : null);
            // Mapear itens para ItemCarrinhoResponse
            if (carrinho.getItens() != null && !carrinho.getItens().isEmpty()) {
                java.util.List<br.com.fiap.byteshoponlineapp.domain.dto.ItemCarrinhoResponse> itensDto = carrinho.getItens().stream().map(item -> {
                    br.com.fiap.byteshoponlineapp.domain.dto.ItemCarrinhoResponse dtoItem = new br.com.fiap.byteshoponlineapp.domain.dto.ItemCarrinhoResponse();
                    dtoItem.setId(item.getId());
                    dtoItem.setProdutoId(item.getProduto() != null ? item.getProduto().getId() : null);
                    dtoItem.setNomeProduto(item.getProduto() != null ? item.getProduto().getNome() : null);
                    dtoItem.setQuantidade(item.getQuantidade());
                    dtoItem.setPrecoUnitario(item.getPrecoUnitario());
                    dtoItem.setSubtotal(item.getSubtotal());
                    return dtoItem;
                }).toList();
                dto.setItens(itensDto);
                // Calcular total
                double total = itensDto.stream().mapToDouble(i -> i.getSubtotal() != null ? i.getSubtotal() : 0.0).sum();
                dto.setTotal(total);
            } else {
                dto.setItens(java.util.Collections.emptyList());
                dto.setTotal(0.0);
            }
            return dto;
    }

    @PostMapping
        public ResponseEntity<CarrinhoResponse> criar(@RequestBody CarrinhoCreateRequest request) {
            Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Cliente não encontrado"));
            try {
                Carrinho salvo = carrinhoService.criarCarrinho(cliente);
                return ResponseEntity.status(201).body(toResponse(salvo));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, e.getMessage());
            }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        carrinhoService.excluirCarrinho(id);
        return ResponseEntity.noContent().build();
    }
}
