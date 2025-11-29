<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.shashi.service.impl.*, com.shashi.beans.*, java.util.*" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cart Details</title>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/changes.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>

<body style="background-color:#E6F9E6;">

<%
String userName = (String) session.getAttribute("username");
if (userName == null) {
    response.sendRedirect("login.jsp?message=Please login first");
    return;
}

// handle + / - buttons
String action = request.getParameter("add");
if (action != null) {
    CartServiceImpl cs = new CartServiceImpl();
    String pid = request.getParameter("pid");
    int qty = Integer.parseInt(request.getParameter("qty"));
    int avail = Integer.parseInt(request.getParameter("avail"));

    if ("1".equals(action)) {
        if (qty + 1 <= avail) {
            cs.addProductToCart(userName, pid, 1);
        }
    }
    if ("0".equals(action)) {
        cs.removeProductFromCart(userName, pid);
    }
}
%>

<jsp:include page="header.jsp" />

<div class="container">
    <h2 class="text-center" style="color:green;">Cart Items</h2>

    <table class="table table-hover">
        <thead style="background-color:#186188;color:white;">
        <tr>
            <th>Picture</th>
            <th>Product</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Add</th>
            <th>Remove</th>
            <th>Amount</th>
        </tr>
        </thead>

        <tbody>
        <%
            CartServiceImpl cart = new CartServiceImpl();
            List<CartBean> items = cart.getAllCartItems(userName);
            double total = 0;

            for (CartBean c : items) {
                ProductBean p = new ProductServiceImpl().getProductDetails(c.getProdId());
                int q = c.getQuantity();
                double amount = p.getProdPrice() * q;
                total += amount;
        %>

        <tr>
            <td><img src="ShowImage?pid=<%=p.getProdId()%>" width="50" height="50"></td>
            <td><%=p.getProdName()%></td>
            <td><%=p.getProdPrice()%></td>

            <td>
                <form action="UpdateToCart" method="post">
                    <input type="hidden" name="pid" value="<%=p.getProdId()%>">
                    <input type="number" name="pqty" value="<%=q%>" min="0" style="width:70px;">
                    <button class="btn btn-sm btn-primary">Update</button>
                </form>
            </td>

            <td>
                <a href="cartDetails.jsp?add=1&pid=<%=p.getProdId()%>&qty=<%=q%>&avail=<%=p.getProdQuantity()%>">
                    <i class="fa fa-plus"></i>
                </a>
            </td>

            <td>
                <a href="cartDetails.jsp?add=0&pid=<%=p.getProdId()%>&qty=<%=q%>&avail=<%=p.getProdQuantity()%>">
                    <i class="fa fa-minus"></i>
                </a>
            </td>

            <td><%=amount%></td>
        </tr>

        <% } %>

        <tr style="background-color:gray;color:white;">
            <td colspan="6" class="text-center">Total Amount</td>
            <td><%=total%></td>
        </tr>

        <% if (total > 0) { %>
        <tr style="background-color:gray;color:white;">
            <td colspan="4"></td>
            <td>
                <a href="userHome.jsp" class="btn btn-dark">Cancel</a>
            </td>
            <td colspan="2">
                <!-- Call CheckoutSrv -->
                <form action="CheckoutSrv" method="get">
                    <button class="btn btn-primary">Pay Now</button>
                </form>
            </td>
        </tr>
        <% } %>

        </tbody>
    </table>
</div>

<jsp:include page="footer.html"/>
</body>
</html>
