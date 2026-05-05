# GRUPO:
# Julio Cesar
# Fhellype Vinicius
# Randerson Albuquerque

# TEMA: Agendamento de Consultar


# REQUISITOS FUNCIONAIS:

RF01: Cadastrar/Listar Médicos (2 endpoints).

RF02: Cadastrar/Listar Pacientes (2 endpoints).

RF03: Buscar Médicos por Especialidade (1 endpoint - Filtro).

RF04: Agendar Consulta (POST): Deve validar se o médico está disponível.

RF05: Listar Consultas de um Paciente (1 endpoint).

RF06: Cancelar Consulta (PATCH/DELETE): Usar Soft Delete (requisito bônus) alterando o status para CANCELADO.

RF07: Atualizar dados do Paciente (PUT).

RF08: Buscar detalhes de uma consulta específica (GET por ID).

RF09: Listar todas as Especialidades.




# REQUISITOS NÃO FUNCIONAIS

RNF01: Interface gráfica com o usuário: o sistema terá um interface gráfica onde o usuário poderá interagir e poder executar todos os comandos para realizar sua consulta;

RNF02: o sistema ter validadores para espaços vazio ou valores nulos, Email e CPF.

RNF03: Criar um validador 'horário comercial' para garantir que consultas só sejam marcadas entre 08h e 18h.

RNF04: Garantir integridade nos retornos ou dados. 

RNF05: Tratamentos de ERROS voltados para o front-end.




# MODELAGEM DE DADOS:

01 - Medico: 
id,
nome,
crm,
email,
especialidade_id.

02 - Paciente:
id,
nome,
cpf,
telefone,
email.

03 - Consulta (A principal):
id,
medico_id,
paciente_id,
data_hora,
status: (AGENDADO, CANCELADO, REALIZADO),
observacoes.

04 - Especialidade:
id,
nome,
descricao.