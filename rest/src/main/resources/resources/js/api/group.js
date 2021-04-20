GroupAPI = {
	list: function(query, offset, limit, callback, silent) {
		request_list("/api/group/list", query, offset || 0, limit || 1000, callback, silent)
	},
	add_student: function(id, student) {
		request("POST", "/api/group/" + id + "/student/" + student, null, (xhr) => {
			switch(xhr.status) {
				case 200:
					popup("Студент приписан к группе")
					break
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})
	},
	get_students: function(id, callback) {
		request_list("/api/group/" + id + "/students", null, callback)
	},
	remove_student: function(id, student) {
		request("DELETE", "/api/group/" + id + "/student/" + student, null, (xhr) => {
			switch(xhr.status) {
				case 200:
					popup("Студент приписан к группе")
					break
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})
	},
	save: function(group, callback) {
		request("PUT", "/api/group/", group, xhr => {
			switch(xhr.status) {
				case 200:
					popup("Данные сохранены")
					callback(JSON.parse(xhr.responseText))
					break
				default:
					popup("На сервере произошла непредвиденная ошибка")
			}
		})	
	},
	delete: function(id, callback) {
		request("DELETE", "/api/group/" + id, null, (xhr) => {
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