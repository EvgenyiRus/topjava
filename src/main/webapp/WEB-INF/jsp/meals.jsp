<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <base href="${pageContext.request.contextPath}/"/>
    <h3><a href="">Home</a></h3>
<%--    <h3><a href="${pageContext.request.contextPath}">Home</a></h3>--%>
    <hr/>
    <h2><spring:message code="meal.title"/></h2>
    <form method="get" action="meals/filter">
        <input type="hidden" name="action" value="filter">
        <dl>
            <%--            <dt>From Date:</dt>--%>
            <dt><spring:message code="meal.fromDate"/>:</dt>
            <dd><input type="date" name="startDate" value="${param.startDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.toDate"/>:</dt>
            <dd><input type="date" name="endDate" value="${param.endDate}"></dd>
        </dl>
        <dl>
            <%--            <dt>From Time:</dt>--%>
            <dt><spring:message code="meal.fromTime"/>:</dt>
            <dd><input type="time" name="startTime" value="${param.startTime}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.toTime"/>:</dt>
            <dd><input type="time" name="endTime" value="${param.endTime}"></dd>
        </dl>
        <button type="submit">
            <dt><spring:message code="meal.filter"/>:</dt>
        </button>
    </form>
    <hr/>
    <a href="meals/create">
        <dt><spring:message code="meal.add"/>:</dt>
    </a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>
                <dt><spring:message code="meal.date"/>:</dt>
            </th>
            <th>
                <dt><spring:message code="meal.description"/>:</dt>
            </th>
            <th>
                <dt><spring:message code="meal.calories"/>:</dt>
            </th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr data-mealExcess="${meal.excess}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals/update?id=${meal.id}"><spring:message code="meal.update"/></a></td>
                <td><a href="meals/delete?id=${meal.id}"><spring:message code="meal.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>