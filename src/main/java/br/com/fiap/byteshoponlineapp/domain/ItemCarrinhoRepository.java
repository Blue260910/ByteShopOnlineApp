package br.com.fiap.byteshoponlineapp.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {
    List<ItemCarrinho> findByCarrinhoId(Long carrinhoId);
}
