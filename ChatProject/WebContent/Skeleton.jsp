<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="MainCss.css">
</head>
<body id="siteBackground">
	<div>
		<div id="skeletonUp">
			<ul id="nav">
<%
				String user = (String) session.getAttribute("logged");
				if (user == null)
					out.print("<li><a class='headerLink' href='SignIn.jsp'>Sign In</a></li>");
				else {
					out.print("<li><a class='headerLink' href='SignOut.jsp'>Sign Out</a></li>");
					out.print("<li><a class='headerLink' href='Settings.jsp'>Settings</a></li>");
					
					if (user.equals("admin")) // Shows administration tab for admin only
						out.print("<li><a class='headerLink' href='Administration.jsp'>Administration</a></li>");
					
					out.print("<li><p>Shalom, " + user + " </p></li>");
				}
%>
				<li id="skeletonLogo"><a href="Homepage.jsp"><img src="http://i58.tinypic.com/2h69g7d.png" width="auto" height="100px"></a></li>
			</ul>
		</div>
	</div>
</body>
</html>