<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="Helper.jsp"%>
<%@ page import="myDB.*" %>
<%@ page import="ajax.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Chat</title>
	<link rel="stylesheet" type="text/css" href="MainCss.css">
	<script src="Ajax.js"></script>
</head>
<body onload="initChat()">
<jsp:include page="./Skeleton.jsp"></jsp:include>
<script type="text/javascript">
window.onbeforeunload = function() {
	leavePage();
    return null;
}
</script>
	<div id="status">
		<p class="statusMsg">trying to find a chat partner, please wait...</p>
	</div>
	<div id="mainChatBody">
		<div id="chatArea"></div>
		<div id="inMainChatBody">
			<form action="" onsubmit="return sendNewMsg();">
				<table><tr>
					<td id="inputMsgField"><input type="text" class="input" name="msg" id="msg"></td>
					<td><input onclick="sendNewMsg()" class="chatControls" type="button" name="snd" id="snd" value="Send Message" disabled></td>
					<td><input onclick="window.location.href='Homepage.jsp'" class="chatControls" type="button" name="end" id="end" value="End Conversation"></td>
				</tr></table>
			</form>
		</div>
	</div>
</body>
<jsp:include page="./SkeletonDown.jsp"></jsp:include>
</html>