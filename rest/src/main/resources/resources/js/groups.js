Group = {}

events.onload.push(() => {
	__groups.fields.students.container.style["max-height"] = __nav.offsetHeight - __groups.form.offsetHeight
	__groups.fields.students.container.style["overflow-y"] = "auto"
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

__user_name = function(t) {
	return t.data.firstName + " " + (t.data.patronymic || "") +  " " + t.data.lastName
}

Group.hidePerfomance = function() {
	fadeOut(__groups.perfomance.container, () => {
		Group.__init_perfomance_form({})
		__groups.perfomance.container.remove()
	})
}

Group.__init_perfomance_form = function() {
	with(__groups.perfomance) {		
		code.innerHTML = ""
		add_discipline.name.value = ""
		add_discipline.select.value = ""
		add_mark.select.value = ""
		add_mark.mark.value = "1"
	}
}

Group.perfomance = function(i) {
	Group.__init_perfomance_form()
	let g = __groups_cache[i]
	Group.__actual_group = g
	
	with(__groups.perfomance) {		
		code.innerHTML = g.code
		
		TeacherAPI.list(null, null, null, l => {
			add_discipline.select.innerHTML = ""
			
			let def = elem("option", null, "преподаватель не выбран")
			def.value = ""
			add_discipline.select.append(def)
			
			for (let i in l) {
				let o = elem("option", null, __user_name(l[i]))
				o.value = l[i].id
				add_discipline.select.append(o)
			}
		})
		
		GroupAPI.get_students(g.id, (g) => {
			add_mark.select.innerHTML = ""
			
			let def = elem("option", null, "студент не выбран")
			def.value = ""
			add_mark.select.append(def)
			
			for (let i in g) {
				let o = elem("option", null, __user_name(g[i]))
				o.value = g[i].id
				add_mark.select.append(o)
			}
		})
		
		MarkAPI.list_disciplines(d => {
			add_mark.name.innerHTML = ""
			
			let def = elem("option", null, "дисциплина не выбрана")
			def.value = ""
			add_mark.name.append(def)
			
			for (let i in d) {
				let o = elem("option", null, d[i].name + " - " + __user_name(d[i].teacher))
				o.value = d[i].id
				add_mark.name.append(o)
			}
		})
		
		__groups.page.append(container)
		fadeIn(container)
	}
}

Group.addDiscipline = function() {
	with (__groups.perfomance.add_discipline) {
		if (select.value == "") {
			popup("Выберите преподавателя")
			return
		}
		
		MarkAPI.add_discipline({ "name": name.value, "teacher": { "id": select.value } }, () => {
			Group.hidePerfomance()
		})
	}
}

Group.addMark = function() {
	with(__groups.perfomance.add_mark) {
		if (select.value == "") {
			popup("Выберите студента")
			return
		}
		
		if (name.value == "") {
			popup("Выберите дисцплину")
			return
		}
		
		MarkAPI.add_mark({ "student": { "id": select.value }, "discipline": { "id": name.value }, "mark": mark.value })
	}
}

Group.exportPerfomance = function(student) {
	let sid = student ? __groups.perfomance.add_mark.select.value : null
	let gid = student ? null : Group.__actual_group.id
	MarkAPI.export_perfomance(sid, gid)
}