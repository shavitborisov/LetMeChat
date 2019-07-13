<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="Helper.jsp"%>
<%@ page import="myDB.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Sign In</title>
	<link rel="stylesheet" type="text/css" href="MainCss.css">
</head>
<body>
<jsp:include page="./Skeleton.jsp"></jsp:include>
<%
MyDB mydb = GetDBConnection(application);
if (isSubmitPressed(request, "signInSubmit")) {
	String msg ="";
	if (mydb.UserExists(getFormField(request, "user"), getFormField(request, "pass"))) {
		if (mydb.checkUserOnOff(getFormField(request, "user")))
			msg = "User already signed in!";
		else {
			session.setAttribute("logged", getFormField(request, "user"));
			mydb.fillUserInfo(session);
			mydb.setUserOnOff(session, "yes");
			ClientRedirect(response, "SignInSuccess.jsp");
		}
	}
	else
		msg = "Wrong username or password!";
	
	AlertAndRedirect(out, msg, null);
}
%>
	<div class="mainBody">
		<div class="inMainBody">
			<h1>Sign In</h1>
			<p>Sign in with your Username and Password:</p>
			<form action="#">
				<table>
					<tr><td><input class="input" type="text" name="user" placeholder="Username" required="required"></td></tr>
					<tr><td><input class="input" type="password" name="pass" placeholder="Password" required="required"></td></tr>
					<tr><td><input class="button" type="submit" name="signInSubmit" id="signInSubmit" value="Sign In"></td></tr>
				</table>
			</form>
			<p class="displayInline">Don't have an account yet? </p>
			<a class="link" href="SignUp.jsp">Sign Up!</a>
		</div>
	</div>
</body>
<jsp:include page="./SkeletonDown.jsp"></jsp:include>
</html>