# Sistema Simplificado de Banco — API RESTful com Java Spring Boot
![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-brightgreen?logo=spring)
![Docker](https://img.shields.io/badge/Docker-Disponível-blue?logo=docker)
![RESTful API](https://img.shields.io/badge/API-RESTful-orange)
![Deploy-Netlify](https://img.shields.io/badge/Deploy-Netlify-success?logo=netlify)

Este projeto é uma aplicação bancária que oferece operações essenciais com autenticação segura, desenvolvida com foco em boas práticas de engenharia de software e implantação em ambiente de produção.

[🔗 Link da aplicação — ssbproject](https://ssbproject.netlify.app/index.html)

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Security (JWT + BCrypt)**
- **Spring Data JPA**
- **PostgreSQL (produção em AWS RDS)**
- **H2 Database (para testes locais)**
- **Docker (imagem da API disponível no Docker Hub)**
- **Swagger UI (documentação estática da API)**

---

## ⚙️ Funcionalidades

- **Criação de contas bancárias** com validação básica de e-mail (verificação de formato e domínio).
- **Autenticação de clientes** utilizando JWT.
- **Operações bancárias seguras**: depósito, saque e transferência entre contas.
- **Consulta de extratos** com filtros por data/hora e tipo de transação.
- **Validação de sessão** por token JWT com expiração e renovação segura.
- **Controle de acesso** garantindo que clientes só possam operar em suas próprias contas.

---

## 🔐 Segurança

- **Tokens JWT** com expiração e validação no backend.
- **Senhas criptografadas** utilizando BCryptPasswordEncoder.
- **Proteção contra acesso indevido** através de validações rigorosas no backend.
- **Separação clara** entre as responsabilidades do cliente e do servidor.

---

## 📌 Endpoints Principais

### 🔑 Autenticação

- `POST /auth/login`  
  Autentica um cliente e retorna o token JWT.

---

### 👤 Clientes

- `POST /clients`  
  Cria um novo cliente.

- `GET /clients/{clientId}`  
  Retorna informações de um cliente específico.

- `PUT /clients/{clientId}/email`  
  Atualiza o e-mail de um cliente.

- `PUT /clients/{clientId}/password`  
  Atualiza a senha de um cliente.

- `DELETE /clients/{clientId}`  
  Remove um cliente específico.

---

### 🏦 Contas

- `POST /accounts`  
  Cria uma nova conta bancária.

- `GET /accounts/{number}`  
  Retorna detalhes de uma conta específica.

- `PUT /accounts/{number}/deposit`  
  Realiza um depósito em uma conta.

- `PUT /accounts/{number}/withdraw`  
  Realiza um saque de uma conta.

- `PUT /accounts/{number}/transfer`  
  Transfere fundos entre contas.

- `PUT /accounts/{number}/toggle-status`  
  Ativa ou desativa uma conta.

---

### 📄 Transações

- `GET /transactions/{number}`  
  Retorna o extrato de transações de uma conta.

---

Para detalhes completos e exemplos de uso, consulte a Documentação da API - Swagger HTML2.

## 📄 Documentação da API

A documentação estática da API está disponível através do Swagger UI. Você pode acessá-la no seguinte link:

[Documentação da API - Swagger UI](https://swagger-ssb-api.netlify.app/)

---

## 📦 Deploy

- **Imagem Docker** disponível no Docker Hub:

  ```bash
  docker pull julioxcsf/ssbbank:1.0.2
  ```

---

## Melhorias futuras

Algumas funcionalidades planejadas para futuras implementações incluem:

- **Atribuição de roles**: Implementar diferentes níveis de acesso, como o papel de "Gerente", que teria permissões para gerenciar contas de clientes e desativar contas conforme necessário.

- **Paginação de resultados**: Adicionar mecanismo de paginação para grande volume de dados, como em transações(extrato)
