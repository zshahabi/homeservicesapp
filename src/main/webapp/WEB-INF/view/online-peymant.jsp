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
    <script>

        let m = 10 , s = 0;
        window.timerInterval = setInterval(() =>
        {
            s--;
            if (s <= 0 && m > 0)
            {
                m--;
                s = 59;
            }

            if (s < 0) s = 0;
            if (m < 0) m = 0;

            if (m === 0 && s === 0)
            {
                window.location.href = "/service-view";
                window.clearTimeout(window.timerInterval);
            }
            document.getElementById("counter").innerHTML = toStringTimer(m) + ":" + toStringTimer(s);
        } , 1000);

        const toStringTimer = (sm) => (sm < 10 ? "0" + sm : sm.toString());
    </script>
    <style>
        #counter
        {
            margin: 10px;
            text-align: center;
            font-family: monospace;
            font-size: 37px;
        }
    </style>
</head>
<body onload="timer()">
<jsp:include page="header.jsp" />
<div class="mb-3 main center form-group card">
        <h3>Payment [${orderName}]</h3>
        <jstl:if test="${price > 0}">
            <span id="counter">10:00</span>
            <h5>Price is ${price}</h5>
            <%--@elvariable id="payment" type="com.home.services.dto.DTOPayment"--%>
            <form:form action="/customer-order-payment/${orderId}" method="post" modelAttribute="payment">
                 <label>
                     <input type="text" class="form-control" name="cardName" placeholder="Card number" />
                 </label>
                 <br />
                 <label>
                       <input type="text" class="form-control" name="cvv2" placeholder="CVV2" />
                 </label>
                  <br />
                  <label>
                      <input type="text" class="form-control" name="date" placeholder="Date" value="00/00" />
                  </label>
                  <br />
                  <input type="submit" class="btn btn-primary" name="submit" value="Payment" />
            </form:form>
      </jstl:if>
    <jstl:if test="${price <= 0}">
        <div class="alert alert-danger" role="alert">
             Price is null
        </div>
    </jstl:if>
</div>
</body>
</html>