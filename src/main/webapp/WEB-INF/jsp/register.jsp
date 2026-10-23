<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registro - Desafio Fitness</title>
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/img/trofeu.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>
<div class="auth-page">
    <aside class="auth-brand">
        <span class="auth-brand__label">Sistema de Desafios</span>
        <p class="auth-brand__title">Desafio<br>Fitness</p>
    </aside>

    <div class="auth-panel">
        <div class="auth-card">
            <h1>Registro</h1>
            <p>Registre-se para acessar o sistema.</p>
            
            <c:if test="${not empty sucesso}">
                <div class="alert sucesso">${sucesso}</div>
            </c:if>

            <c:if test="${not empty erro}">
                <div class="alert erro">${erro}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/register">
                <div class="form-group">
                    <label for="nome">Nome</label>
                    <input type="text" name="nome" id="nome" minlength="2" maxlength="100" required autofocus autocomplete="name">
                </div>
                <div class="form-group">
                    <label for="email">E-mail</label>
                    <input type="email" id="email" name="email" maxlength="150" required autocomplete="email">
                </div>
                <div class="form-group">
                    <label for="senha">Senha</label>
                    <input type="password" id="senha" name="senha" minlength="6" maxlength="72" required autocomplete="new-password">
                </div>
                <div class="actions">
                    <button type="submit" class="btn">Registrar</button>
                </div>
                <a class="auth-card__link" href="${pageContext.request.contextPath}/login">Tem conta? Faça Login</a>
            </form>
        </div>
    </div>
</div>
</body>
</html>
