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
        const onClickAddNewOrder = () => window.location.href = "/add-comment/${orderId}";
    </script>
</head>
<body>
<div id="wrapper">
    <h1>Comments [${orderName}]</h1>

    <div>
        <button type="button" onclick="onClickAddNewOrder()" class="btn btn-outline-primary waves-effect" id="add-new-order">Add new comment</button>
    </div>
    <br />
    <table id="keywords">
        <thead>
        <tr>
            <th><span>Id</span></th>
            <th><span>User</span></th>
            <th><span>Comment</span></th>
            <th><span>Created at</span></th>
            <th><span>Operation</span></th>
        </tr>
        </thead>
        <tbody>

        <jstl:if test="${comments.size() > 0}">
            <jstl:forEach items="${comments}" var="comment">
                <tr>
                    <td class="lalign">${comment.id}</td>
                    <td>${comment.user}</td>
                    <td>${comment.text}</td>
                    <td>${comment.createdAt}</td>
                    <td>
                        <a href="/remove-comment/${comment.id}" class="btn btn-danger">Remove</a>
                    </td>
                </tr>
            </jstl:forEach>
        </jstl:if>
        </tbody>
    </table>
</div>
</body>
</html>