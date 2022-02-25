<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!doctype html>
<html lang="en">
<head>
<meta charset="UTF-8">
 <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
 <meta http-equiv="X-UA-Compatible" content="ie=edge">
 <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
 <link rel="stylesheet" type="text/css" href="/css/add-new-order.css" />
 <title>Add new order</title>
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
    <div>
        Add order: ${result}
    </div>
</jstl:if>

 <h3>Add new order</h3>
<%--@elvariable id="dtoAddNewOrder" type="com.home.services.dto.DTOAddOrder"--%>
<form:form action="/add-new-order" method="post" modelAttribute="dtoAddNewOrder">
 <label>
  <input type="text" class="form-control" name="name" placeholder="Order name" />
 </label>
 <label>
  <input type="text" class="form-control" name="price" placeholder="Order price" />
 </label>
 <br />
 <label>
   <input type="text" class="form-control" name="street" placeholder="street" />
 </label>
 <label>
   <input type="text" class="form-control" name="alley" placeholder="alley" />
 </label>
 <br />
 <label>
   <input type="text" class="form-control" name="postalCode" placeholder="Postal code" />
 </label>
 <label>
     <jstl:if test="${hdrRole == 'customer'}">
        <input type="text" class="form-control" name="emailCustomer" placeholder="email customer" value="${userEmail}" />
     </jstl:if>
     <jstl:if test="${hdrRole != 'customer'}">
        <input type="text" class="form-control" name="emailCustomer" placeholder="email customer" />
     </jstl:if>
 </label>
  <br />
 <select class="form-select" name="subServiceName">
  <jstl:if test="${subServiceNames.size() > 0}">
       <jstl:forEach items="${subServiceNames}" var="name">
             <option value="${name}">${name}</option>
       </jstl:forEach>
  </jstl:if>
 </select>
  <br />
   <br />
  <label>
  <textarea name="description" class="form-control" placeholder="Order description" rows="3"></textarea>
 </label>
   <br />
 <input type="submit" class="btn btn-primary" value="Add new order" name="add" />
</form:form>
</div>
</body>
</html>