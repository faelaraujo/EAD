# EAD - Plataforma de Ensino a Distância

Projeto de uma plataforma EAD desenvolvida com arquitetura de microsserviços utilizando o ecossistema Spring. O sistema é composto por múltiplos serviços independentes que se comunicam de forma síncrona (REST/Feign) e assíncrona (RabbitMQ), garantindo escalabilidade, resiliência e baixo acoplamento.

---

## Arquitetura

O projeto segue o padrão de **microsserviços** com os seguintes componentes:

                     ┌─────────────────┐
                     │  Config Server  │
                     └────────┬────────┘
                              │
                     ┌────────▼────────┐
                     │ Service Registry│
                     │    (Eureka)     │
                     └────────┬────────┘
                              │
                     ┌────────▼────────┐
                     │   API Gateway   │
                     └───┬────┬────┬───┘
                         │    │    │
           ┌─────────────┘    │    └──────────────┐
           │                  │                   │
    ───────▼──────┐   ┌───────▼──────┐    ┌───────▼──────┐
    │   AuthUser   │   │    Course    │   │ Notification │
    │  (Auth/JWT)  │   │  (Cursos)   │    │  (Mensagens) │
    └──────────────┘   └──────────────┘   └──────────────┘
           │                  │                   │
           └──────────────────┴───────────────────┘
                          RabbitMQ
                     (Comunicação Assíncrona)

---

## Serviços

### 🔐 AuthUser
Responsável pela autenticação e gerenciamento de usuários.

- Cadastro, atualização e exclusão de usuários
- Autenticação via **JWT** (JSON Web Tokens)
- Controle de autorização com **Spring Security**
- Comunicação assíncrona com outros serviços via **RabbitMQ**
- Integração com Eureka, Config Server e Circuit Breaker (Resilience4j)
- Banco de dados: **PostgreSQL**

### 📚 Course
Responsável pelo gerenciamento de cursos e matrículas.

- CRUD completo de cursos e módulos
- Gerenciamento de inscrições de alunos
- Comunicação assíncrona via **RabbitMQ** (producers e consumers)
- Filtros avançados com **Specification**
- Suporte a **HATEOAS**
- Banco de dados: **PostgreSQL**

### 🔔 Notification
Responsável pelo envio e gerenciamento de notificações.

- Consumo de eventos via **RabbitMQ**
- Armazenamento e consulta de notificações por usuário
- Integração com Eureka e Config Server
- Banco de dados: **PostgreSQL**

### 💳 Payment *(em desenvolvimento)*
Serviço destinado ao processamento de pagamentos.

- Estrutura inicial criada com Spring Boot

### 🌐 API Gateway
Ponto de entrada único para todos os microsserviços.

- Roteamento de requisições via **Spring Cloud Gateway**
- Integração com **Eureka** para descoberta dinâmica de serviços
- Configuração centralizada via **Config Server**

### ⚙️ Config Server
Servidor de configurações centralizado.

- Gerenciamento externo de configurações para todos os serviços
- Integrado ao Eureka para descoberta de serviços
- Protegido com **Spring Security**

### 🗂️ Service Registry (Eureka)
Servidor de descoberta de serviços.

- Registro e descoberta dinâmica de todos os microsserviços
- Protegido com **Spring Security**
- Utilizado pelo API Gateway para roteamento

---

## Tecnologias Utilizadas

| Tecnologia | Descrição |
|---|---|
| **Java 21** | Linguagem principal |
| **Spring Boot 3.x / 4.x** | Framework base dos microsserviços |
| **Spring Cloud 2023.x / 2025.x** | Ferramentas para arquitetura de microsserviços |
| **Spring Security + JWT (jjwt)** | Autenticação e autorização |
| **Spring Cloud Netflix Eureka** | Service Discovery |
| **Spring Cloud Gateway** | API Gateway |
| **Spring Cloud Config** | Configuração centralizada |
| **Spring Cloud OpenFeign** | Comunicação síncrona entre serviços |
| **Spring AMQP (RabbitMQ)** | Comunicação assíncrona entre serviços |
| **Spring Data JPA** | Persistência de dados |
| **PostgreSQL** | Banco de dados relacional |
| **Resilience4j** | Circuit Breaker e tolerância a falhas |
| **Spring HATEOAS** | Hypermedia nas respostas REST |
| **Lombok** | Redução de boilerplate |
| **Log4j2** | Sistema de logging |
| **Maven** | Gerenciamento de dependências e build |

---

## Branches

| Branch | Descrição |
|---|---|
| `master` | Versão base com comunicação síncrona entre serviços |
| `ComunicacaoAssincrona` | Versão evoluída com comunicação assíncrona via RabbitMQ, adição do Config Server e melhorias gerais |

---

## Pré-requisitos

- Java 21+
- Maven 3.8+
- PostgreSQL
- RabbitMQ
- Git

---

## Como Executar

A ordem de inicialização dos serviços deve ser respeitada para garantir o correto funcionamento:

1. **Config Server** — sobe primeiro, pois os demais serviços buscam configurações nele
2. **Service Registry (Eureka)** — sobe logo após, para que os serviços se registrem
3. **AuthUser**, **Course**, **Notification** — podem subir em paralelo após os serviços de infraestrutura
4. **API Gateway** — sobe por último, após todos os serviços estarem registrados no Eureka

```bash
# Clone o repositório
git clone https://github.com/faelaraujo/EAD.git
cd EAD

# Escolha o branch desejado
git checkout master
# ou
git checkout ComunicacaoAssincrona

# Compile e execute cada serviço individualmente
cd config-server && mvn spring-boot:run
cd service-registry && mvn spring-boot:run
cd authuser && mvn spring-boot:run
cd course/course && mvn spring-boot:run
cd notification/notification && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

> **Atenção:** Certifique-se de que o **PostgreSQL** e o **RabbitMQ** estejam em execução antes de iniciar os serviços.

---

## Estrutura do Repositório

```text
EAD/
├── api-gateway/          # API Gateway (Spring Cloud Gateway)
├── authuser/             # Serviço de autenticação e usuários
├── config-server/        # Servidor de configuração centralizado
├── course/
│   └── course/           # Serviço de cursos
├── notification/
│   └── notification/     # Serviço de notificações
├── payment/
│   └── payment/          # Serviço de pagamentos (em desenvolvimento)
└── service-registry/     # Servidor Eureka (Service Discovery)
```

---

## Comunicação entre Serviços

### Síncrona (branch `master`)
A comunicação entre os serviços ocorre via chamadas REST utilizando **Spring Cloud OpenFeign**, com descoberta de serviços pelo Eureka.

### Assíncrona (branch `ComunicacaoAssincrona`)
Os serviços publicam e consomem eventos via **RabbitMQ**, garantindo desacoplamento e maior resiliência. Cada serviço possui pacotes `publishers` e/ou `consumers` dedicados à troca de mensagens.

---

## Autor

**Rafael** — [faelaraujo](https://github.com/faelaraujo)
