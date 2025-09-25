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
	"INSERT INTO pedido (cliente_id, total, status) VALUES (1, 100.00, 'CRIADO');"
})
public class ClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void deveRetornarClientesInseridos() throws Exception {
		var resultado = mockMvc.perform(get("/clientes"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].nome").value("Ana Silva"))
			.andExpect(jsonPath("$[0].email").value("ana.silva@email.com"))
			.andExpect(jsonPath("$[0].documento").value("123.456.789-00"))
			.andExpect(jsonPath("$[1].nome").value("Carlos Souza"))
			.andExpect(jsonPath("$[1].email").value("carlos.souza@email.com"))
			.andExpect(jsonPath("$[1].documento").value("987.654.321-00"))
			.andReturn();
		System.out.println("JSON retornado: " + resultado.getResponse().getContentAsString());
	}

	@Test
	void deveRetornarClientePorId() throws Exception {
		mockMvc.perform(get("/clientes/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nome").value("Ana Silva"));
	}

	@Test
	void deveRetornar404SeClienteNaoExistir() throws Exception {
		mockMvc.perform(get("/clientes/9"))
			.andExpect(status().isNotFound());
	}

	@Test
	void deveCriarClienteERetornar201() throws Exception {
		String novoCliente = "{" +
			"\"nome\": \"João Teste\"," +
			"\"email\": \"joao.teste@email.com\"," +
			"\"documento\": \"111.222.333-44\"}";
		mockMvc.perform(
			org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/clientes")
				.contentType("application/json")
				.content(novoCliente)
		)
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"));
	}

	@Test
	void deveAtualizarCliente() throws Exception {
		String clienteAtualizado = "{" +
			"\"nome\": \"Ana Atualizada\"," +
			"\"email\": \"ana.atualizada@email.com\"," +
			"\"documento\": \"123.456.789-00\"}";
		mockMvc.perform(
			org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/clientes/1")
				.contentType("application/json")
				.content(clienteAtualizado)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nome").value("Ana Atualizada"));
	}

	@Test
	void deveExcluirCliente() throws Exception {
		mockMvc.perform(
			org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/clientes/2")
		)
			.andExpect(status().isNoContent());
	}

	@Test
	void deveRetornar409AoExcluirClienteComPedidos() throws Exception {
		mockMvc.perform(
			org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/clientes/1")
		)
			.andExpect(status().isConflict());
	}
}
