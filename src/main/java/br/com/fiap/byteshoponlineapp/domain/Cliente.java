package br.com.fiap.byteshoponlineapp.domain;

import br.com.fiap.byteshoponlineapp.domain.vo.DocumentoVO;
import br.com.fiap.byteshoponlineapp.domain.vo.EmailVO;
import jakarta.persistence.*;

/**
 * @Entity indica que a classe é persistida pelo JPA (vira uma tabela).
 * @Table permite customizar o nome da tabela.
 */

@Entity
@Table(name = "cliente")
public class Cliente {

    /**
     * @Id marca a PK.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) faz o auto-incremento.
     * (No H2/MySQL, IDENTITY é a estratégia mais simples para ensinar).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** @Column define restrições/atributos da coluna (opcional). */
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    /**
     * @Embedded coloca os campos do VO como colunas na mesma tabela.
     * Aqui, as colunas virão de Email.value e Documento.value.
     */
    @Embedded
    private EmailVO email;

    @Embedded
    private DocumentoVO documento;

    /** Construtor padrão exigido pelo JPA */
    protected Cliente() {}

    public Cliente(String nome, EmailVO email, DocumentoVO documento) {
        this.nome = nome;
        this.email = email;
        this.documento = documento;
    }

    // Getters (mantemos sem setters para encorajar imutabilidade controlada)
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public EmailVO getEmail() { return email; }
    public DocumentoVO getDocumento() { return documento; }

    // Atualizações controladas (úteis no PUT)
    public void atualizarDados(String novoNome, EmailVO novoEmail, DocumentoVO novoDocumento) {
        if (novoNome != null && !novoNome.isBlank()) this.nome = novoNome;
        if (novoEmail != null) this.email = novoEmail;
        if (novoDocumento != null) this.documento = novoDocumento;
    }
}
