<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="jdbc.connection.ConnectionProvider" %><%--
  Created by IntelliJ IDEA.
  User: junyoungkim
  Date: 2024/05/06
  Time: 20:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>연결 테스트</title>
</head>
<body>
    <%
        try(Connection conn = ConnectionProvider.getConnection()) {
            out.println("커넥션 연결 성공합");
        } catch (SQLException e) {
            out.println("커넥션 연결 실패함: " + e.getMessage());
            application.log("커넥션 연결 실패", e);
        }
    %>
</body>
</html>
