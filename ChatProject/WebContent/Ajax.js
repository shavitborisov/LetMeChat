var xml;

function initChat(){
	document.getElementById("snd").disabled = true;
	document.getElementById("chatArea").innerHTML = "";
	xml = new XMLHttpRequest(); // creating a new XMLHttpRequest object
	xml.onreadystatechange = function() {onNewChat();};
	sendData("new", "");
}

function sendData(operation, data) {
	xml.open("post", "ChatListener", true);
	xml.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xml.send("code=" + operation + (data != "" ? "&data=" + data : ""));
}

function onNewChat() {
	if (xml.readyState == 4 && xml.status == 200) {
		document.getElementById("status").innerHTML = xml.responseText;
		xml.onreadystatechange = function() {onPartnerExist();};
		sendData("getPartner", "");
	}
}

function onPartnerExist() {
	if (xml.readyState == 4 && xml.status == 200) {
		document.getElementById("status").innerHTML = xml.responseText;
		xml.onreadystatechange = function() {onPartnerAck();};
		sendData("getPartnerAck", "");
	}
}

function onPartnerAck() {
	if (xml.readyState == 4 && xml.status == 200) {
		document.getElementById("status").innerHTML = xml.responseText;
		xml.onreadystatechange = function() {onNewMsg();};
		document.getElementById("snd").disabled = false;
		sendData("getMsg", "");
	}
}

function onNewMsg() {
	if (xml.readyState == 4) {
		if (xml.status == 200) { // status okay
			document.getElementById("chatArea").innerHTML += xml.responseText;
			xml.onreadystatechange = function() {onNewMsg();};
			sendData("getMsg", "");
		}
		else if (xml.status == 410) { // status gone, partner has left
			document.getElementById("status").innerHTML = xml.responseText;
			xml.abort();
			initChat();
		}
	}
}

function sendNewMsg() {
	var newMsg = document.getElementById("msg").value;
	document.getElementById("msg").value = "";
	var ajaxReq = new XMLHttpRequest(); // creating new Ajax request
	if (newMsg != "") { // not empty
		ajaxReq.open("post", "ChatListener", true);
		ajaxReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		ajaxReq.send("code=setMsg" + (newMsg != "" ? "&data=" + newMsg : ""));
	}
	return false;
}

function leavePage() {
	sendData("leave", "");
}