<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="Helper.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Home</title>
	<link rel="stylesheet" type="text/css" href="MainCss.css">
</head>
<body>
<jsp:include page="./Skeleton.jsp"></jsp:include>
	<div class="mainBody">
		<br><br><br><br><br><br><br><br>
		<div class="inMainBody">
<%
			String user = (String) session.getAttribute("logged");
			if (user == null) { // no access granted if not logged in
				out.print("<a href='' class='homeButton' id='disabledHomeButton'><span>Let Me Chat</span></a>");
				out.print("<p id='homeButtonSubtitle'>You must be signed in to be able to chat!</p>");
			}
			else // access granted if logged in
				out.print("<a href='Chat.jsp' class='homeButton'><span>Let Me Chat</span></a>");
%>			
		</div>
	</div>
</body>
<jsp:include page="./SkeletonDown.jsp"></jsp:include>
</html>