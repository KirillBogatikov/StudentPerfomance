StudentAPI = {
	group_of: function(id, callback, no_group) {
		request("GET", "/api/student/" + id + "/group", null, (xhr) => {
			if (xhr.status == 200) {
				callback(JSON.parse(xhr.responseText))
				return
			} else if (xhr.status == 404 && no_group) {
				no_group();
				return;
			}
			
			popup("На сервере произошла непредвиденная ошибка")
		})
	},
	list: function(query, offset, limit, callback, silent) {
		request_list("/api/student/list", query, offset || 0, limit || 1000, callback, silent)
	}, 
	save: function(student, callback) {
		request("PUT", "/api/student", student, xhr => {
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
					message += validation_message("Номер телефона", v['phone'], 2, 128, "номер в формате +7 (900) 123-45-67")
					message += validation_message("Эл. почта", v['email'], 3, 128, "адреса в формате user@host.com")
					
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
		request("DELETE", "/api/student/" + id, null, (xhr) => {
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