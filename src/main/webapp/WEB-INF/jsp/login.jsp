<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - MVC Aula</title>
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/img/trofeu.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<div class="auth-page">
    <aside class="auth-brand">
        <span class="auth-brand__label">Sistema de Desafios</span>
        <p class="auth-brand__title">Desafio<br>Fitness</p>
    </aside>

    <div class="auth-panel">
        <div class="auth-card">
            <h1>Login</h1>
            <p>Entre com login e senha para acessar o sistema.</p>

            <c:if test="${not empty sucesso}">
                <div class="alert sucesso">${sucesso}</div>
            </c:if>

            <c:if test="${not empty erro}">
                <div class="alert erro">${erro}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/login">
                <div class="form-group">
                    <label for="email">E-mail</label>
                    <input type="email" id="email" name="email" maxlength="150" required autofocus autocomplete="email">
                </div>
                <div class="form-group">
                    <label for="senha">Senha</label>
                    <input type="password" id="senha" name="senha" minlength="6" maxlength="72" required autocomplete="current-password">
                </div>
                <div class="actions">
                    <button type="submit" class="btn">Entrar</button>
                </div>
                <a class="auth-card__link" href="${pageContext.request.contextPath}/register">Não tem conta? Registre-se</a>
            </form>
        </div>
    </div>
</div>
</body>
</html>
