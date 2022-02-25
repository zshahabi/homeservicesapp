<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile ${user.name}</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
    <link rel="stylesheet" type="text/css" href="/css/services.css">
    <script>
        const changeLocation = (route) => window.location.href = route;
    </script>
</head>
<body>
<jsp:include page="header.jsp" />
<div id="wrapper">
    <h1>Profile ${user.name}</h1>

    <table id="keywords">
        <thead>
        <tr>
            <th><span>Id</span></th>
            <th><span>Name</span></th>
            <th><span>Family</span></th>
            <th><span>Email</span></th>
            <th><span>Account credit</span></th>
            <th><span>Street</span></th>
            <th><span>alley</span></th>
            <th><span>Postal code</span></th>
            <th><span>Role</span></th>
            <th><span>Status</span></th>
            <th><span>Created at</span></th>
           <jstl:if test="${hdrRole == 'expert'}">
               <th><span>Rating</span></th>
               <th><span>Image</span></th>
           </jstl:if>
        </tr>
        </thead>
        <tbody>
            <tr>
                <td class="lalign">${user.id}</td>
                <td class="lalign">${user.name}</td>
                <td class="lalign">${user.family}</td>
                <td class="lalign">${user.email}</td>
                <td class="lalign">${user.accountCredit}</td>
                <td class="lalign">${user.street}</td>
                <td class="lalign">${user.alley}</td>
                <td class="lalign">${user.postalCode}</td>
                <td class="lalign">${hdrRole}</td>
                <td class="lalign">${user.status}</td>
                <td class="lalign">${user.createdAt}</td>
                 <jstl:if test="${hdrRole == 'expert'}">
                     <td class="lalign">${user.rating}</td>
                     <td><img src="/expert-image/${user.id}" width="50" height="50" alt="Expert image" /></td>
                 </jstl:if>
            </tr>
        </tbody>
    </table>
</div>
</body>
</html>