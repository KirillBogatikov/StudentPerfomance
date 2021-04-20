Student = {}

Student.add = function() {
	Student.edit({})
}

Student.edit = function(i) {
	let s = __students_cache[i]
	Student.__actual = s
	Student.__init_form(s || {})
	
	__students.page.append(__students.container)
	fadeIn(__students.container)
}

Student.drop = function(i) {
	StudentAPI.delete(__students_cache[i].id)
}

Student.save = function() {
	let s = Student.__actual
	if (!s['data']) {
		s.data = {}
	}
	if (!s['contact']) {
		s.contact = {}
	}
	
	with(__students.fields) {
		s.data.firstName = first_name.value
		s.data.lastName = last_name.value
		s.data.patronymic = patronymic.value
		s.contact.phone = phone.value
		s.contact.email = email.value
		
		let gr = group.value
		if (gr != "null" && gr != s.group.id) {
			GroupAPI.add_student(gr, s.id)
		}
	}
	
	delete s.group
	StudentAPI.save(s, () => {
		Student.applyFilter()
		Student.cancel()
	})
}

Student.__init_form = function(s) {
	let d = s['data'] || {}
	let c = s['contact'] || {}
	
	with(__students.fields) {
		first_name.value = d['firstName'] || ""
		last_name.value = d['lastName'] || ""
		patronymic.value = d['patronymic'] || ""
		phone.value = c['phone'] || ""
		email.value = c['email'] || ""
		
		group.innerHTML = "<option value='null'><span>Не назначена</span></option>"
		GroupAPI.list(null, null, null, (list) => {
			for (let i in list) {
				let o = elem('option', null, list[i].code)
				o.value = list[i].id
				group.append(o)
			}
			
			if(Student.__actual) {
				StudentAPI.group_of(Student.__actual.id, (g) => {	
					Student.__actual.group = g
					group.value = g.id
				})
			}
		})
	}
}

Student.cancel = function() {
	fadeOut(__students.container, () => {
		Student.__init_form({})
		__students.container.remove()
	})
}

Student.applyFilter = function() {
	with(__students) {
		StudentAPI.list(actions.query_field.value, actions.offset.value - 1, actions.limit.value, (g) => {
			list.innerHTML = `<tr>
				<th>Имя</th>
				<th>Фамилия</th>
				<th>Отчество</th>
				<th>Номер телефона</th>
				<th>Эл. почта</th>
				<th class="no-min"></th>
			</tr>`;
			
			__students_cache = g
			for (let i in g) {
				let r = elem("tr")
				
				tableCell(r, g[i].data.firstName, i)
				tableCell(r, g[i].data.lastName, i)
				tableCell(r, g[i].data.patronymic, i)
				tableCell(r, g[i].contact.phone, i)
				tableCell(r, g[i].contact.email, i)
				
				tableCell(r, `<span class="material-icons" onclick=Student.edit(` + i + `)>edit</span>
					<span class="material-icons" onclick=Student.drop(` + i + `)>delete</span>`, i).classList.add("no-min")
				
				list.append(r)
			}
		})
	}
}
