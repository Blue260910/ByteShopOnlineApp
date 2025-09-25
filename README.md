# ByteShopOnlineApp

![Java](https://img.shields.io/badge/Java-17%2B-blue?style=flat-square) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square) ![H2 Database](https://img.shields.io/badge/H2-Database-lightgrey?style=flat-square)

## 🛒 Sobre o Projeto
O **ByteShopOnlineApp** é uma aplicação web desenvolvida em Java com Spring Boot, simulando uma loja online completa. O sistema permite cadastro de clientes, gerenciamento de produtos, carrinho de compras, pedidos e pagamentos, com arquitetura moderna e boas práticas.

## 🚀 Funcionalidades
- Cadastro e consulta de clientes
- Gerenciamento de produtos
- Carrinho de compras
- Processamento de pedidos
- Pagamento integrado
- Tratamento global de exceções
- API RESTful documentada

## 📦 Estrutura do Projeto
```
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
```

## 🛠️ Tecnologias Utilizadas
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- H2 Database (dev)
- Maven
- JUnit 5

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

## 📋 Exemplos de Uso
- Exemplos reais de requisições para cada endpoint principal:

### 📑 Cliente
**Criar Cliente**
```http
POST /clientes
Content-Type: application/json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "documento": "12345678900"
}
```
**Listar Clientes**
```http
GET /clientes
```
**Buscar Cliente por ID**
```http
GET /clientes/1
```
**Atualizar Cliente**
```http
PUT /clientes/1
Content-Type: application/json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "documento": "12345678900"
}
```
**Remover Cliente**
```http
DELETE /clientes/1
```

### 📦 Produto
**Criar Produto**
```http
POST /produtos
Content-Type: application/json
{
  "nome": "Notebook Dell",
  "preco": 3500.00,
  "categoria": "Informática"
}
```
**Listar Produtos**
```http
GET /produtos
```
**Buscar Produto por ID**
```http
GET /produtos/1
```
**Atualizar Produto**
```http
PUT /produtos/1
Content-Type: application/json
{
  "nome": "Notebook Dell XPS",
  "preco": 4000.00,
  "categoria": "Informática"
}
```
**Excluir Produto**
```http
DELETE /produtos/1
```

### 🛒 Carrinho
**Criar Carrinho**
```http
POST /carrinhos
Content-Type: application/json
{
  "clienteId": 1
}
```
**Buscar Carrinho por ID**
```http
GET /carrinhos/1
```
**Excluir Carrinho**
```http
DELETE /carrinhos/1
```

### 🧩 Item do Carrinho
**Adicionar Item**
```http
POST /carrinhos/1/itens
Content-Type: application/json
{
  "produtoId": 1,
  "quantidade": 2
}
```
**Atualizar Quantidade**
```http
PUT /carrinhos/1/itens/1
Content-Type: application/json
{
  "quantidade": 3
}
```
**Remover Item**
```http
DELETE /carrinhos/1/itens/1
```
**Listar Itens do Carrinho**
```http
GET /carrinhos/1/itens
```

### 📃 Pedido
**Criar Pedido**
```http
POST /pedidos
Content-Type: application/json
{
  "clienteId": 1,
  "itens": [
    { "produtoId": 1, "quantidade": 2 }
  ]
}
```
**Listar Pedidos**
```http
GET /pedidos?clienteId=1
```
**Buscar Pedido por ID**
```http
GET /pedidos/1
```
**Atualizar Status do Pedido**
```http
PUT /pedidos/1/status
Content-Type: application/json
{
  "status": "PAGO"
}
```
**Cancelar Pedido**
```http
DELETE /pedidos/1
```

### 💳 Pagamento
**Criar Pagamento**
```http
POST /pagamentos
Content-Type: application/json
{
  "pedidoId": 1,
  "valor": 3500.00,
  "metodo": "CARTAO"
}
```
**Buscar Pagamento por ID**
```http
GET /pagamentos/1
```
**Atualizar Status do Pagamento**
```http
PUT /pagamentos/1/status
Content-Type: application/json
"APROVADO"
```

## 🧪 Testes
Execute os testes automatizados com:
```bash
./mvnw test
```

## 👤 Integrantes
- André Lambert – RM 99148
- Felipe Cortez - RM 99750
- Julia Lins - RM 98690
- Luis Barreto - RM 99210
- Victor Aranda - RM 99667

## 📃 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

---
> Projeto acadêmico desenvolvido para fins de estudo e demonstração.
