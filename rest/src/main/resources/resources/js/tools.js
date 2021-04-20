function uuid4() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

function cookie(name) {
	let pairs = document.cookie.split("; ")
	for (let i in pairs) {
		let parts = pairs[i].split("=")
		if (parts[0] == name) {
			return parts[1]
		} 
	}
}

function local(name) {
	let data = localStorage.getItem(name)
	if (data != null) {
		data = JSON.parse(data)
	}
}

function validation_message(name, result, min, max, chars) {
	if (!result || result == "Valid") {
		return ""
	}
	
	switch(result) {
		case "TooShort": 
			return "В поле " + name + " слишком короткое значение. Минимальная длина - " + min;
		case "TooLong": 
			return "В поле " + name + " слишком длинное значение. Максимальная длина - " + max;
		case "Incorrect": 
			return "В поле " + name + " введены недопустимые символы. Разрешено использовать только " + chars;
		case "Required": 
			return "Поле " + name + " обязательно для заполнения";
	}
	
}