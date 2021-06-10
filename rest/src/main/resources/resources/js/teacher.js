Teacher = {}

Teacher.add = function() {
    Teacher.__actual = {}
	Teacher.edit()
}

Teacher.edit = function(i) {
	if (i) {
		Teacher.__actual = __teachers_cache[i]
	}

	Teacher.__init_form(Teacher.__actual)
	
	__teachers.page.append(__teachers.container)
	fadeIn(__teachers.container)
}

Teacher.drop = function(i) {
	TeacherAPI.delete(__teachers_cache[i].id)
}

Teacher.save = function() {
	let t = Teacher.__actual
	if (!t['data']) {
		t.data = {}
	}
	if (!t['auth']) {
		t.auth = {}
	}
	
	with(__teachers.fields) {
		t.data.firstName = first_name.value
		t.data.lastName = last_name.value
		t.data.patronymic = patronymic.value
		t.auth.login = login.value
		t.auth.password = password.value
	}
	
	if (!t.auth.password) {
		delete t.auth.password
	}
	
	delete t.group
	TeacherAPI.save(t, () => {
		Teacher.applyFilter()
		Teacher.cancel()
	})
}

Teacher.__init_form = function(t) {
	let d = t['data'] || {}
	let a = t['auth'] || {}
	
	with(__teachers.fields) {
		first_name.value = d['firstName'] || ""
		last_name.value = d['lastName'] || ""
		patronymic.value = d['patronymic'] || ""
		login.value = a['login'] || ""
		password.value = ""
	}
}

Teacher.cancel = function() {
	fadeOut(__teachers.container, () => {
		Teacher.__init_form({})
		__teachers.container.remove()
	})
}

Teacher.applyFilter = function() {
	with(__teachers) {
		TeacherAPI.list(actions.query_field.value, actions.offset.value - 1, actions.limit.value, (g) => {
			list.innerHTML = `<tr>
				<th>Имя</th>
				<th>Фамилия</th>
				<th>Отчество</th>
				<th>Логин</th>
				<th class="no-min"></th>
			</tr>`;
			
			__teachers_cache = g
			for (let i in g) {
				let r = elem("tr")
				
				tableCell(r, g[i].data.firstName, i)
				tableCell(r, g[i].data.lastName, i)
				tableCell(r, g[i].data.patronymic, i)
				tableCell(r, g[i].auth.login, i)
				
				tableCell(r, `<span class="material-icons" onclick=Teacher.edit(` + i + `)>edit</span>
					<span class="material-icons" onclick=Teacher.drop(` + i + `)>delete</span>`, i).classList.add("no-min")
				
				list.append(r)
			}
		})
	}
}
