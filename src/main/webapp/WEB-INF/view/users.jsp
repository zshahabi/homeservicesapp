<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Users</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
    <link rel="stylesheet" type="text/css" href="/css/services.css">
        <script>
            const onClickBtnSearch = () => window.location.href = "/users/${role}/search";
        </script>
</head>
<body>
  <div id="wrapper">
     <h1>Users [${role}]</h1>
      <div>
            <button type="button" onclick="onClickBtnSearch()" class="btn btn-outline-primary waves-effect" id="search">Search</button>
      </div>

        <jstl:if test="${error != null}">
            <div class="alert alert-danger" role="alert">
                    ${error}
            </div>
        </jstl:if>
    <table id="keywords">
        <thead>
        <tr>
            <th><span>Id</span></th>
            <th><span>Name</span></th>
            <th><span>Family</span></th>
            <th><span>Email</span></th>
            <th><span>Status</span></th>
            <th><span>Account credit</span></th>
            <th><span>Street</span></th>
            <th><span>Alley</span></th>
            <th><span>Postal Code</span></th>
            <th><span>Created at</span></th>
            <jstl:if test="${role == 'expert'}">
                <th><span>Rating</span></th>
                <th><span>Sub service</span></th>
                <th><span>Image</span></th>
            </jstl:if>
            <th><span>Operation</span></th>
        </tr>
        </thead>
        <tbody>

        <jstl:if test="${users.size() > 0}">
            <jstl:forEach items="${users}" var="user">
                <tr>
                    <td class="lalign">${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.family}</td>
                    <td>${user.email}</td>
                    <td>${user.status}</td>
                    <td>${user.accountCredit}</td>
                    <td>
                        <jstl:if test="${user.street != null}">
                            ${user.street}
                        </jstl:if>
                    </td>
                    <td>
                        <jstl:if test="${user.alley != null}">
                            ${user.alley}
                        </jstl:if>
                    </td>
                    <td>
                        <jstl:if test="${user.postalCode != null}">
                            ${user.postalCode}
                        </jstl:if>
                    </td>
                    <td>${user.createdAt}</td>
                     <jstl:if test="${role == 'expert'}">
                        <td>${user.rating}</td>
                        <td>${user.subService}</td>
                        <td><img src="/expert-image/${user.id}" alt="Expert image" /></td>
                     </jstl:if>
                    <td>
<%--                        <a href="/edit-user/${user.id}" class="btn btn-info">Edit user</a>--%>
                        <a href="/remove-user/${user.id}" class="btn btn-danger">Remove user</a>
                        <jstl:if test="${user.status == 'WAITING_ACCEPT'}">
                            <a href="/accept-user/${user.id}" class="btn btn-outline-success">Accept user</a>
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