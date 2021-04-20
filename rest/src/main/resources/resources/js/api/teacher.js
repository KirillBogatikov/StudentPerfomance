TeacherAPI = {
	get: function(callback) {
		request("GET", "/api/teacher", null, (xhr) => {
			switch(xhr.status) {
				case 200:
					callback(JSON.parse(xhr.responseText))
					break;
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})
	},
	list: function(query, offset, limit, callback, silent) {
		request_list("/api/teacher/list", query, offset || 0, limit || 1000, callback, silent)
	}, 
	save: function(teacher, callback) {
		request("PUT", "/api/teacher", teacher, xhr => {
			switch(xhr.status) {
				case 200:
					popup("Данные сохранены")
					callback()
					break
				case 400:
					let v = JSON.parse(xhr.responseText)
					
					let message = ""
					message += validation_message("Имя", v['first_name'], 1, 128, "буквы кириллицы и латиницы, а также пробел")
					message += validation_message("Фамидлия", v['last_name'], 1, 128, "буквы кириллицы и латиницы, а также пробел")
					message += validation_message("Отчество", v['patronymic'], 1, 128, "буквы кириллицы и латиницы, а также пробел")
					message += validation_message("Логин", v['login'], 4, 16, "буквы кириллицы и латиницы, а также пробел")
					message += validation_message("Пароль", v['password'], 8, 128, null)
					
					if (message.length > 0) {
						popup(message, 6000)
					}
					
					break
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})	
	},
	delete: function(id, callback) {
		request("DELETE", "/api/teacher/" + id, null, (xhr) => {
			switch(xhr.status) {
				case 200:
					popup("Запись удалена")
					callback()
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})
	}
}