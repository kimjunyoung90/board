<%--
  Created by IntelliJ IDEA.
  User: junyoungkim
  Date: 2024/05/06
  Time: 23:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>로그인</title>
</head>
<body>
    <form action="login.do" method="post">
        <c:if test="${errors.idOrPwNotMatch}">
            아이디와 암호가 일치하지 않습니다.
        </c:if>
        <p>
            아이디:<br/><input type="text" name="id" value="${param.id}">
            <c:if test="${errors.id}">ID를 입력하세요.</c:if>
        </p>
        <p>
            암호:<br/><input type="password" name="password">
            <c:if test="${errors.password}">암호를 입력하세요.</c:if>
        </p>
        <input type="submit" value="로그인">
    </form>
</body>
</html>
