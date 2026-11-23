# projeto-desafio-fitness-api

API REST em Java (Servlets + MySQL) para desafios fitness: usuários criam desafios, participam deles e registram o progresso.

Arquitetura em camadas: **Controller → Service → DAO → MySQL**. Todas as respostas são JSON. A estrutura de pastas está explicada em [`ESTRUTURA.txt`](ESTRUTURA.txt).

## Como rodar

Gerar o build do projeto (gera `deploy/projeto-desafio-fitness.war`):

```bash
mvn clean package
```

Subir os containers (MySQL + Tomcat):

```bash
docker compose up -d
```

Parar os containers:

```bash
docker compose down
```

- API: `http://localhost:8080/projeto-desafio-fitness/api`
- MySQL: `localhost:3307` (banco `projeto_desafios_fitness`, usuário `pdf_user`, senha `pdf123`)

O `database.sql` cria as tabelas e já insere usuários de teste: `admin@gmail.com` (admin), `teste@gmail.com`, `leandro@gmail.com` e `ruan@gmail.com`, todos com a senha `password`.

## Autenticação

1. Faça login em `POST /api/login` e copie o `token` da resposta.
2. Envie o token em todas as outras requisições:

```
Authorization: Bearer <token>
```

- Rotas públicas (sem token): `POST /api/login` e `POST /api/register`.
- `/api/usuarios` só é liberada para usuários com `tipo` = `admin`.
- O token vale por 2 horas. Logout = o cliente apagar o token.

## Padrão de respostas

| Status | Quando |
|---|---|
| `200 OK` | Busca ou atualização com sucesso |
| `201 Created` | Criação com sucesso (POST) |
| `204 No Content` | Exclusão com sucesso (DELETE), sem corpo |
| `400 Bad Request` | JSON ou campos inválidos, id inválido na URL |
| `401 Unauthorized` | Sem token, token inválido/expirado ou login incorreto |
| `403 Forbidden` | Token válido, mas sem permissão (rota de admin) |
| `404 Not Found` | Recurso não existe |
| `500 Internal Server Error` | Erro inesperado no servidor |

Erros sempre vêm no formato:

```json
{ "erro": "mensagem" }
```

Datas são enviadas como `"2026-09-01"` (ou `"2026-09-01T00:00"`) e retornadas como `"2026-09-01T00:00"`.

## Endpoints

### Autenticação

| Método | URL | Token | Sucesso |
|---|---|---|---|
| POST | `/api/login` | Não | 200 |
| POST | `/api/register` | Não | 201 |

**POST /api/login**

```json
{ "email": "admin@gmail.com", "senha": "password" }
```

Resposta `200`:

```json
{ "id": 1, "token": "eyJhbGciOi...", "nome": "Admin", "tipo": "admin" }
```

E-mail ou senha errados → `401`.

**POST /api/register**: cadastro público. O usuário é criado sempre com `tipo` = `user`.

```json
{ "nome": "Maria", "email": "maria@gmail.com", "senha_hash": "123456" }
```

Resposta `201` com o usuário criado (sem a senha). Erros `400`: campo obrigatório faltando, senha com menos de 6 caracteres ou e-mail já cadastrado.

### Usuários (somente admin)

| Método | URL | Sucesso | Erros |
|---|---|---|---|
| GET | `/api/usuarios` | 200, lista | |
| GET | `/api/usuarios/{id}` | 200 | 400, 404 |
| POST | `/api/usuarios` | 201 | 400 |
| PUT | `/api/usuarios/{id}` | 200 | 400, 404 |
| DELETE | `/api/usuarios/{id}` | 204 | 400, 404 |

Body de POST/PUT:

```json
{ "nome": "Maria", "email": "maria@gmail.com", "senha_hash": "123456", "tipo": "user" }
```

- `tipo`: `admin` ou `user` (sem informar, vira `user`).
- No PUT a `senha_hash` é opcional: sem ela, a senha atual é mantida.
- A senha nunca é devolvida nas respostas.
- Não é possível excluir um usuário que criou desafios (`400`).

