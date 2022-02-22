<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Login page</title>
    <link href="/css/home.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="login-wrap center">
    <h2>Login</h2>

    <div class="form">
        <form action="/login" method="post">
            <input type="email" placeholder="Email" name="email" />
            <input type="password" placeholder="Password" name="password" />
            <input type="submit" value="Sign in" name="submit" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
        <a href="/register"><p> Don't have an account? Register </p></a>
    </div>
</div>
</body>
</html>
