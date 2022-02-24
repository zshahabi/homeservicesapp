<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Search users</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
    <link rel="stylesheet" type="text/css" href="/css/add-new-order.css" />
</head>
<body>
<jsp:include page="header.jsp" />
<div class="mb-3 main center form-group card">
    <h3>Search users</h3>
    <jstl:if test="${error != null}">
        <div class="alert alert-danger" role="alert">
                ${error}
        </div>
    </jstl:if>
    <%--@elvariable id="searchUsers" type="com.home.services.dto.DTOSearchUser"--%>
  <form:form action="/users/${role}/search" method="post" modelAttribute="searchUsers">
     <label>
           <input type="text" class="form-control" name="name" placeholder="Name" />
     </label>
     <label>
           <input type="text" class="form-control" name="family" placeholder="Family" />
     </label>
      <br />
      <label>
           <input type="email" class="form-control" name="email" placeholder="Email" />
      </label>
      <br />
      <label>
           <select class="form-control" name="userStatus">
               <option>New user</option>
               <option>Waiting accept</option>
               <option>Accepted</option>
           </select>
      </label>
      <br />
      <input type="submit" class="btn btn-primary" name="submit" value="Search user" />
  </form:form>
</div>
</body>
</html>