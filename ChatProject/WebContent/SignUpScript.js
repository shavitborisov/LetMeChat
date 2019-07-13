function checkSignUpForm() {
	var mail = document.getElementById("mail");
	var mailconfirm = document.getElementById("mailconfirm");
	
	if (mail.value != mailconfirm.value)
		mail.setCustomValidity("E-mails don't match");
	else
		mail.setCustomValidity("");
	
	var username = document.getElementById("username");
	
	if (username.value.length < 3 || username.value.length > 16)
		username.setCustomValidity("Username must be between 3 and 16 chars long");
	else if (specialChars(username.value))
		username.setCustomValidity("Username must only contain alphanumeric characters");
	else if (firstIsChar(username.value))
		username.setCustomValidity("Username must start with a letter");
	else
		username.setCustomValidity("");
	
	var pass = document.getElementById("pass");
	
	if (pass.value.length < 3)
		pass.setCustomValidity("Password must be at least 3 chars long");
	else if (!checkPass(pass.value))
		pass.setCustomValidity("Password must contain 1 uppercase letter, 1 lowercase letter and 1 digit");
	else if (specialChars(pass.value))
		pass.setCustomValidity("Password must only contain alphanumeric characters");
	else
		pass.setCustomValidity("");
	
	var passconfirm = document.getElementById("passconfirm");
	
	if (passconfirm.value != pass.value)
		passconfirm.setCustomValidity("Passwords don't macth");
	else
		passconfirm.setCustomValidity("");
	
	var bday = document.getElementById('bday');
	var bdate = +new Date(bday.value);
	
	if((~~ ((Date.now() - bdate) / (31557600000))) < 13)
		bday.setCustomValidity("You must be at least 13 years old to use this service");
	else
		bday.setCustomValidity("");
}