<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.shashi.srv.CheckoutSrv.CartItem" %>

<!DOCTYPE html>
<html>
<head>
    <title>Checkout Summary</title>
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
</head>

<body style="padding:20px;">

<jsp:include page="header.jsp"/>

<h2 class="text-center">Checkout Summary</h2>

<%
    List<CartItem> cart = (List<CartItem>) request.getAttribute("cart");
    Double originalTotal = (Double) request.getAttribute("originalTotal");
    Double productDiscountTotal = (Double) request.getAttribute("productDiscountTotal");
    Double couponDiscountTotal = (Double) request.getAttribute("couponDiscountTotal");
    Double finalTotal = (Double) request.getAttribute("finalTotal");
    String couponCode = (String) request.getAttribute("couponCode");
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
        <th>Product Discount</th>
        <th>Coupon Discount</th>
        <th>Final</th>
    </tr>
    </thead>
    <tbody>

    <% for (CartItem c : cart) { %>
    <tr>
        <td><%= c.name %></td>
        <td>$<%= String.format("%.2f", c.price) %></td>
        <td><%= c.qty %></td>
        <td>$<%= String.format("%.2f", c.original) %></td>
        <td>$<%= String.format("%.2f", c.productDiscount) %></td>
        <td>$<%= String.format("%.2f", c.couponDiscount) %></td>
        <td><strong>$<%= String.format("%.2f", c.finalAmt) %></strong></td>
    </tr>
    <% } %>

    </tbody>
</table>

<h3>Totals</h3>
<p><strong>Original:</strong> $<%= String.format("%.2f", originalTotal) %></p>
<p><strong>Product Discounts:</strong> -$<%= String.format("%.2f", productDiscountTotal) %></p>
<p><strong>Coupon Savings:</strong> -$<%= String.format("%.2f", couponDiscountTotal) %></p>

<p style="font-size:20px;">
    <strong>Final Total: $<%= String.format("%.2f", finalTotal) %></strong>
</p>

<hr>

<form action="CheckoutSrv" method="get" class="text-center">
    <label>Enter Coupon Code:</label>
    <input type="text" name="coupon" value="<%= couponCode != null ? couponCode : "" %>" class="form-control" style="max-width:300px; margin:auto;">
    <button class="btn btn-primary" style="margin-top:10px;">Apply Coupon</button>
</form>

<br>

<form action="CheckoutSrv" method="post" class="text-center">
    <button class="btn btn-success btn-lg">Confirm Purchase</button>
</form>

<% } %>

<jsp:include page="footer.html"/>
</body>
</html>

