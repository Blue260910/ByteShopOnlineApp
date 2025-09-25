package br.com.fiap.byteshoponlineapp.api;


import br.com.fiap.byteshoponlineapp.api.dto.ClienteRequest;
import br.com.fiap.byteshoponlineapp.api.dto.ClienteResponse;
import br.com.fiap.byteshoponlineapp.domain.Cliente;
import br.com.fiap.byteshoponlineapp.domain.ClienteRepository;
import br.com.fiap.byteshoponlineapp.domain.vo.DocumentoVO;
import br.com.fiap.byteshoponlineapp.domain.vo.EmailVO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @RestController combina @Controller + @ResponseBody (JSON por padrão).
 * @RequestMapping define o prefixo da API.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    /** CREATE: POST /clientes */
        @PostMapping
        public ResponseEntity<ClienteResponse> criar(@RequestBody @Valid ClienteRequest body) {
            if (repository.existsByEmailValue(body.email())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já em uso");
            }

            Cliente cliente = new Cliente(
                    body.nome(),
                    new EmailVO(body.email()),   // VO embarcado
                    new DocumentoVO(body.documento())   // VO embarcado
            );

            Cliente salvo = repository.save(cliente);
            var location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(salvo.getId())
                    .toUri();
            return ResponseEntity.created(location).body(toResponse(salvo));
        }


    /** READ ALL: GET /clientes */
    @GetMapping
    public List<ClienteResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    /** READ ONE: GET /clientes/{id} */
    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        return toResponse(cliente);
    }


    /** UPDATE: PUT /clientes/{id} */
    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequest body) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // Atualiza campos via método de domínio
        cliente.atualizarDados(
                body.nome(),
                new EmailVO(body.email()),
                new DocumentoVO(body.documento())
        );

        Cliente salvo = repository.save(cliente);
        return toResponse(salvo);
    }


    /** Mapeia entidade -> DTO de resposta */
    private ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNome(),
                c.getEmail().getValue(),
                c.getDocumento().getValue()
        );
    }

    /** DELETE: DELETE /clientes/{id} */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente possui pedidos vinculados");
        }
    }
}
