<%@ page import="com.home.services.dto.DTOShowSuggestion" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Service view</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
    <link rel="stylesheet" type="text/css" href="/css/services.css">
</head>
<body>
<div id="wrapper">
    <h1>Suggestion [${orderName}]</h1>
    <jstl:if test="${error != null}">
        <div class="alert alert-danger" role="alert">
                ${error}
        </div>
    </jstl:if>
    <table id="keywords">
        <thead>
        <tr>
            <th><span>Id</span></th>
            <th><span>Suggestion</span></th>
            <th><span>Price</span></th>
            <th><span>Time do</span></th>
            <th><span>Start time</span></th>
            <th><span>Expert email</span></th>
            <th><span>The operation</span></th>
        </tr>
        </thead>
        <tbody>

        <jstl:if test="${showSuggestions.size() > 0}">
            <jstl:forEach items="${showSuggestions}" var="showSuggestion">
                <tr>
                    <td class="lalign">${showSuggestion.id}</td>
                    <td>${showSuggestion.suggestion}</td>
                    <td>${showSuggestion.price}</td>
                    <td>${showSuggestion.timeDo}</td>
                    <td>${showSuggestion.startTime}</td>
                    <td>${showSuggestion.expertEmail}</td>
                    <td>
                        <a href="/accept-suggestion/${showSuggestion.expertId}/${showSuggestion.orderId}/${showSuggestion.id}" class="btn btn-info">Accept</a>
                        <a href="/remove-suggestion/${showSuggestion.expertId}/${showSuggestion.id}" class="btn btn-danger">Remove</a>
                    </td>
                </tr>
            </jstl:forEach>
        </jstl:if>
        </tbody>
    </table>
</div>
</body>
</html>