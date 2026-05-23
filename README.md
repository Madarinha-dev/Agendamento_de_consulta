 # 🏥 Agendamento de Consulta Médica
 
API REST para gerenciamento de consultas médicas, desenvolvida com Java e Spring Boot.

---

## 💻 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Hibernate**
- **Banco de Dados H2** (em memória)
- **Maven**
- **Lombok**
- **Swagger / SpringDoc OpenAPI**

---

## 📋 Funcionalidades

### Usuários

- Cadastro de usuários com validação de CPF e e-mail únicos
- Controle de permissões de acesso
- Atualização e exclusão de usuários

### Médicos

- Cadastro de médicos com validação de CPF, e-mail e número de conselho únicos
- Associação de especialidades ao médico
- Busca de médicos por especialidade

### Pacientes

- Cadastro completo de pacientes com dados pessoais e endereço
- Validação de CPF, e-mail

### Especialidades

- Cadastro de especialidades médicas com código CBO
- Validação de nome e código CBO únicos

### Procedimentos/Produtos

- Cadastro de procedimentos com códigos TUSS, CBHPM e ambulatorial
- Validação de duplicidade de nome interno e códigos

### Agendamentos

- Agendamento de consultas com validação de horário comercial (08h às 18h)
- Impedimento de agendamentos duplicados para médico e paciente no mesmo horário
- Listagem de agendamentos por médico ou paciente
- Cancelamento de agendamentos (sem exclusão do banco)
- Status: `AGENDADO`, `CONFIRMADO`, `CANCELADO`

---

## 📐 Arquitetura

O projeto segue o padrão de **arquitetura em camadas**:

```
Controller → Service → Repository → Banco de Dados
```

- **Controller**: recebe as requisições HTTP e delega ao Service
- **Service**: contém as regras de negócio e validações
- **Repository**: responsável pela persistência dos dados
- **Entity**: mapeamento das tabelas do banco de dados

---

## Endpoints

### Usuários — `/api/usuarios`

|Método|Rota|Descrição|Status|
|---|---|---|---|
|GET|`/api/usuarios`|Lista todos os usuários|200|
|GET|`/api/usuarios/{id}`|Busca usuário por ID|200 / 404|
|POST|`/api/usuarios`|Cadastra novo usuário|201|
|PUT|`/api/usuarios/{id}`|Atualiza usuário|200|
|DELETE|`/api/usuarios/{id}`|Remove usuário|204|

### Médicos — `/api/medicos`

|Método|Rota|Descrição|Status|
|---|---|---|---|
|GET|`/api/medicos`|Lista todos os médicos|200|
|GET|`/api/medicos/{id}`|Busca médico por ID|200 / 404|
|GET|`/api/medicos/especialidade?especialidadeId={id}`|Busca por especialidade|200|
|POST|`/api/medicos`|Cadastra novo médico|201|
|PUT|`/api/medicos/{id}`|Atualiza médico|200|
|DELETE|`/api/medicos/{id}`|Remove médico|204|

### Pacientes — `/api/pacientes`

|Método|Rota|Descrição|Status|
|---|---|---|---|
|GET|`/api/pacientes`|Lista todos os pacientes|200|
|GET|`/api/pacientes/{id}`|Busca paciente por ID|200 / 404|
|POST|`/api/pacientes`|Cadastra novo paciente|201|
|PUT|`/api/pacientes/{id}`|Atualiza paciente|200|
|DELETE|`/api/pacientes/{id}`|Remove paciente|204|

### Especialidades — `/api/especialidades`

|Método|Rota|Descrição|Status|
|---|---|---|---|
|GET|`/api/especialidades`|Lista todas as especialidades|200|
|GET|`/api/especialidades/{id}`|Busca especialidade por ID|200 / 404|
|POST|`/api/especialidades`|Cadastra nova especialidade|201|
|PUT|`/api/especialidades/{id}`|Atualiza especialidade|200|
|DELETE|`/api/especialidades/{id}`|Remove especialidade|204|

### Procedimentos — `/api/procedimentos`

|Método|Rota|Descrição|Status|
|---|---|---|---|
|GET|`/api/procedimentos`|Lista todos os procedimentos|200|
|GET|`/api/procedimentos/{id}`|Busca procedimento por ID|200 / 404|
|POST|`/api/procedimentos`|Cadastra novo procedimento|201|
|PUT|`/api/procedimentos/{id}`|Atualiza procedimento|200|
|DELETE|`/api/procedimentos/{id}`|Remove procedimento|204|

### Agendamentos — `/api/agendamentos`

|Método|Rota|Descrição|Status|
|---|---|---|---|
|GET|`/api/agendamentos`|Lista todos os agendamentos|200|
|GET|`/api/agendamentos/{id}`|Busca agendamento por ID|200 / 404|
|GET|`/api/agendamentos/medico/{medicoId}`|Lista por médico|200|
|GET|`/api/agendamentos/paciente/{pacienteId}`|Lista por paciente|200|
|POST|`/api/agendamentos`|Cria novo agendamento|201|
|PUT|`/api/agendamentos/{id}`|Atualiza agendamento|200|
|PATCH|`/api/agendamentos/{id}/cancelar`|Cancela agendamento|204|

---

## ▶️ Como Rodar o Projeto

### Pré-requisitos

- Java 21+
- Maven

### Passos

1. Clone o repositório:

```bash
git clone https://github.com/Madarinha-dev/Agendamento_de_consulta.git
cd Agendamento_de_consulta
```

2. Execute o projeto:

```bash
./mvnw spring-boot:run
```

3. Acesse a API em:

```
http://localhost:8081
```

---

## 📖 Documentação Interativa (Swagger)

Com o projeto rodando, acesse:

```
http://localhost:8081/swagger-ui/index.html
```

---

## 🗄️ Console do Banco de Dados (H2)

```
http://localhost:8081/h2-console
```

- **JDBC URL:** `jdbc:h2:mem:agendamento`
- **Usuário:** `SA`
- **Senha:** _(deixar em branco)_

---

## 📁 Estrutura do Projeto

```
src/
└── main/
    └── java/
        └── com/example/Agendamento_de_consulta/
            ├── controller/       # Endpoints REST
            ├── dto/              # Data Transfer Object
            ├── service/          # Regras de negócio
            ├── repository/       # Acesso ao banco
            ├── entity/           # Entidades JPA
            ├── exception/        # Exceções customizadas
            └── config/           # Configurações (Swagger)
```
