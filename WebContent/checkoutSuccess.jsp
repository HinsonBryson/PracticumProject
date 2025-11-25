<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Checkout Successful</title>

    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
</head>

<body style="background-color: #F2FFF2;">

<jsp:include page="header.jsp" />

<div class="container" style="margin-top: 40px;">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">

            <div class="panel panel-success">
                <div class="panel-heading text-center">
                    <h2>Order Completed Successfully!</h2>
                </div>

                <div class="panel-body">

                    <p class="lead text-center">
                        Thank you for your purchase! Your order has been processed.
                    </p>

                    <hr/>

                    <%-- THESE ATTRIBUTES ARE SET ONLY IF forwarded (not redirected) --%>
                    <%
                        Object oTotal = request.getAttribute("originalTotal");
                        Object oDisc  = request.getAttribute("totalDiscount");
                        Object oFinal = request.getAttribute("finalTotal");
                    %>

                    <% if (oTotal != null && oDisc != null && oFinal != null) { %>

                        <h3>Order Summary</h3>

                        <p><strong>Original Total:</strong> $<%= oTotal %></p>
                        <p><strong>Discount Applied:</strong> $<%= oDisc %></p>
                        <p><strong>Final Amount Paid:</strong> $<%= oFinal %></p>

                    <% } else { %>

                        <p class="text-warning">
                            Order totals were not forwarded.  
                            (This happens when CheckoutSrv redirects instead of forward.)
                        </p>

                    <% } %>

                    <hr/>

                    <div class="text-center">
                        <a href="index.jsp" class="btn btn-primary">Return to Home</a>
                        <a href="products.jsp" class="btn btn-success">Continue Shopping</a>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>

<jsp:include page="footer.html" />

</body>
</html>