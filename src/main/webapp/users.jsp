<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<div>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Email</th>
            <th>Password</th>
            <th>Date registered</th>
            <th>Enabled</th>
        </tr>
        </thead>
        <c:forEach items="${users}" var="user">
            <jsp:useBean id="user" type="ru.javawebinar.topjava.model.User"/>
            <tr>
                <td>${user.email}</td>
                <td>${user.password}</td>
                <td>${user.registered}</td>
                <td>${user.enabled}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>