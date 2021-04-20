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
	}
}