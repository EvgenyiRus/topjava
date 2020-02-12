<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Редактирование продукта</title>
</head>
<body>
    <div align="center">
        <form method="POST" action="meals">
        <table>
            <jsp:useBean id="meal" class="ru.javawebinar.topjava.model.Meal" scope="request"/>
            <tr>
                <td><input type="hidden" name="id" value="${meal.id}"/></td>
            </tr>
            <tr>
                <td>Description</td>
                <td><input type="text" name="description" value="${meal.description}"/></td>
            </tr>
            <tr>
                <td>DateTime</td>
                <td><input type="dateTime-local" name="dateTime-local" value="${meal.dateTime}"/></td>
            </tr>
            <tr>
                <td>Count calories</td>
                <td><input type="text" name="calories" value="${meal.calories}"/></td>
            </tr>
            <td><input type="submit" value="submit">
            <input type="reset" value="reset"></td>
        </table>
        </form>
    </div>
</body>
</html>
