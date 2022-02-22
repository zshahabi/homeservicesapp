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
        const onClickAddNewOrder = () => window.location.href = "/add-new-order";
        const onClickAddSubService = () => window.location.href = "/add-subservice";
        const onClickAddMainService = () => window.location.href = "/add-main-service";
    </script>
</head>
<body>
<div id="wrapper">
    <h1>Services</h1>

    <div>
        <button type="button" onclick="onClickAddNewOrder()" class="btn btn-outline-primary waves-effect" id="add-new-order">Add new order</button>
        <button type="button" onclick="onClickAddSubService()" class="btn btn-outline-primary waves-effect" id="add-sub-service">Add sub service</button>
        <button type="button" onclick="onClickAddMainService()" class="btn btn-outline-primary waves-effect" id="add-main-service">Add main service</button>
    </div>
    <br />
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

        <jstl:if test="${orders.size() > 0}">
            <jstl:forEach items="${orders}" var="order">
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
                    <td>
                        <a href="/add-suggestion/${order.id}" class="btn btn-success">Add suggestion</a>
                        <a href="/show-suggestion/${order.id}" class="btn btn-primary">Show suggestion</a>
                        <a href="/order-payment/${order.id}" class="btn btn-info">Payment</a>
                    </td>
                </tr>
            </jstl:forEach>
        </jstl:if>
        </tbody>
    </table>
</div>
</body>
</html>