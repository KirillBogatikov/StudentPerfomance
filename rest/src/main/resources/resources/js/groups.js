Group = {}

events.onload.push(() => {
	__groups.fields.students.list.style["max-height"] = __nav.offsetHeight - __groups.form.offsetHeight - 200
})

Group.add = function() {
	Group.edit({})
}

Group.edit = function(i) {
	let t = __groups_cache[i]
	Group.__init_form(t || {})
	
	__groups.page.append(__groups.container)
	fadeIn(__groups.container)
}

Group.drop = function(i) {
	GroupAPI.delete(__groups_cache[i].id, () => {
		Group.applyFilter()
	})
}

Group.save = function() {
	let g = Group.__actual
		
	with(__groups.fields) {
		g.code = code.value
		g.duration = duration.value
	}
	
	let students = g.students
	delete g.students
	GroupAPI.save(g, (id) => {
		for (let i in students) {
			GroupAPI.add_student(id, students[i])
		}
		Group.applyFilter()
		Group.cancel()
	})
}

Group.__init_form = function(t) {
	Group.__actual = t
	Group.__actual.students = []
	with(__groups.fields) {
		code.value = t['code'] || ""
		duration.value = t['duration'] || 0
		
		StudentAPI.list(null, null, null, (s) => {
			with(__groups.fields.students) {
				select.innerHTML = "";
				let o = elem("option", null, `студент не выбран`)
				o.value = "null"
				select.append(o);
			
				for (let i in s) {
					StudentAPI.group_of(s[i].id, () => {}, () => {
						let o = elem("option", null, s[i].data.firstName + " " + (s[i].data.patronymic || "") +  " " + s[i].data.lastName)
						o.value = s[i].id
						select.append(o)
					})
				}
			}
		}, true)
		
		students.list.innerHTML = `<tr>
			<th>Имя</th>
			<th>Фамилия</th>
			<th>Отчество</th>
			<th>Номер телефона</th>
			<th>Эл. почта</th>
			<th class="no-min"></th>
		</tr>`;
				
		if (t['id']) {
			GroupAPI.get_students(t.id, (g) => {
				__students_cache = g
				for (let i in g) {
					let r = elem("tr")
					
					tableCell(r, g[i].data.firstName, i)
					tableCell(r, g[i].data.lastName, i)
					tableCell(r, g[i].data.patronymic, i)
					tableCell(r, g[i].contact.phone, i)
					tableCell(r, g[i].contact.email, i)
					
					tableCell(r, `<span class="material-icons" onclick=Group.removeStudent(` + i + `)>delete</span>`, i).classList.add("no-min")
					
					students.list.append(r)
				}
			})
		}
	}
}

Group.cancel = function() {
	fadeOut(__groups.container, () => {
		Group.__init_form({})
		__groups.container.remove()
	})
}

Group.addStudent = function() {
	let id = __groups.fields.students.select.value
	if (id != "null") {
		Group.__actual.students.push(id)
		__groups.fields.students.select.value = "null"
	}
}

Group.removeStudent = function(i) {
	Student.drop(i)
	Group.cancel()
}

Group.applyFilter = function() {
	with(__groups) {
		GroupAPI.list(actions.query_field.value, actions.offset.value - 1, actions.limit.value, (g) => {
			list.innerHTML = `<tr>
				<th>Код</th>
				<th>Срок обучения<br>(в семестрах)</th>
				<th>Количество<br>студентов</th>
				<th class="no-min"></th>
			</tr>`;
			
			__groups_cache = g
			for (let i in g) {
				let r = elem("tr")
				
				tableCell(r, g[i].code, i)
				tableCell(r, g[i].duration, i)
				tableCell(r, "0", i)
				
				tableCell(r, `<span class="material-icons" onclick=Group.edit(` + i + `)>edit</span>
					<span class="material-icons" onclick=Group.drop(` + i + `)>delete</span>
					<span class="material-icons" onclick=Group.perfomance(` + i + `)>tune</span>`, i).classList.add("no-min")
				
				list.append(r)
			}
		})
	}
}
