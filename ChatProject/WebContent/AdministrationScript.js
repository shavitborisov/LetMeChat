function deleteUser(id) {
	var input = document.getElementById("delete");
	input.value = id;
}

function updateUsername(id) {
	var update = document.getElementById("update");
	var newUsername = prompt("New Username:");
	if (newUsername != null) {
		if (newUsername.length < 3 || newUsername.length > 16)
			alert("Username must be between 3 and 16 chars long!");
		else if (specialChars(newUsername))
			alert("Username must only contain alphanumeric characters");
		else if (firstIsChar(newUsername))
			alert("Username must start with a letter");
		else
			update.value = id + "," + newUsername;
	}
}