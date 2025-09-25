package br.com.fiap.byteshoponlineapp.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByCategoriaAndAtivoTrue(String categoria);
    List<Produto> findByAtivoTrue();
    Optional<Produto> findByNome(String nome);
}
