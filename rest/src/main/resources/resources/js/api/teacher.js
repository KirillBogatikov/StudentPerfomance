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
	}
}