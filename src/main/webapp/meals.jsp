<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>

<head>
    <title>Check calories</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <div align="center">
    <h2>User meals</h2>
        <h4><a href="mealEdit.jsp?action=add">Add new meal</a></h4>
    <table>
        <tr align="left">
            <th>DateTime</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach var="meal" items="${mealList}">
            <tr style="color: ${meal.excess ? 'red':'green'}">
                <td>
                   <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="date"/>
                   <fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm" />
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=edit&id=${meal.id}"/>Edit</td>
                <td><a href="meals?action=delete&id=${meal.id}"/>Delete</td>
            </tr>
        </c:forEach>
    </table>
    </div>
</body>
</html>
