function specialChars(str) {
	var pattern = new RegExp(/[~`!#$%\^&*+=\-\[\]\\';,/{}|\\":<>\?]/);
	if (pattern.test(str))
		return true;
	return false;
}

function firstIsChar(str) {
	var char = str.charAt(0);
	return (!((char >= 'a' && char <= 'z') || (char >= 'A' && char <= 'Z')));
}