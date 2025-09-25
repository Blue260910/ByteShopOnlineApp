package br.com.fiap.byteshoponlineapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
// import removido: get não utilizado
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
public class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCriarPagamentoParaPedido() throws Exception {
        // Adiciona item ao carrinho e cria pedido
        String novoItem = "{\"produtoId\":1,\"quantidade\":2}";
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                .contentType("application/json")
                .content(novoItem)
        ).andExpect(status().isCreated());
        String novoPedido = "{\"carrinhoId\":1}";
        var pedidoResult = mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                .contentType("application/json")
                .content(novoPedido)
        ).andReturn();
        String pedidoId = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.id").toString();
        String total = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.total").toString();

        // Cria pagamento
        String novoPagamento = String.format("{\"pedidoId\":%s,\"valor\":%s,\"metodo\":\"PIX\"}", pedidoId, total);
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pagamentos")
                .contentType("application/json")
                .content(novoPagamento)
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.pedidoId").value(pedidoId))
            .andExpect(jsonPath("$.valor").value(total))
            .andExpect(jsonPath("$.metodo").value("PIX"))
            .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

        @Test
        void naoDevePermitirMaisDeUmPagamentoPorPedido() throws Exception {
            String novoItem = "{\"produtoId\":1,\"quantidade\":1}";
            mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                    .contentType("application/json")
                    .content(novoItem)
            ).andExpect(status().isCreated());
            String novoPedido = "{\"carrinhoId\":1}";
            var pedidoResult = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                    .contentType("application/json")
                    .content(novoPedido)
            ).andReturn();
            String pedidoId = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.id").toString();
            String total = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.total").toString();

            String pagamento = String.format("{\"pedidoId\":%s,\"valor\":%s,\"metodo\":\"PIX\"}", pedidoId, total);
            mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pagamentos")
                    .contentType("application/json")
                    .content(pagamento)
            ).andExpect(status().isCreated());

            mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pagamentos")
                    .contentType("application/json")
                    .content(pagamento)
            ).andExpect(status().isBadRequest());
        }

        @Test
        void deveBuscarPagamentoPorId() throws Exception {
            String novoItem = "{\"produtoId\":1,\"quantidade\":1}";
            mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                    .contentType("application/json")
                    .content(novoItem)
            ).andExpect(status().isCreated());
            String novoPedido = "{\"carrinhoId\":1}";
            var pedidoResult = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                    .contentType("application/json")
                    .content(novoPedido)
            ).andReturn();
            String pedidoId = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.id").toString();
            String total = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.total").toString();

            String pagamento = String.format("{\"pedidoId\":%s,\"valor\":%s,\"metodo\":\"PIX\"}", pedidoId, total);
            var pagamentoResult = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pagamentos")
                    .contentType("application/json")
                    .content(pagamento)
            ).andReturn();
            String pagamentoId = com.jayway.jsonpath.JsonPath.read(pagamentoResult.getResponse().getContentAsString(), "$.id").toString();

            mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/pagamentos/" + pagamentoId)
            )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pagamentoId))
                .andExpect(jsonPath("$.pedidoId").value(pedidoId));
        }

        @Test
        void naoDevePermitirValorDiferenteDoTotalDoPedido() throws Exception {
            String novoItem = "{\"produtoId\":1,\"quantidade\":1}";
            mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                    .contentType("application/json")
                    .content(novoItem)
            ).andExpect(status().isCreated());
            String novoPedido = "{\"carrinhoId\":1}";
            var pedidoResult = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                    .contentType("application/json")
                    .content(novoPedido)
            ).andReturn();
            String pedidoId = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.id").toString();
            String total = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.total").toString();

            String pagamento = String.format("{\"pedidoId\":%s,\"valor\":%s,\"metodo\":\"PIX\"}", pedidoId, "9999.99");
            mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pagamentos")
                    .contentType("application/json")
                    .content(pagamento)
            ).andExpect(status().isBadRequest());
        }

        @Test
        void naoDevePermitirMetodoInvalido() throws Exception {
            String novoItem = "{\"produtoId\":1,\"quantidade\":1}";
            mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/carrinhos/1/itens")
                    .contentType("application/json")
                    .content(novoItem)
            ).andExpect(status().isCreated());
            String novoPedido = "{\"carrinhoId\":1}";
            var pedidoResult = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pedidos")
                    .contentType("application/json")
                    .content(novoPedido)
            ).andReturn();
            String pedidoId = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.id").toString();
            String total = com.jayway.jsonpath.JsonPath.read(pedidoResult.getResponse().getContentAsString(), "$.total").toString();

            String pagamento = String.format("{\"pedidoId\":%s,\"valor\":%s,\"metodo\":\"DINHEIRO\"}", pedidoId, total);
            mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/pagamentos")
                    .contentType("application/json")
                    .content(pagamento)
            ).andExpect(status().isBadRequest());
        }
}