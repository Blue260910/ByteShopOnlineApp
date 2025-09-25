package br.com.fiap.byteshoponlineapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(statements = {
	"DELETE FROM pagamento;",
	"DELETE FROM item_pedido;",
	"DELETE FROM pedido;",
	"DELETE FROM item_carrinho;",
	"DELETE FROM carrinho;",
	"DELETE FROM cliente;",
	"DELETE FROM produto;",
	"ALTER TABLE pagamento ALTER COLUMN id RESTART WITH 1;",
	"ALTER TABLE item_pedido ALTER COLUMN id RESTART WITH 1;",
	"ALTER TABLE pedido ALTER COLUMN id RESTART WITH 1;",
	"ALTER TABLE item_carrinho ALTER COLUMN id RESTART WITH 1;",
	"ALTER TABLE carrinho ALTER COLUMN id RESTART WITH 1;",
	"ALTER TABLE cliente ALTER COLUMN id RESTART WITH 1;",
	"ALTER TABLE produto ALTER COLUMN id RESTART WITH 1;",
    "INSERT INTO cliente (nome, email, documento) VALUES ('Ana Silva', 'ana.silva@email.com', '123.456.789-00');",
    "INSERT INTO cliente (nome, email, documento) VALUES ('Carlos Souza', 'carlos.souza@email.com', '987.654.321-00');",
    "INSERT INTO carrinho (cliente_id) VALUES (1);"
})
public class CarrinhoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveBuscarCarrinhoPorId() throws Exception {
        mockMvc.perform(get("/carrinhos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.clienteId").value(1));
    }

    @Test
    void deveCriarCarrinhoParaCliente() throws Exception {
        String novoCarrinho = "{\"clienteId\":2}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos")
                .contentType("application/json")
                .content(novoCarrinho)
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.clienteId").value(2));
    }

    @Test
    void naoDeveCriarCarrinhoDuplicadoParaCliente() throws Exception {
        String carrinhoDuplicado = "{\"clienteId\":1}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos")
                .contentType("application/json")
                .content(carrinhoDuplicado)
        )
            .andExpect(status().isConflict());
    }

    @Test
    void deveExcluirCarrinho() throws Exception {
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/carrinhos/1")
        )
            .andExpect(status().isNoContent());
    }
}

