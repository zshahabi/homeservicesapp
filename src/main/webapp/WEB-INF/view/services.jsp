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
    <h1>Services</h1>

    <div>
        <button type="button" onclick="changeLocation('/add-new-order')" class="btn btn-outline-primary waves-effect" id="add-new-order">Add new order</button>
        <button type="button" onclick="changeLocation('/add-subservice')" class="btn btn-outline-primary waves-effect" id="add-sub-service">Add sub service</button>
        <button type="button" onclick="changeLocation('/add-main-service')" class="btn btn-outline-primary waves-effect" id="add-main-service">Add main service</button>
        <button type="button" onclick="changeLocation('/sub-services')" class="btn btn-outline-primary waves-effect" id="sub-services">Sub services</button>
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
            <jstl:forEach items="${orders}" var="subService">
                <tr>
                    <td class="lalign">${subService.id}</td>
                    <td>${subService.orderStatus.name()}</td>
                    <td>${subService.name}</td>
                    <td>${subService.customer.name}</td>
                    <td>${subService.subService.name}</td>
                    <td>${subService.expert.name}</td>
                    <td>${subService.address.street}</td>
                    <td>${subService.description}</td>
                    <td>${subService.requestAt.toString()}</td>
                    <td>
                        <jstl:if test="${role == 'admin' || role == 'expert'}">
                             <a href="/add-suggestion/${subService.id}" class="btn btn-success">Add suggestion</a>
                             <a href="/show-suggestion/${subService.id}" class="btn btn-primary">Show suggestion</a>
                             <a href="/order-payment/${subService.id}" class="btn btn-info">Payment</a>
                        </jstl:if>
                        <a href="/order-comments/${subService.id}" class="btn btn-primary">Comment</a>
                    </td>
                </tr>
            </jstl:forEach>
        </jstl:if>
        </tbody>
    </table>
</div>
</body>
</html>