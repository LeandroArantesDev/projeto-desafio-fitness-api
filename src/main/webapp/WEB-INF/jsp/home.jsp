<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@
taglib prefix="c" uri="jakarta.tags.core" %> <%@
taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Home - MVC Aula</title>
      <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/img/trofeu.svg" />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/estilo.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/home.css"
    />
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
        <h1>Painel</h1>
      </div>

      <div class="grid-cards">
        <c:if test="${usuarioLogado.tipo == 'admin'}">
          <a class="menu-card" href="${pageContext.request.contextPath}/usuarios">
            <strong>Usuarios</strong>
            <span>Listar, cadastrar, editar e excluir usuarios.</span>
          </a>
        </c:if>
        <a class="menu-card" href="${pageContext.request.contextPath}/desafios">
          <strong>Desafios</strong>
          <span>Ver desafios que participo e que eu criei.</span>
        </a>
      </div>

      <h2>Todos os desafios</h2>
      <div class="grid-cards">
        <c:choose>
          <c:when test="${empty desafios}">
            <p class="empty">Nenhum desafio cadastrado.</p>
          </c:when>
          <c:otherwise>
            <c:forEach var="desafio" items="${desafios}">
              <c:if test="${desafio.status == 'ativo'}">
                <article class="challenge-card">
                  <span class="challenge-card__label">${desafio.categoria}</span>
                  <strong class="challenge-card__title">${desafio.nome}</strong>
                  <span class="challenge-card__stat">${desafio.metaTotal} <small>${desafio.unidadeMedida}</small></span>
                  <span class="challenge-card__period">${fn:substring(desafio.dataInicio, 8, 10)}/${fn:substring(desafio.dataInicio, 5, 7)}/${fn:substring(desafio.dataInicio, 0, 4)} a ${fn:substring(desafio.dataFim, 8, 10)}/${fn:substring(desafio.dataFim, 5, 7)}/${fn:substring(desafio.dataFim, 0, 4)}</span>
                  <c:choose>
                    <c:when test="${desafio.participando}">
                      <div class="challenge-card__progress">
                        <div class="challenge-card__progress-header">
                          <span>Seu progresso</span>
                          <strong>${desafio.progressoAtual} / ${desafio.metaTotal} ${desafio.unidadeMedida}</strong>
                        </div>
                        <div class="progress-track" role="progressbar" aria-valuenow="${desafio.progressoPercentual}" aria-valuemin="0" aria-valuemax="100">
                          <span class="progress-fill" style="width: ${desafio.progressoPercentual}%"></span>
                        </div>
                        <span class="challenge-card__progress-percent">${desafio.progressoPercentual}% concluído</span>
                      </div>
                      <a class="btn btn-secondary" href="${pageContext.request.contextPath}/progresso?desafioId=${desafio.id}">Ver progresso</a>
                    </c:when>
                    <c:otherwise>
                      <a class="btn" href="${pageContext.request.contextPath}/desafios?acao=participar&id=${desafio.id}">Participar</a>
                    </c:otherwise>
                  </c:choose>
                </article>
              </c:if>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>
    </main>
  </body>
</html>
