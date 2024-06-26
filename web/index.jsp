<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: junyoungkim
  Date: 2024/05/06
  Time: 23:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원제 게시판 예제</title>
</head>
<body>
    <c:if test="${!empty authUser}">
        ${authUser.name}님, 안녕하세요.
        <a href="logout.do">[로그아웃하기]</a>
        <a href="changePwd.do">[암호변경하기]</a>
    </c:if>
    <c:if test="${empty authUser}">
        <a href="join.do">[회원가입하기]</a>
        <a href="login.do">[로그인하기]</a>
    </c:if>
</body>
</html>
