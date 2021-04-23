MarkAPI = {
	list_disciplines: function(callback) {
		request_list("api/mark/discipline/", null, 0, 1000, callback)
	},
	add_discipline: function(mark, callback) {
		request("POST", "api/mark/discipline/", mark, xhr => {
			if (xhr.status == 200) {
				popup("Данные сохранены")
				callback()
				return
			}
			
			popup("На сервере произошла непредвиденная ошибка")
		})
	},
	add_mark: function(mark, callback) {
		request("PUT", "/api/mark/", mark, xhr => {
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
	export_perfomance: function(student, group) {
		let url = "/api/mark/export/group/" + group
		if (student) {
			url = "/api/mark/export/student/" + student
		}
		
		request("GET", url, null, xhr => {
			if (xhr.status == 200) {
				popup("Файл сформирован")
				
				let id = xhr.responseText.replaceAll('"', '')
				let a = elem("a")
				a.href = "/api/mark/files/" + id
				a.click()
				
				return
			}
			
			popup("На сервере произошла непредвиденная ошибка")
		})
	}
}