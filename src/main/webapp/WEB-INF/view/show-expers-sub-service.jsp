<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Service view</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
    <link rel="stylesheet" type="text/css" href="/css/services.css">
    <script>
        const changeLocation = (route) => window.location.href = route;
    </script>
</head>
<body>
<div id="wrapper">
    <h1>Experts [${subServiceName}]</h1>

    <table id="keywords">
        <thead>
        <tr>
            <th><span>Id</span></th>
            <th><span>Email</span></th>
            <th><span>Operation</span></th>
        </tr>
        </thead>
        <tbody>

        <jstl:if test="${experts.size() > 0}">
            <jstl:forEach items="${experts}" var="expert">
                <tr>
                    <td class="lalign">${expert.id}</td>
                    <td>${expert.email}</td>
                    <td>
                         <a href="/sub-services/remove-expert/${subServiceId}/${expert.id}" class="btn btn-danger">Remove from list</a>
                    </td>
                </tr>
            </jstl:forEach>
        </jstl:if>
        </tbody>
    </table>
</div>
</body>
</html>