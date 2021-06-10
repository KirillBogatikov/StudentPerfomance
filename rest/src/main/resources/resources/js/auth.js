function logout(silent) {
	process_logout()
	if (!silent) {
		localStorage.removeItem("token")
		popup("Вы вышли из аккаунта")
	}
}

function process_logout(start) {
	document.body.append(__landing)
	fadeIn(__landing)
	if (start) {
		__nav.remove()
		__main.remove()
	} else {
		fadeOut(__nav, () => __nav.remove())
		fadeOut(__main, () => __main.remove())
		history.pushState("", "", "")
	}
}

function process_login() {
	fadeOut(__landing, () => __landing.remove())
	document.body.append(__nav)
	fadeIn(__nav)
	document.body.append(__main)
	fadeIn(__main)
	
	TeacherAPI.get((info) => {
	    __account.innerHTML = info.data.firstName + " " + (info.data.patronymic || "") + " " + info.data.lastName
	})
}

auth_onload = function() {
	if (logged_in()) {
		process_login()
	} else {
		process_logout(true)
	}
}

//default auth - carrot/33980e243add
function login() {
	with(__auth.fields) {
		AuthAPI.login(login.value, password.value, (token) => {
			localStorage.setItem("token", token)
			process_login()
		})
	}
}

function logged_in() {
	return localStorage.getItem("token") != null
}