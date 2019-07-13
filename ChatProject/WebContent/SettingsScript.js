function checkUpdateForm() {
	var displayName = document.getElementById("name");
	if ((displayName == null) || (displayName.value == ""))
		displayName.setCustomValidity("Display name can't be empty");
	else
		displayName.setCustomValidity("");
}

function checkMailForm() {
	var mail = document.getElementById("newMail");
	var confirmMail = document.getElementById("confirmMail");
	
	if ((mail == null) || (mail.value == ""))
		mail.setCustomValidity("This field is empty");
	else if ((confirmMail == null) || (confirmMail.value == ""))
		confirmMail.setCustomValidity("This field is empty");
	else if (mail.value != confirmMail.value)
		mail.setCustomValidity("E-mails don't match");
	else {
		mail.setCustomValidity("");
		confirmMail.setCustomValidity("");
	}
}

function checkPassForm() {
	var pass = document.getElementById("newPass");
	var confirmPass = document.getElementById("confirmPass");
	
	if ((pass == null) || (pass.value == ""))
		pass.setCustomValidity("This field is empty");
	else if (pass.value.length < 3)
		pass.setCustomValidity("Password must be at least 3 chars long");
	else if (!checkPass(pass.value))
		pass.setCustomValidity("Password must contain 1 uppercase letter, 1 lowercase letter and 1 digit");
	else if (specialChars(pass.value))
		pass.setCustomValidity("Password must only contain alphanumeric characters");
	else
		pass.setCustomValidity("");
	
	if ((confirmPass == null) || (confirmPass.value == ""))
		confirmPass.setCustomValidity("This field is empty");
	else if (confirmPass.value != pass.value)
		confirmPass.setCustomValidity("Passwords don't match");
	else
		confirmPass.setCustomValidity("");
}

function terminatePrompt() {
    res = confirm("Are you sure you want to terminate your account? This action is irreversible!");
    if (res == true)
    	window.location.href="Settings.jsp?terminate=true";
}