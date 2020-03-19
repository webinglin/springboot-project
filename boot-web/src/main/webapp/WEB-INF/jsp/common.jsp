<%--
  Created by IntelliJ IDEA.
  User: linwb
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
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
