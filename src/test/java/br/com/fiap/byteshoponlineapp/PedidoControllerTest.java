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
    "INSERT INTO carrinho (cliente_id) VALUES (1);",
    "INSERT INTO produto (nome, preco, categoria, descricao, ativo) VALUES ('Notebook Dell', 3500.00, 'Informática', 'Notebook básico para estudos', TRUE);",
    "INSERT INTO produto (nome, preco, categoria, descricao, ativo) VALUES ('Mouse sem fio', 120.00, 'Acessórios', 'Mouse sem fio USB', TRUE);"
})
public class PedidoControllerTest {
    @Test
    void deveCancelarPedido() throws Exception {
        // Adiciona item ao carrinho e cria pedido
        String novoItem = "{\"produtoId\":1,\"quantidade\":2}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                .contentType("application/json")
                .content(novoItem)
        ).andExpect(status().isCreated());
        String novoPedido = "{\"carrinhoId\":1}";
        var result = mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                .contentType("application/json")
                .content(novoPedido)
        ).andReturn();
        String pedidoId = com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.id").toString();

        // Cancela pedido
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/pedidos/" + pedidoId)
        ).andExpect(status().isNoContent());

        // Verifica que pedido não existe mais
        mockMvc.perform(get("/pedidos/" + pedidoId))
            .andExpect(status().isNotFound());
    }
    @Test
    void deveAtualizarStatusDoPedido() throws Exception {
        // Adiciona item ao carrinho e cria pedido
        String novoItem = "{\"produtoId\":1,\"quantidade\":2}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                .contentType("application/json")
                .content(novoItem)
        ).andExpect(status().isCreated());
        String novoPedido = "{\"carrinhoId\":1}";
        var result = mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                .contentType("application/json")
                .content(novoPedido)
        ).andReturn();
        String pedidoId = com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.id").toString();

        // Atualiza status para PAGO
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/pedidos/" + pedidoId + "/status")
                .contentType("application/json")
                .content("{\"status\":\"PAGO\"}")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(Integer.parseInt(pedidoId)))
            .andExpect(jsonPath("$.status").value("PAGO"));
    }
    @Test
    void deveBuscarPedidoPorId() throws Exception {
        // Adiciona item ao carrinho e cria pedido
        String novoItem = "{\"produtoId\":1,\"quantidade\":2}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                .contentType("application/json")
                .content(novoItem)
        ).andExpect(status().isCreated());
        String novoPedido = "{\"carrinhoId\":1}";
        var result = mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                .contentType("application/json")
                .content(novoPedido)
        ).andReturn();
        String pedidoId = com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.id").toString();

        // Busca pedido por id
        mockMvc.perform(get("/pedidos/" + pedidoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(Integer.parseInt(pedidoId)))
            .andExpect(jsonPath("$.clienteId").value(1))
            .andExpect(jsonPath("$.total").exists())
            .andExpect(jsonPath("$.status").value("CRIADO"))
            .andExpect(jsonPath("$.itens[0].produtoId").value(1))
            .andExpect(jsonPath("$.itens[0].quantidade").value(2));
            // .andExpect(jsonPath("$.pagamento").doesNotExist()); // se não houver pagamento
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCriarPedidoAPartirDeCarrinhoFinalizado() throws Exception {
        // Adiciona item ao carrinho
        String novoItem = "{\"produtoId\":1,\"quantidade\":2}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                .contentType("application/json")
                .content(novoItem)
        ).andExpect(status().isCreated());

        // Cria pedido a partir do carrinho
        String novoPedido = "{\"carrinhoId\":1}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                .contentType("application/json")
                .content(novoPedido)
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.clienteId").value(1))
            .andExpect(jsonPath("$.total").exists())
            .andExpect(jsonPath("$.status").value("CRIADO"))
            .andExpect(jsonPath("$.itens[0].produtoId").value(1))
            .andExpect(jsonPath("$.itens[0].quantidade").value(2));
    }

    @Test
    void deveListarPedidos() throws Exception {
        // Adiciona item ao carrinho e cria pedido
        String novoItem = "{\"produtoId\":1,\"quantidade\":2}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                .contentType("application/json")
                .content(novoItem)
        ).andExpect(status().isCreated());
        String novoPedido = "{\"carrinhoId\":1}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                .contentType("application/json")
                .content(novoPedido)
        ).andExpect(status().isCreated());

        // Lista pedidos
        mockMvc.perform(get("/pedidos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].clienteId").value(1))
            .andExpect(jsonPath("$[0].status").value("CRIADO"));

        // Filtro por cliente
        mockMvc.perform(get("/pedidos?clienteId=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].clienteId").value(1));

        // Filtro por status
        mockMvc.perform(get("/pedidos?status=CRIADO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].status").value("CRIADO"));
    }
}