package br.com.fiap.byteshoponlineapp.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;


@Embeddable
public class DocumentoVO {

    @Column(name = "documento", length = 30, nullable = false)
    @NotBlank(message = "Documento é obrigatório")
    private String value;

    protected DocumentoVO() {}

    public DocumentoVO(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
