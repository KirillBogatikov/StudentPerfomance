function request(method, url, body, callback) {
	if (body != null) {
		body = JSON.stringify(body)
	}
	
	let xhr = new XMLHttpRequest()
	xhr.open(method, url, true)
	xhr.setRequestHeader("Content-Type", "application/json")
	xhr.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"))
	xhr.send(body)
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) return
		
		callback(xhr)
	}
	
}

Auth = {
	login: function(l, p) {
		request("POST", "/api/auth/login", {
			"login": l,
			"password": p
		}, (xhr) => {
			switch(xhr.status) {
				case 403:
					popup("Пароль введен неверно")
					break
				case 404:
					popup("Пользователь с таким логином не найден")
					break
				case 200:
					popup("Вход выполнен успешно")
					let data = JSON.parse(xhr.responseText)
					localStorage.setItem("token", data.token)
					break
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})
	}
}

Student = {
	list: function(o, l, g) {
		request("GET", "/api/student/list?offset=" + o + "&limit=" + l, null, (xhr) => {
			switch(xhr.status) {
				case 200:
					popup("Данные получены")
					g(JSON.parse(xhr.responseText))
					break
				case 400:
					popup("Вы ввели неверные настройки. Выбирать записи можно начиная с 1 и не более 1000 штук");
					break
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})
	}, 
	save: function(s, c) {
		request("PUT", "/api/student", s, xhr => {
			switch(xhr.status) {
				case 200:
					popup("Данные сохранены")
					c()
					break
				case 400:
					let v = JSON.parse(xhr.responseText)
					
					let message = ""
					message += validation_message("Имя", v['first_name'], 1, 128, "буквы кириллицы и латиницы, а также пробел")
					message += validation_message("Фамидлия", v['last_name'], 1, 128, "буквы кириллицы и латиницы, а также пробел")
					message += validation_message("Отчество", v['patronymic'], 1, 128, "буквы кириллицы и латиницы, а также пробел")
					message += validation_message("Номер телефона", v['phone'], 2, 128, "номер в формате +7 (900) 123-45-67")
					message += validation_message("Эл. почта", v['email'], 3, 128, "адреса в формате user@host.com")
					
					if (message.length > 0) {
						popup(message)
					}
					
					break
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})	
	}
}