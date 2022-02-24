<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sub services</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
    <link rel="stylesheet" type="text/css" href="/css/services.css">
    <jstl:if test="${role == 'admin'}">
        <script>
            const onClickAddSubService = () => window.location.href = "/add-subservice";
        </script>
    </jstl:if>
</head>
<body>
<div id="wrapper">
    <h1>Sub service</h1>

    <jstl:if test="${role == 'admin'}">
        <div>
            <button type="button" onclick="onClickAddSubService()" class="btn btn-outline-primary waves-effect" id="add-sub-service">Add sub service</button>
        </div>
    </jstl:if>
    <br />
    <table id="keywords">
        <thead>
        <tr>
            <th><span>Id</span></th>
            <th><span>Name></th>
            <th><span>price</span></th>
            <th><span>Description</span></th>
            <th><span>createdAt</span></th>
            <jstl:if test="${role == 'admin'}">
                <th><span>Operation</span></th>
            </jstl:if>
        </tr>
        </thead>
        <tbody>

        <jstl:if test="${subServices.size() > 0}">
            <jstl:forEach items="${subServices}" var="subService">
                <tr>
                    <td class="lalign">${subService.id}</td>
                    <td>${subService.name}</td>
                    <td>${subService.price}</td>
                    <td>${subService.description}</td>
                    <td>${subService.createdAt}</td>
                    <jstl:if test="${role == 'admin'}">
                        <td>
                             <a href="/sub-services/add-expert/${subService.id}" class="btn btn-success">Add Expert</a>
                             <a href="/sub-services/show-experts/${subService.id}" class="btn btn-primary">Show Experts</a>
                        </td>
                    </jstl:if>
                </tr>
            </jstl:forEach>
        </jstl:if>
        </tbody>
    </table>
</div>
</body>
</html>