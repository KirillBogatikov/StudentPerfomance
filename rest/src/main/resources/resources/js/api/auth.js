AuthAPI = {
	login: function(l, p, c) {
		request("POST", "/api/auth/login", {
			"login": l,
			"password": p
		}, (xhr) => {
			switch(xhr.status) {
				case 400: case 403:
					popup("Пароль введен неверно")
					break
				case 404:
					popup("Пользователь с таким логином не найден")
					break
				case 200:
					popup("Вход выполнен успешно")
					let data = JSON.parse(xhr.responseText)
					c(data.token)
					break
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})
	}
}