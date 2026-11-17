# DSCommerce

API REST para gerenciamento de um sistema de comércio eletrônico, desenvolvida com **Java e Spring Boot**. O projeto contempla operações de consulta e manutenção de produtos e categorias, gerenciamento de usuários, autenticação e autorização com OAuth2/JWT (incluindo Password Grant customizado), além do fluxo de criação e consulta de pedidos.

A aplicação foi desenvolvida a partir dos requisitos do projeto educacional **DSCommerce**, da [DevSuperior](https://devsuperior.com.br), com o objetivo de praticar conceitos avançados de desenvolvimento backend, modelagem de domínio relacional, segurança robusta e arquitetura em camadas.

## Sumário

- [Visão geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Tecnologias e ferramentas](#tecnologias-e-ferramentas)
- [Modelo de domínio](#modelo-de-domínio)
- [Regras de negócio](#regras-de-negócio)
- [Validação e tratamento de erros](#validação-e-tratamento-de-erros)
- [Arquitetura e organização do projeto](#arquitetura-e-organização-do-projeto)
- [Executando localmente](#executando-localmente)
- [Testando a API (Postman Collection e Environment)](#testando-a-api-postman-collection-e-environment)

## Visão geral

O DSCommerce representa o backend de uma aplicação de e-commerce corporativa. Os usuários podem consultar o catálogo de produtos de forma otimizada, realizar autenticação via token JWT, registrar pedidos e consultar seus próprios dados de perfil e histórico de compras. Administradores possuem privilégios elevados para gerenciar o catálogo de produtos e categorias.

O domínio foi modelado utilizando JPA/Hibernate com relacionamentos complexos:
- **Um para muitos (1:N):** um usuário possui vários pedidos.
- **Muitos para muitos (N:N):** um produto pertence a várias categorias, e uma categoria possui vários produtos.
- **Um para muitos (1:N):** um pedido contém vários itens (`OrderItem`).
- **Um para um (1:1):** um pedido possui os dados de pagamento associados (`Payment`).

## Funcionalidades

### Catálogo de produtos
- Consulta de produtos por ID com detalhes completos e categorias associadas.
- Listagem paginada e ordenada de produtos (`/products`) com filtro opcional por nome.
- Criação, atualização e exclusão de produtos (restrito a administradores).

### Paginação e ordenação do catálogo

A listagem de produtos (`GET /products`) utiliza os recursos de paginação do Spring Data, permitindo controlar o número de registros por página, critérios de ordenação e busca por nome.

Parâmetros suportados na *query string*:
- `page`: Número da página (começando em `0`).
- `size`: Quantidade de registros por página (padrão configurado).
- `sort`: Critério de ordenação (ex: `name,asc` para ordem alfabética crescente).
- `name`: Filtro opcional por parte do nome do produto.

**Exemplo de requisição:**
```http
GET /products?page=0&size=12&sort=name,asc&name=PC
```

### Categorias
- Listagem de todas as categorias cadastradas (`/categories`).

### Usuários e Perfil
- Endpoint protegido (`/users/profile`) para recuperar os dados do usuário autenticado no token JWT atual.

### Autenticação e Autorização
- Servidor de Autorização e Servidor de Recursos integrados com OAuth2 e JWT.
- Fluxo *Password Grant* customizado para validação de credenciais via `UserDetailsService` e hash `BCrypt`.
- Controle de acesso por rotas e perfis de usuário (`ROLE_CLIENT`, `ROLE_ADMIN`).

### Pedidos
- Registro de novos pedidos para o usuário autenticado com itens e validação de estoque/produtos.
- Consulta de pedidos por ID validando a propriedade (o cliente só vê os próprios pedidos, enquanto o administrador acessa qualquer pedido).

## Tecnologias e ferramentas

- **Java 25** — Linguagem de programação.
- **Spring Boot** — Framework para desenvolvimento da aplicação.
- **Spring Data JPA & Hibernate** — Persistência, ORM e acesso aos dados.
- **Spring Security & OAuth2** — Infraestrutura de segurança, Resource Server e Authorization Server.
- **JWT (JSON Web Token)** — Padrão de tokens para autenticação *stateless*.
- **ModelMapper / MapStruct** — Mapeamento eficiente entre entidades e DTOs.
- **Jakarta Validation** — Validação robusta de dados de entrada (`@NotBlank`, `@Positive`, `@Size`, etc.).
- **H2 Database** — Banco de dados em memória para ambiente de desenvolvimento e testes.
- **Maven** — Gerenciamento de dependências e build.

## Modelo de domínio

Principais entidades do sistema:
- `User`: Armazena dados cadastrais, e-mail, senha criptografada e perfis (`Role`).
- `Role`: Define as permissões de acesso (`ROLE_CLIENT`, `ROLE_ADMIN`).
- `Product`: Representa os itens do catálogo de e-commerce.
- `Category`: Categoriza os produtos.
- `Order`: Registra as compras efetuadas pelos clientes, data do pedido, status e vínculo com o usuário e pagamento.
- `OrderItem`: Tabela associativa que armazena a quantidade e o preço exato do produto praticado no momento da compra.
- `Payment`: Dados de pagamento do pedido.

## Regras de negócio

- **Perfis de acesso:** O sistema diferencia clientes (`ROLE_CLIENT`) e administradores (`ROLE_ADMIN`).
- **Status de pedidos:** O fluxo inicia com o status `WAITING_PAYMENT`.
- **Segurança nos pedidos:** Apenas o próprio cliente proprietário do pedido ou um administrador podem consultar os detalhes de um pedido.

## Validação e tratamento de erros

A aplicação utiliza Jakarta Bean Validation nos DTOs e possui uma classe centralizada de tratamento de exceções (`ControllerExceptionHandler`) que retorna respostas padronizadas (`CustomError`, `ValidationError`) com códigos HTTP adequados:
- `200 OK` / `201 Created` / `204 No Content`
- `400 Bad Request` / `401 Unauthorized` / `403 Forbidden` / `404 Not Found`
- `422 Unprocessable Entity` (erros de validação de campos)
- `409 Conflict` (violação de integridade referencial)

## Arquitetura e organização do projeto

O projeto segue uma arquitetura limpa em camadas bem definidas:

```text
src/main/java/com/mooncake/dscommerce/
├── config/            # Configurações de segurança, OAuth2 e beans
├── controllers/       # Controladores REST (Product, Category, Order, User)
├── dto/               # Objetos de Transferência de Dados
├── entities/          # Entidades JPA e Enums
├── mappers/           # Mapeadores de entidades para DTOs
├── projections/       # Projeções para consultas customizadas no banco
├── repositories/      # Repositórios Spring Data JPA
└── services/          # Regras de negócio e tratamentos de exceção
```

## Executando localmente

### Pré-requisitos
- JDK 25 instalado.
- Maven (ou utilizando o Maven Wrapper `./mvnw` incluso no projeto).

### 1. Clone o repositório
```bash
git clone git@github.com:yarabrg/sistema-dscommerce.git
cd dscommerce
```

### 2. Carga inicial de dados (Seed)
O projeto conta com um arquivo de seed SQL localizado em `src/main/resources/import.sql` contendo categorias, produtos, usuários reais inspirados no universo de **Cyberpunk 2077** (incluindo personagens como Valerie (V), Johnny Silverhand e Judy Alvarez), papéis (roles), pedidos e pagamentos pré-cadastrados. O banco H2 carrega esses dados automaticamente ao iniciar a aplicação. Isso facilita a execução de testes locais, validações de autenticação OAuth2 e simulações de pedidos sem a necessidade de cadastrar tudo manualmente.

### 3. Execute a aplicação
No Linux/macOS:
```bash
./mvnw spring-boot:run
```
No Windows:
```bat
mvnw.cmd spring-boot:run
```

A aplicação estará disponível em:
```text
http://localhost:8080
```

O console do banco de dados H2 pode ser acessado em:
```text
http://localhost:8080/h2-console
```

## Testando a API (Postman Collection e Environment)

Para facilitar os testes de todos os endpoints (incluindo o fluxo de login OAuth2 e rotas protegidas), o repositório disponibiliza os arquivos de configuração do Postman dentro da pasta `postman/`:

- **Collection:** `postman/dscommerse.postman_collection.json`
- **Environment:** `postman/dscommerse.postman_environment.json`

### Como utilizar:
1. Abra o Postman.
2. Importe ambos os arquivos de collection e environment localizados na pasta `postman/`.
3. Selecione o environment importado no canto superior direito do Postman.
4. Certifique-se de que as variáveis do environment estão preenchidas corretamente (ex: `host` como `http://localhost:8080`, além das credenciais de cliente e usuário).
5. Execute a requisição de **Login** na pasta *Authorization Server* para obter o token JWT de forma automática (o script de teste da requisição já armazena o token na variável de ambiente).
6. Utilize o token nas requisições protegidas enviando o cabeçalho HTTP:
```http
Authorization: Bearer <seu_token_jwt>
```
