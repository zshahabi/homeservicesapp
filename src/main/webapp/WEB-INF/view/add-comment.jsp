<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Add suggestion</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
  <link rel="stylesheet" type="text/css" href="/css/add-new-order.css" />
</head>
<body>
<jsp:include page="header.jsp" />
<div class="mb-3 main center form-group card">
<jstl:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
            ${error}
    </div>
 </jstl:if>
<jstl:if test="${result != null}">
    <jstl:if test="${result}"> <div class="alert alert-success" role="alert"></jstl:if>
    <jstl:if test="${!result}"> <div class="alert alert-danger" role="alert"></jstl:if>
        Add Comment: ${result}
    </div>
</jstl:if>
<jstl:if test="${orderName != null}">
         <h3>Add comment [${orderName}]</h3>
    <%--@elvariable id="textComment" type="java.lang.String"--%>
    <form:form action="/add-comment/${orderId}" method="post" modelAttribute="textComment">
     <label>
           <textarea class="form-control" name="textComment" placeholder="Your comment" rows="5"></textarea>
     </label>
     <br />
      <input type="submit" class="btn btn-primary" name="submit" value="Add commit" />
   </form:form>
</jstl:if>
</div>
</body>
</html>