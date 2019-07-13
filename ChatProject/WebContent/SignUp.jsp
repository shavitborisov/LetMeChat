<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="Helper.jsp"%>
<%@ page import="myDB.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Sign Up</title>
	<script src="SignUpScript.js"></script>
	<script src="Pass.js"></script>
	<script src="SpecialChars.js"></script>
	<link rel="stylesheet" type="text/css" href="MainCss.css">
</head>
<body>
<jsp:include page="./Skeleton.jsp"></jsp:include>
<%
MyDB mydb = GetDBConnection(application);
if (isSubmitPressed(request, "signUpSubmit")) {
	String msg;
	boolean badUser = mydb.UsernameTaken(getFormField(request, "username"));
	boolean badMail = mydb.MailTaken(getFormField(request, "mail"));
	
	if((badUser) || (badMail)) {
		if(badUser && !badMail)
			msg = "Username is already taken!";
		else if(!badUser && badMail)
			msg = "Email address is already taken!";
		else
			msg = "Username and Email address are already taken!";
		
		AlertAndRedirect(out, msg, null);
	}
	else {
		mydb.AddNewUser(request);
		ServerRedirect(request, response, "SignUpSuccess.jsp");
	}
}
%>
	<div class="mainBody">
		<div class="inMainBody">
			<h1>Sign Up</h1>
			<p>Please complete this form to allow your registration to our website:</p>
			<form action="#">
				<table>
					<tr>
						<td><label for="mail">Email:</label></td>
						<td><input class="input" type="email" name="mail" id="mail" maxlength="45" required="required"></td>
					</tr>
					<tr>
						<td><label for="mailconfirm">Confirm Email:</label></td>
						<td><input  class="input" type="email" id="mailconfirm" maxlength="45" required="required"></td>
					</tr>
					<tr>
						<td><label for="username">Username:</label></td>
						<td><input class="input" type="text" name="username" id="username" required="required"></td>
					</tr>
					<tr>
						<td><label for="pass">Password:</label></td>
						<td><input class="input" type="password" name="pass" id="pass" maxlength="20" required="required"></td>
					</tr>
					<tr>
						<td><label for="passconfirm">Confirm Password:</label></td>
						<td><input class="input" type="password" id="passconfirm" maxlength="20" required="required"></td>
					</tr>
					<tr>
						<td><label>Birth Date:</label></td>
						<td><input class="input" type="date" name="bday" id="bday" required="required"></td>
					</tr>
					<tr>
						<td><label for="sex">Sex:</label></td>
						<td>
							<input type="radio" name="sex" value="male" required="required">Male
							<input type="radio" name="sex" value="female">Female
						</td>
					</tr>
					<tr>
						<td><input class="input" type="checkbox" name="terms" id="terms" required="required"></td>
						<td><label>I have read and agreed the </label><a class="link" href="TermsOfService.jsp">Terms of Service</a></td>
					</tr>
					<tr>
						<td colspan="2"><button class="button" name="signUpSubmit" id="signUpSubmit" onclick="checkSignUpForm()">Sign Up</button></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
<jsp:include page="./SkeletonDown.jsp"></jsp:include>
</html>