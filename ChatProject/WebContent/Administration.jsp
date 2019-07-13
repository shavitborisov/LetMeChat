<%@page import="java.security.Policy.Parameters"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="Helper.jsp"%>
<%@ page import="myDB.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Administration</title>
	<script src="AdministrationScript.js"></script>
	<script src="SpecialChars.js"></script>
	<link rel="stylesheet" type="text/css" href="MainCss.css">
</head>
<body>
<jsp:include page="./Skeleton.jsp"></jsp:include>
<%
MyDB mydb = GetDBConnection(application);

if (request.getMethod().equals("POST")) {
	String msg = "";
	
	String user_id = request.getParameter("delete");
	if (!user_id.isEmpty()) {
		mydb.terminateAccount(session, user_id);
		msg = "Successfully terminated the selected account!";
		AlertAndRedirect(out, msg, null);
	}
	
	String update = request.getParameter("update");
	if (!update.isEmpty()) {
		String[] parts = update.split("\\,");
		mydb.updateUsername(session, parts[0], parts[1]);
		msg = "Successfully updated the selected username!";
		AlertAndRedirect(out, msg, null);
	}
}
%>
	<div class="mainBody">
		<div class="inMainBody">
			<h1>Administration</h1>
			<%=mydb.printUsers()%>
		</div>
	</div>
</body>
<jsp:include page="./SkeletonDown.jsp"></jsp:include>
</html>