<%--
  Created by IntelliJ IDEA.
  User: linwb
  Date: 2019/9/11
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

    try {
        // 防止session劫持，所以登录之后，session都会重新创建。 要是没有登录成功，session都是不可用的
        session.setAttribute("URL", basePath);
    } catch (Exception e){
        request.setAttribute("URL", basePath);
    }
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

    hello, ${name}.

    <c:if test="${age>20}">
        old man's age is ${age}.
    </c:if>

<script type="text/javascript" src="${URL}/js/login/login.js"></script>
</body>
</html>
