<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${empty desafio.id}">Novo desafio</c:when>
            <c:otherwise>Editar desafio</c:otherwise>
        </c:choose>
        - MVC Aula
    </title>
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/img/trofeu.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/desafios-form.css">
</head>
<body>
    <header class="topbar">
      <div class="container">
        <a class="topbar__brand ${pageContext.request.servletPath == '/home' ? 'active' : ''}" href="${pageContext.request.contextPath}/home"><svg class="topbar__icon" data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M16.5 18.75h-9m9 0a3 3 0 0 1 3 3h-15a3 3 0 0 1 3-3m9 0v-3.375c0-.621-.503-1.125-1.125-1.125h-.871M7.5 18.75v-3.375c0-.621.504-1.125 1.125-1.125h.872m5.007 0H9.497m5.007 0a7.454 7.454 0 0 1-.982-3.172M9.497 14.25a7.454 7.454 0 0 0 .981-3.172M5.25 4.236c-.982.143-1.954.317-2.916.52A6.003 6.003 0 0 0 7.73 9.728M5.25 4.236V4.5c0 2.108.966 3.99 2.48 5.228M5.25 4.236V2.721C7.456 2.41 9.71 2.25 12 2.25c2.291 0 4.545.16 6.75.47v1.516M7.73 9.728a6.726 6.726 0 0 0 2.748 1.35m8.272-6.842V4.5c0 2.108-.966 3.99-2.48 5.228m2.48-5.492a46.32 46.32 0 0 1 2.916.52 6.003 6.003 0 0 1-5.395 4.972m0 0a6.726 6.726 0 0 1-2.749 1.35m0 0a6.772 6.772 0 0 1-3.044 0"></path></svg> MVC Aula</a>
        <nav>
          <a class="${pageContext.request.servletPath == '/home' ? 'active' : ''}" href="${pageContext.request.contextPath}/home">Home</a>
          <a class="${pageContext.request.servletPath == '/desafios' || pageContext.request.servletPath == '/progresso' ? 'active' : ''}" href="${pageContext.request.contextPath}/desafios">Meus Desafios</a>
          <c:if test="${usuarioLogado.tipo == 'admin'}">
            <a class="${pageContext.request.servletPath == '/usuarios' ? 'active' : ''}" href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
          </c:if>
          <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </nav>
      </div>
    </header>

<main class="container">
    <div class="page-header">
        <h1>
            <c:choose>
                <c:when test="${empty desafio.id}">Novo desafio</c:when>
                <c:otherwise>Editar desafio</c:otherwise>
            </c:choose>
        </h1>
    </div>

    <div class="card">
            <c:if test="${not empty sucesso}">
                <div class="alert sucesso">${sucesso}</div>
            </c:if>

            <c:if test="${not empty erro}">
                <div class="alert erro">${erro}</div>
            </c:if>

        <form method="post" action="${pageContext.request.contextPath}/desafios">
            <input type="hidden" name="acao" value="salvar">
            <input type="hidden" name="id" value="${desafio.id}">

            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" value="${desafio.nome}" required>
            </div>

            <div class="form-group">
                <label for="descricao">Descricao</label>
                <textarea id="descricao" name="descricao">${desafio.descricao}</textarea>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="categoria">Categoria</label>
                    <select id="categoria" name="categoria" required>
                        <option value="corrida" <c:if test="${desafio.categoria == 'corrida'}">selected</c:if>>Corrida</option>
                        <option value="musculacao" <c:if test="${desafio.categoria == 'musculacao'}">selected</c:if>>Musculacao</option>
                        <option value="ciclismo" <c:if test="${desafio.categoria == 'ciclismo'}">selected</c:if>>Ciclismo</option>
                        <option value="flexoes" <c:if test="${desafio.categoria == 'flexoes'}">selected</c:if>>Flexoes</option>
                        <option value="habito" <c:if test="${desafio.categoria == 'habito'}">selected</c:if>>Habito</option>
                        <option value="outro" <c:if test="${desafio.categoria == 'outro' or empty desafio.categoria}">selected</c:if>>Outro</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="tipoMeta">Tipo de meta</label>
                    <select id="tipoMeta" name="tipoMeta" required>
                        <option value="repeticoes" <c:if test="${desafio.tipoMeta == 'repeticoes'}">selected</c:if>>Repeticoes</option>
                        <option value="distancia_km" <c:if test="${desafio.tipoMeta == 'distancia_km'}">selected</c:if>>Distancia (km)</option>
                        <option value="tempo_minutos" <c:if test="${desafio.tipoMeta == 'tempo_minutos'}">selected</c:if>>Tempo (minutos)</option>
                        <option value="dias_seguidos" <c:if test="${desafio.tipoMeta == 'dias_seguidos'}">selected</c:if>>Dias seguidos</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="metaTotal">Meta total</label>
                    <input type="number" id="metaTotal" name="metaTotal" step="0.01" min="0" value="${desafio.metaTotal}" required>
                </div>

                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="rascunho" <c:if test="${desafio.status == 'rascunho'}">selected</c:if>>Rascunho</option>
                        <option value="ativo" <c:if test="${desafio.status == 'ativo' or empty desafio.status}">selected</c:if>>Ativo</option>
                        <option value="encerrado" <c:if test="${desafio.status == 'encerrado'}">selected</c:if>>Encerrado</option>
                        <option value="cancelado" <c:if test="${desafio.status == 'cancelado'}">selected</c:if>>Cancelado</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="dataInicio">Data inicio</label>
                    <input type="date" id="dataInicio" name="dataInicio"
                           value="${empty desafio.dataInicio ? '' : fn:substring(desafio.dataInicio, 0, 10)}" required>
                </div>

                <div class="form-group">
                    <label for="dataFim">Data fim</label>
                    <input type="date" id="dataFim" name="dataFim"
                           value="${empty desafio.dataFim ? '' : fn:substring(desafio.dataFim, 0, 10)}" required>
                </div>
            </div>

            <div class="actions">
                <button type="submit" class="btn">Salvar</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/desafios">Cancelar</a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
