<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registrar progresso - MVC Aula</title>
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/img/trofeu.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/progresso-form.css">
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
        <h1>Registrar progresso — ${desafio.nome}</h1>
    </div>

    <div class="progresso-layout">
        <div class="progresso-layout__form">
            <div class="card progress-card">
                <div class="progress-card__header">
                    <span class="progress-card__label">Progresso</span>
                    <span class="progress-card__percent"><fmt:formatNumber value="${totalRegistrado / desafio.metaTotal * 100}" maxFractionDigits="1"/>%</span>
                </div>
                <div class="progress-card__track">
                    <div class="progress-card__fill" style="width: ${totalRegistrado / desafio.metaTotal * 100}%"></div>
                </div>
                <p class="progress-card__detail">
                    <strong>${totalRegistrado}</strong> de <strong>${desafio.metaTotal}</strong> ${desafio.unidadeMedida}
                </p>
            </div>

            <div class="card">
                <c:if test="${not empty sucesso}">
                    <div class="alert sucesso">${sucesso}</div>
                </c:if>

                <c:if test="${not empty erro}">
                    <div class="alert erro">${erro}</div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/progresso">
                    <input type="hidden" name="participacaoId" value="${participacao.id}">

                    <div class="form-group">
                        <label for="valorRegistrado">Valor registrado</label>
                        <input type="number" id="valorRegistrado" name="valorRegistrado" step="0.01" min="0.01" required>
                    </div>

                    <div class="form-group">
                        <label for="dataRegistro">Data do registro</label>
                        <input type="date" id="dataRegistro" name="dataRegistro" required>
                    </div>

                    <div class="form-group">
                        <label for="observacao">Observacao</label>
                        <textarea id="observacao" name="observacao"></textarea>
                    </div>

                    <div class="actions">
                        <button type="submit" class="btn">Salvar</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/desafios">Cancelar</a>
                    </div>
                </form>
            </div>
        </div>

        <div class="progresso-layout__history">
            <h2>Historico de check-ins</h2>
            <div class="table-wrap">
                <c:choose>
                    <c:when test="${empty historico}">
                        <p class="empty">Nenhum check-in registrado ainda.</p>
                    </c:when>
                    <c:otherwise>
                        <table>
                            <thead>
                            <tr>
                                <th>Data</th>
                                <th>Valor</th>
                                <th>Observacao</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${historico}">
                                <tr>
                                    <td>${fn:substring(item.dataRegistro, 8, 10)}/${fn:substring(item.dataRegistro, 5, 7)}/${fn:substring(item.dataRegistro, 0, 4)}</td>
                                    <td><strong>${item.valorRegistrado}</strong> <span class="unit">${desafio.unidadeMedida}</span></td>
                                    <td>${item.observacao}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>
</body>
</html>
