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
<jsp:include page="header.jsp" />
<div id="wrapper">
    <h1>Services</h1>

    <jstl:if test="${role == 'ADMIN'}">
        <div>
            <button type="button" onclick="changeLocation('/add-new-order')" class="btn btn-outline-primary waves-effect" id="add-new-order">Add new order</button>
            <button type="button" onclick="changeLocation('/add-subservice')" class="btn btn-outline-primary waves-effect" id="add-sub-service">Add sub service</button>
            <button type="button" onclick="changeLocation('/add-main-service')" class="btn btn-outline-primary waves-effect" id="add-main-service">Add main service</button>
            <button type="button" onclick="changeLocation('/sub-services')" class="btn btn-outline-primary waves-effect" id="sub-services">Sub services</button>
        </div>
    </jstl:if>

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
                        <jstl:if test="${role == 'admin' || role == 'expert'}">
                             <a href="/set-done-order/${order.id}" class="btn btn-outline-success">Done</a>
                             <a href="/add-suggestion/${order.id}" class="btn btn-success">Add suggestion</a>
                             <a href="/show-suggestion/${order.id}" class="btn btn-primary">Show suggestion</a>
                             <a href="/order-payment/${order.id}" class="btn btn-info">Payment</a>
                        </jstl:if>
                        <a href="/order-comments/${order.id}" class="btn btn-primary">Comment</a>
                        <jstl:if test="${role == 'customer' && order.orderStatus.name() == 'DONE'}">
                            <a href="/customer-order-payment/${order.id}" class="btn btn-info">Payment</a>
                        </jstl:if>
                    </td>
                </tr>
            </jstl:forEach>
        </jstl:if>
        </tbody>
    </table>
</div>
</body>
</html>