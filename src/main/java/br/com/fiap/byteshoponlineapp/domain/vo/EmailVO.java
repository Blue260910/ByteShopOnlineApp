package br.com.fiap.byteshoponlineapp.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;



@Embeddable
public class EmailVO {

    // @Column personaliza a coluna gerada na tabela de Cliente
    @Column(name = "email", length = 150, nullable = false, unique = true)
    @jakarta.validation.constraints.Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    private String value;

    /** Construtor padrão exigido pelo JPA */
    protected EmailVO() {}

    public EmailVO(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
