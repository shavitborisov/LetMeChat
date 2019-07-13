<%@ page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="Helper.jsp"%>
<%@ page import="myDB.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Settings</title>
	<script src="SettingsScript.js"></script>
	<script src="Pass.js"></script>
	<script src="SpecialChars.js"></script>
	<link rel="stylesheet" type="text/css" href="MainCss.css">
</head>
<body>
<jsp:include page="./Skeleton.jsp"></jsp:include>
<%
MyDB mydb = GetDBConnection(application);
mydb.fillUserInfo(session);
UserInfo info = (UserInfo) session.getAttribute("info");

String msg;

if (isSubmitPressed(request, "personalUpdate")) {
	mydb.updatePersonal(request, session);
	msg = "Successfully updated personal settings";
	AlertAndRedirect(out, msg, "Homepage.jsp");
}

if (isSubmitPressed(request, "mailUpdate")) {
	if (mydb.MailTaken(getFormField(request, "newMail")))
		msg = "Email address is already taken!";
	else {
		mydb.updateMail(request, session);
		msg = "Successfully updated mail";
	}
	AlertAndRedirect(out, msg, "Homepage.jsp");
}

if (isSubmitPressed(request, "passUpdate")) {
	mydb.updatePass(request, session);
	msg = "Successfully updated password";
	AlertAndRedirect(out, msg, "Homepage.jsp");
}

if (isSubmitPressed(request, "terminate")) {
	mydb.terminateMe(session);
	msg = "Your account has been terminated!";
	AlertAndRedirect(out, msg, "SignOutRedirect.jsp");
}
%>
	<div class="mainBody">
		<div class="inMainBody">
		<h1>Settings</h1>
			<div class="floatLeft">
				<p>Tweak your personal settings:</p>
				<form id="personal" action="#">
					<table>
						<tr>
							<td><label for="name">Display Name:</label></td>
							<td><input class="input" type="text" name="name" id="name" maxlength="45" value="<%=info.getName()%>"></td>
						</tr>
						<tr>
							<td><label for="ageDisp">Display Age (<%=info.getAge()%>):</label></td>
							<td>
								<input type="radio" name="ageDisp" value="yes" required="required" <%=info.ageDisp() ? "checked" : ""%>>Yes
								<input type="radio" name="ageDisp" value="no" <%=!info.ageDisp() ? "checked" : ""%>>No
							</td>
						</tr>
						<tr>
							<td valign="top"><label for="bio">Bio:</label></td>
							<td><textarea maxlength="200" rows="10" cols="50" class="input" name="bio" id="bio"><%=info.getBio()%></textarea></td>
						</tr>
						<tr>
							<td><label for="mailDisp">Display Mail:</label></td>
							<td>
								<input type="radio" name="mailDisp" value="yes" required="required" <%=info.mailDisp() ? "checked" : ""%>>Yes
								<input type="radio" name="mailDisp" value="no" <%=!info.mailDisp() ? "checked" : ""%>>No
							</td>
						</tr>
						<tr>
							<td><input class="button" type="submit" name="personalUpdate" id="personalUpdate" value="Update"  onclick="checkUpdateForm()"></td>
						</tr>
					</table>
				</form>
			</div>
			<div id="accountSettingsDiv">
				<p>Tweak your account settings:</p>
				<form class="displayBlock" id="accountMail" method="post" action="#">
					<table>
						<tr><td><label for="newMail">Change Email:</label></td></tr>
						<tr><td><p class="noTopMargin noBottomMargin">(<%=info.getMail()%>)</p></td></tr>
						<tr><td><input class="input" type="email" placeholder="New Email" name="newMail" id="newMail" maxlength="45"></td>
							<td><input class="input" type="email" placeholder="Confirm New Email" name="confirmMail" id="confirmMail" maxlength="45"></td>
							<td><input class="button" type="submit" name="mailUpdate" id="mailUpdate" value="Update" onclick="checkMailForm()"></td>
						</tr>
					</table>
				</form>
				<form class="displayBlock" id="accountPass" method="post" action="#">
					<table>
						<tr><td><label for="newPass">Change Password:</label></td></tr>
						<tr><td><input class="input" type="password" placeholder="New Password" name="newPass" id="newPass" maxlength="20"></td>
							<td><input class="input" type="password" placeholder="Confirm New Password" name="confirmPass" id="confirmPass" maxlength="20"></td>
							<td><input class="button" type="submit" name="passUpdate" id="passUpdate" value="Update" onclick="checkPassForm()"></td>
						</tr>
					</table>
				</form>
<%
				String user = (String) session.getAttribute("logged");
				if (!user.equals("admin")) // Doesn't show terminate button for admin
					out.print("<input class='button' type='button' name='terminate' id='terminate' value='Terminate Account' onclick='terminatePrompt()'></td>");
%>
			</div>
		</div>
	</div>
</body>
<jsp:include page="./SkeletonDown.jsp"></jsp:include>
</html>