Exemplo de resposta (`GET /api/usuarios/1`):

```json
{
  "id": 1,
  "nome": "Admin",
  "email": "admin@gmail.com",
  "tipo": "admin",
  "criado_em": "2026-11-23T14:00",
  "atualizado_em": "2026-11-23T14:00"
}
```

### Desafios

| Método | URL | Sucesso | Erros |
|---|---|---|---|
| GET | `/api/desafios` | 200, lista | |
| GET | `/api/desafios/{id}` | 200 | 400, 404 |
| POST | `/api/desafios` | 201 | 400 |
| PUT | `/api/desafios/{id}` | 200 | 400, 404 |
| DELETE | `/api/desafios/{id}` | 204 | 400, 404 |

Body de POST/PUT:

```json
{
  "nome": "Desafio 50km",
  "descricao": "Correr 50km no mês",
  "categoria": "corrida",
  "tipo_meta": "distancia_km",
  "meta_total": 50,
  "data_inicio": "2026-12-01",
  "data_fim": "2026-12-31",
  "status": "ativo"
}
```

- Obrigatórios: `nome`, `categoria`, `tipo_meta`, `meta_total`, `data_inicio`, `data_fim`.
- `data_fim` precisa ser depois de `data_inicio`.
- `status` é opcional (padrão `ativo`).
- O `criador_id` é o usuário do token (não é enviado no body).
- Valores aceitos (definidos no banco):
  - `categoria`: `corrida`, `musculacao`, `ciclismo`, `flexoes`, `habito`, `outro`
  - `tipo_meta`: `repeticoes`, `distancia_km`, `tempo_minutos`, `dias_seguidos`
  - `status`: `ativo`, `finalizado`, `cancelado`

`GET /api/desafios` devolve todos os desafios já com os dados do usuário logado: se ele participa, quanto já registrou e o percentual da meta (máximo 100):

```json
[
  {
    "id": 1,
    "criador_id": 1,
    "nome": "Desafio 100km em Setembro",
    "descricao": "Correr ou caminhar um total acumulado de 100km durante o mês de setembro.",
    "categoria": "corrida",
    "tipo_meta": "distancia_km",
    "meta_total": 100.0,
    "unidade_medida": "km",
    "data_inicio": "2026-09-01T00:00",
    "data_fim": "2026-09-30T00:00",
    "status": "ativo",
    "participando": true,
    "progresso_atual": 17.8,
    "progresso_percentual": 17.8
  }
]
```

### Participações

| Método | URL | Sucesso | Erros |
|---|---|---|---|
| POST | `/api/participacoes` | 201 | 400 |

O usuário do token passa a participar do desafio informado:

```json
{ "desafio_id": 1 }
```

Resposta `201` com a participação criada. O `id` dela é o `participacao_id` usado para registrar progresso:

```json
{ "id": 5, "usuario_id": 2, "desafio_id": 1, "status": "em_andamento" }
```

Erros `400`: desafio não existe ou o usuário já participa.

### Progressos

| Método | URL | Sucesso | Erros |
|---|---|---|---|
| GET | `/api/progressos?desafioId={id}` | 200 | 400, 404 |
| POST | `/api/progressos` | 201 | 400 |

**GET**: histórico e total do usuário logado em um desafio. Responde `404` se ele não participa do desafio.

```json
{
  "participacao": { "id": 1, "usuario_id": 1, "desafio_id": 1, "status": "em_andamento" },
  "historico": [
    { "id": 1, "participacao_id": 1, "valor_registrado": 6.5, "observacao": "Treino matinal no parque", "data_registro": "2026-09-02T00:00" }
  ],
  "total_registrado": 17.8
}
```

**POST**: registra um novo progresso.

```json
{ "participacao_id": 1, "valor_registrado": 5.5, "observacao": "Treino leve", "data_registro": "2026-11-23" }
```

- Obrigatórios: `participacao_id`, `valor_registrado` (maior que zero) e `data_registro`.
- `observacao` é opcional.
