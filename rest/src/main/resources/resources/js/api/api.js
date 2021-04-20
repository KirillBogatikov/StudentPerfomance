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

function request_list(url, query, offset, limit, callback, silent) {
	url += "?"
	if (query) {
		url += "query=" + query + "&"
	}
	
	if (typeof(offset) == 'number') {
		url += "offset=" + offset + "&limit=" + limit
	} else if(typeof(offset) == 'function') {
		callback = offset
		silent = limit
	}
	
	request("GET", url, null, (xhr) => {
		if (silent && xhr.status == 200) {
			callback(JSON.parse(xhr.responseText))
			return
		}
		
		switch(xhr.status) {
			case 200:
				popup("Данные получены")
				callback(JSON.parse(xhr.responseText))
				break
			case 400:
				popup("Вы ввели неверные настройки. Выбирать записи можно начиная с 1 и не более 1000 штук");
				break
			default:
				popup("На сервере произошла непредвиденная ошибка")
		}
	})
}
