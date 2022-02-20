<%@ page import="com.home.services.data.entity.Order" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Service view</title>
    <link rel="stylesheet" type="text/css" href="/css/services.css">

    <script>
        const onClickAddNewOrder = () => window.location.href = "/add-new-order";
    </script>
</head>
<body>
<div id="wrapper">
    <h1>Services</h1>

    <div>
        <button type="button" onclick="onClickAddNewOrder()" id="add-new-order">Add new order</button>
    </div>

    <table id="keywords">
        <thead>
        <tr>
            <th><span>Id</span></th>
            <th><span>Status</span></th>
            <th><span>Name</span></th>
            <th><span>Customer</span></th>
            <th><span>Sub service</span></th>
            <th><span>Expert</span></th>
            <th><span>Address</span></th>
            <th><span>Description</span></th>
            <th><span>Requested at</span></th>
            <th><span>Operation</span></th>
        </tr>
        </thead>
        <tbody>

        <c:if test="${orders.size() > 0}">
            <c:forEach items="${orders}" var="order">
                <tr>
                    <td class="lalign">${order.id}</td>
                    <td>${order.orderStatus.name()}</td>
                    <td>${order.name}</td>
                    <td>${order.customer.name}</td>
                    <td>${order.subService.name}</td>
                    <td>${order.expert.name}</td>
                    <td>${order.address.street}</td>
                    <td>${order.description}</td>
                    <td>${order.requestAt.toString()}</td>
                    <td><a href="/add-suggestion/${order.id}">Add suggestion</a></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>