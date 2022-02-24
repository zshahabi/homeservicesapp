<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Order payment</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />

</head>
<body>
<jsp:include page="header.jsp" />
  <jstl:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
            ${error}
    </div>
</jstl:if>
<jstl:if test="${result}"> <div class="alert alert-success" role="alert"></jstl:if>
<jstl:if test="${!result}"> <div class="alert alert-danger" role="alert"></jstl:if>
    Payment: ${result}
</div>
</body>
</html>