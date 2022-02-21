<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Add sub service</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
    <link rel="stylesheet" type="text/css" href="/css/add-new-order.css" />
</head>
<body>
<div class="mb-3 main center form-group card">
    <jstl:if test="${error != null}">
        <div class="alert alert-danger" role="alert">
                ${error}
        </div>
    </jstl:if>
    <jstl:if test="${result != null}">
        <jstl:if test="${result}"> <div class="alert alert-success" role="alert"></jstl:if>
        <jstl:if test="${!result}"> <div class="alert alert-danger" role="alert"></jstl:if>
            Add sub service: ${result}
        </div>
</jstl:if>
    <h3>Add sub service</h3>
    <jstl:if test="${mainServices != null}">
        <%--@elvariable id="addSubService" type="com.home.services.dto.DTOAddSubService"--%>
        <form:form action="/add-subservice" method="post" modelAttribute="addSubService">
             <label>
                 <input type="text" class="form-control" name="name" placeholder="Name" />
             </label>
             <br />
             <label>
                   <input type="text" class="form-control" name="price" placeholder="Price" />
             </label>
              <br />
              <label>
                  <textarea type="text" class="form-control" name="description" placeholder="Description" rows="5"></textarea>
              </label>
              <br />
              <select name="mainServiceId">
                  <jstl:forEach items="${mainServices}" var="mainService">
                      <option value="${mainService.id}">${mainService.name}</option>
                  </jstl:forEach>
              </select>
              <input type="submit" class="btn btn-primary" name="submit" value="Add subservice" />
        </form:form>
  </jstl:if>
<jstl:if test="${mainServices == null}">
    <div class="alert alert-danger" role="alert">
         Not found main service
    </div>
</jstl:if>
</div>
</body>
</html>