package br.com.fiap.byteshoponlineapp.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByClienteIdAndItensIsNotNull(Long clienteId);
    Optional<Carrinho> findByClienteId(Long clienteId);
}
