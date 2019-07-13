<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="Helper.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Sign Up Succeeded</title>
	<link rel="stylesheet" type="text/css" href="MainCss.css">
</head>
<body>
<jsp:include page="./Skeleton.jsp"></jsp:include>
	<div class="mainBody">
		<div class="inMainBody">
		<h1>Sign Up Succeeded</h1>
		<p>You have successfully signed up, <span class="fontWeightBold"><%=getFormField(request, "username")%></span>!</p>
		</div>
	</div>
</body>
<jsp:include page="./SkeletonDown.jsp"></jsp:include>
</html>