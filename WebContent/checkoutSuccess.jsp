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
                        Thank you for your purchase! Your order has been successfully processed.
                    </p>

                    <hr/>

                    <!-- Since CheckoutSrv redirects, totals will NOT exist -->
                    <p class="text-center">
                        A confirmation email has been sent (if configured).  
                        You can continue shopping or return to the home page.
                    </p>

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
