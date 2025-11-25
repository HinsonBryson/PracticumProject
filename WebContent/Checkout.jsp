<%@ page import="java.util.*, com.shashi.srv.CheckoutSrv.CartItem" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
    <title>Checkout</title>
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
</head>

<body style="padding:20px;">

<jsp:include page="header.jsp" />

<h2 class="text-center">Checkout Summary</h2>

<%
    List<CartItem> cart = (List<CartItem>) request.getAttribute("cart");
    Double originalTotal = (Double) request.getAttribute("originalTotal");
    Double totalDiscount = (Double) request.getAttribute("totalDiscount");
    Double finalTotal = (Double) request.getAttribute("finalTotal");
%>

<% if (cart == null || cart.isEmpty()) { %>
    <div class="alert alert-warning text-center">
        Your cart is empty.
    </div>
<% } else { %>

<table class="table table-bordered table-striped">
    <thead>
    <tr>
        <th>Product</th>
        <th>Unit Price</th>
        <th>Qty</th>
        <th>Original</th>
        <th>Discount</th>
        <th>Final</th>
    </tr>
    </thead>

    <tbody>
    <% for (CartItem item : cart) { %>
        <tr>
            <td><%= item.name %></td>
            <td>$<%= String.format("%.2f", item.price) %></td>
            <td><%= item.qty %></td>
            <td>$<%= String.format("%.2f", item.original) %></td>
            <td>$<%= String.format("%.2f", item.discount) %></td>
            <td><strong>$<%= String.format("%.2f", item.finalAmt) %></strong></td>
        </tr>
    <% } %>
    </tbody>
</table>

<hr>

<h3>Totals</h3>
<p><strong>Original Total:</strong> $<%= String.format("%.2f", originalTotal) %></p>
<p><strong>Total Discount:</strong> $<%= String.format("%.2f", totalDiscount) %></p>
<p><strong>Final Total:</strong> $<%= String.format("%.2f", finalTotal) %></p>

<br>

<form action="CheckoutSrv" method="post" class="text-center">
    <button type="submit" class="btn btn-success btn-lg">Confirm Purchase</button>
</form>

<% } %>

<jsp:include page="footer.html" />

</body>
</html>
