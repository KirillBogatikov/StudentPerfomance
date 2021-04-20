function logout() {
	localStorage.removeItem("token")
	document.body.append(__landing)
	__nav.remove()
	__main.remove()
	popup("Вы вышли из аккаунта")
}

function login() {
	AuthAPI.login("carrot", "33980e243add")
}