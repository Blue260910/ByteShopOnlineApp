src/
  main/
    java/
      br/com/fiap/byteshoponlineapp/
        api/         # Controllers REST
        domain/      # Entidades, DTOs, Value Objects, Repositórios
        service/     # Regras de negócio
        infrastructure/ # Configurações
    resources/
      application.properties
      static/
      templates/
  test/
    java/
      br/com/fiap/byteshoponlineapp/ # Testes automatizados

# ByteShopOnlineApp

![Java](https://img.shields.io/badge/Java-17%2B-blue?style=flat-square) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square) ![H2 Database](https://img.shields.io/badge/H2-Database-lightgrey?style=flat-square)

---

## 🛒 Sobre o Projeto
O **ByteShopOnlineApp** é uma aplicação web desenvolvida em Java com Spring Boot, simulando uma loja online completa. O sistema permite cadastro de clientes, gerenciamento de produtos, carrinho de compras, pedidos e pagamentos, com arquitetura moderna e boas práticas.

---

## 🚀 Funcionalidades
- Cadastro e consulta de clientes
- Gerenciamento de produtos
- Carrinho de compras
- Processamento de pedidos
- Pagamento integrado
- Tratamento global de exceções
- API RESTful documentada

---

## 📦 Estrutura do Projeto
```text
src/
  main/
    java/
      br/com/fiap/byteshoponlineapp/
        api/             # Controllers REST
        domain/          # Entidades, DTOs, Value Objects, Repositórios
        service/         # Regras de negócio
        infrastructure/  # Configurações
    resources/
      application.properties
      static/
      templates/
  test/
    java/
      br/com/fiap/byteshoponlineapp/ # Testes automatizados
```

---

## 🛠️ Tecnologias Utilizadas
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- H2 Database (dev)
- Maven
- JUnit 5

---

## ⚙️ Como Executar
1. **Pré-requisitos:**
   - Java 17 ou superior
   - Maven
2. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/ByteShopOnlineApp.git
   ```
3. **Execute o projeto:**
   ```bash
   ./mvnw spring-boot:run
   ```
4. **Acesse:**
   - API: `http://localhost:8080/`
   - H2 Console: `http://localhost:8080/h2-console`

---

## 🧪 Sugestão de Fluxo de Teste

Exemplo de fluxo completo para testar a aplicação:

1️⃣ **Criar Cliente**
```http
POST /clientes
Content-Type: application/json
{
  "nome": "Victor Aranda",
  "email": "Victor@gmail.com",
  "documento": "52604982838"
}
```

2️⃣ **Criar Produto**
```http
POST /produtos
Content-Type: application/json
{
  "id": 1,
  "nome": "Notebook Galaxy Book",
  "preco": 1250,
  "categoria": "Notebook",
  "descricao": "Notebook da samsung bonitao",
  "ativo": true
}
```

3️⃣ **Criar Carrinho**
```http
POST /carrinhos
Content-Type: application/json
{
  "clienteId": 1
}
```

4️⃣ **Adicionar Item ao Carrinho**
```http
POST /carrinhos/1/itens
Content-Type: application/json
{
  "produtoId": 1,
  "quantidade": 3
}
```

5️⃣ **Criar Pedido**
```http
POST /pedidos
Content-Type: application/json
{
  "carrinhoId": 1
}
```

6️⃣ **Realizar Pagamento**
```http
POST /pagamentos
Content-Type: application/json
{
  "pedidoId": 1,
  "valor": 3750,
  "metodo": "PIX"
}
```

---

## 🧪 Testes
Execute os testes automatizados com:
```bash
./mvnw test
```

---

## 👤 Integrantes
- André Lambert – RM 99148
- Felipe Cortez - RM 99750
- Julia Lins - RM 98690
- Luis Barreto - RM 99210
- Victor Aranda - RM 99667

---

## 📃 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

> Projeto acadêmico desenvolvido para fins de estudo e demonstração.
