<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="Helper.jsp"%>
<%@ page import="myDB.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Sign Out</title>
</head>
<body>
<%
MyDB mydb = GetDBConnection(application);
mydb.setUserOnOff(session, "no");
session.invalidate();
ServerRedirect(request, response, "Homepage.jsp");
%>
</body>
</html>