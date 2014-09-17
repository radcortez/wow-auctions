<%--
  Created by IntelliJ IDEA.
  User: radcortez
  Date: 05/09/14
  Time: 12:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<br>
<a href="${pageContext.request.contextPath}/BatchExecutionServlet?batch=prepare">Prepare Job</a>
<br><br>
<a href="${pageContext.request.contextPath}/BatchExecutionServlet?batch=files">Files Job</a>
<br><br>
<a href="${pageContext.request.contextPath}/BatchExecutionServlet?batch=process">Process Job</a>
<br><br>
<a href="${pageContext.request.contextPath}/BatchExecutionServlet?batch=multiple-process">Multiple Process Job</a>

</body>
</html>

