function checkPass(str) {
	var capital = false;
	var smaller = false;
	var digit = false;

	for(var i = 0; ((i < str.length) && (!(capital && smaller && digit))); ++i) {
		var char = str.charAt(i);

		if(char >= '0' && char <= '9')
			digit = true;
		else if(char >= 'a' && char <= 'z')
			smaller = true;
		else if(char >= 'A' && char <= 'Z')
			capital = true;
	}
	return (capital && smaller && digit);
}