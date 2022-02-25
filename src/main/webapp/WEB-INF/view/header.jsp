<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    const back = (when) =>
    {
        if (when === "back") history.back();
        else changeLocation(when)
    };

    const changeLocation = (location) => window.location.href = location;
</script>

<style>
    header
    {
        margin: 15px;
        background-color: #eee;
        border-radius: 10px;
    }

    .btn-back
    {
        margin: 5px;
    }
</style>
<header class="p-5 text-white">

<div>

    <button type="button" class="btn btn-dark float-right btn-back" onclick='back("${hdrBack}")'>Back</button>
    <button type="button" class="btn btn-dark float-right btn-back" onclick='changeLocation("/home")'>Home</button>

    <button type="button" class="btn btn-dark float-right btn-back" onclick='changeLocation("/my-profile")'>My profile</button>

    <jstl:if test="${hdrIsLogin && hdrRole == 'admin'}">
        <button class="btn btn-outline-primary" type="button" onclick="changeLocation('/users/expert');">Experts info</button>
        <button class="btn btn-outline-primary" type="button" onclick="changeLocation('/users/customer');">Customer info</button>
    </jstl:if>

    <button class="btn btn-outline-primary" type="button" onclick="changeLocation('/service-view');">Services</button>

    <jstl:if test="${hdrIsLogin}">
      <button class="btn btn-outline-primary" type="button" onclick="changeLocation('/add-new-order');">Add order</button>
    </jstl:if>
    <jstl:if test="${!hdrIsLogin}">
        <button class="btn btn-outline-primary" type="button" onclick="changeLocation('/login');">Login</button>
        <button class="btn btn-outline-primary" type="button" onclick="changeLocation('/register');">Register</button>
    </jstl:if>
</div>

</header>