package br.com.fiap.byteshoponlineapp.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmailValue(String email);  // verifica e-mail já usado (coluna do VO)
}
