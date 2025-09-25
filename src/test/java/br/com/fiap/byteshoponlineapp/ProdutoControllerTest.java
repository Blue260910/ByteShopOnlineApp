package br.com.fiap.byteshoponlineapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    "INSERT INTO carrinho (cliente_id) VALUES (1);",
    "INSERT INTO produto (nome, preco, categoria, descricao, ativo) VALUES ('Notebook Dell', 3500.00, 'Informática', 'Notebook básico para estudos', TRUE);",
    "INSERT INTO produto (nome, preco, categoria, descricao, ativo) VALUES ('Mouse sem fio', 120.00, 'Acessórios', 'Mouse sem fio USB', TRUE);"
})

public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarProdutos() throws Exception {
        mockMvc.perform(get("/produtos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        mockMvc.perform(get("/produtos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveCriarProduto() throws Exception {
        String novoProduto = "{\"nome\":\"Produto Teste\",\"preco\":10.0,\"categoria\":\"Eletrônicos\",\"descricao\":\"Teste\",\"ativo\":true}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/produtos")
                .contentType("application/json")
                .content(novoProduto)
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome").value("Produto Teste"));
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        String atualizaProduto = "{\"nome\":\"Produto Atualizado\",\"preco\":20.0,\"categoria\":\"Eletrônicos\",\"descricao\":\"Atualizado\",\"ativo\":true}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/produtos/1")
                .contentType("application/json")
                .content(atualizaProduto)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Produto Atualizado"));
    }

    @Test
    void deveExcluirProduto() throws Exception {
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/produtos/1")
        )
            .andExpect(status().isNoContent());
    }

    @Test
    void naoDeveCriarProdutoComNomeVazio() throws Exception {
        String produtoInvalido = "{\"nome\":\"\",\"preco\":10.0}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/produtos")
                .contentType("application/json")
                .content(produtoInvalido)
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCriarProdutoComPrecoInvalido() throws Exception {
        String produtoInvalido = "{\"nome\":\"Produto Teste\",\"preco\":0}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/produtos")
                .contentType("application/json")
                .content(produtoInvalido)
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar409AoExcluirProdutoEmUso() throws Exception {
        // Adiciona produto 1 ao carrinho
        String novoItem = "{\"produtoId\":1,\"quantidade\":1}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                .contentType("application/json")
                .content(novoItem)
        ).andExpect(status().isCreated());
        // Tenta excluir produto 1
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/produtos/1")
        )
            .andExpect(status().isConflict());
    }
}