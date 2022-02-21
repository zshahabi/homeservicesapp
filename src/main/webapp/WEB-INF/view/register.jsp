<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Register</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
 <link rel="stylesheet" type="text/css" href="/css/add-new-order.css" />
    <script>
        const setAction = () =>
        {
            let userType = document.getElementById('user-type').value;

            userType = userType.toLowerCase();

            const registerForm = document.getElementById("register-form");

            const divImageChooser = document.getElementById("div-image-chooser");

            if (userType === "expert")
            {
                registerForm.action = "/register-expert";

                divImageChooser.innerHTML = '<label>Select image<input type="file" class="form-control" name="image" /></label>';
                registerForm.enctype = "multipart/form-data";
            }
            else if (userType === "customer")
            {
                registerForm.action = "/register-customer";
                divImageChooser.innerHTML = "";
                registerForm.removeAttribute("enctype");
            }
        }
    </script>
</head>
<body onload="setAction()">
<div class="mb-3 main center form-group card">
    <jstl:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
            ${error}
    </div>
 </jstl:if>
<jstl:if test="${result != null}">
    <jstl:if test="${result}"> <div class="alert alert-success" role="alert"></jstl:if>
    <jstl:if test="${!result}"> <div class="alert alert-danger" role="alert"></jstl:if>
        Register: ${result}
    </div>
</jstl:if>
   <h3>Register</h3>
  <%--@elvariable id="addNewUser" type="com.home.services.dto.DTORegister"--%>
  <form:form action="/register" id="register-form" method="post" modelAttribute="addNewUser">
     <label>
           <input type="text" class="form-control" name="name" placeholder="Name" />
     </label>
      <label>
           <input type="text" class="form-control" name="Family" placeholder="Family" />
      </label>
      <br />
      <label>
           <input type="email" class="form-control" name="email" placeholder="Email" />
      </label>
      <label>
           <input type="password" class="form-control" name="password" placeholder="Password" />
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
      <br />
      <select id="user-type" name="userType" onchange="setAction()">
          <option>Customer</option>
          <option>Expert</option>
      </select>
      <br />
      <div id="div-image-chooser">
            <label>
                Select image
                <input type="file" class="form-control" name="image" />
            </label>
      </div>
      <input type="submit" class="btn btn-primary" name="submit" value="Add suggestion" />
  </form:form>
</div>

</body>
</html>