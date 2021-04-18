Student.add = function() {
	Student.edit({})
}

Student.saveForm = function() {
	let s = Student.__actual
	if (!s['data']) {
		s.data = {}
	}
	if (!s['contact']) {
		s.contact = {}
	}
	
	s.data.firstName = query("#student-first-name").value
	s.data.lastName = query("#student-last-name").value
	s.data.patronymic = query("#student-patronymic").value
	s.contact.phone = query("#student-phone").value
	s.contact.email = query("#student-email").value
	
	Student.save(s, () => {
		Student.applyFilter()
		
		let c = query("#students .container")
		fadeOut(c, () => { c.style["display"] = "none" })
	})
}

Student.__init_form = function(s) {
	let d = s['data'] || {}
	let c = s['contact'] || {}
	
	query("#student-first-name").value = d['firstName'] || ""
	query("#student-last-name").value = d['lastName'] || ""
	query("#student-patronymic").value = d['patronymic'] || ""
	query("#student-phone").value = c['phone'] || ""
	query("#student-email").value = c['email'] || ""
}

Student.cancel = function() {
	let c = query("#students .container")
	Student.__init_form({})
	fadeOut(c)
	setTimeout(() => c.style["display"] = "none", time)
}

Student.applyFilter = function() {
	Student.list(query("#student-offset").value - 1, query("#student-limit").value, (g) => {
		let table = query("#students .list")
		
		table.innerHTML = `<tr>
			<th>Имя</th>
			<th>Фамилия</th>
			<th>Отчество</th>
			<th>Номер телефона</th>
			<th>Эл. почта</th>
			<th></th>
		</tr>`;
		
		for (let i in g) {
			let r = document.createElement("tr")
			
			tableCell(r, g[i].data.firstName, i)
			tableCell(r, g[i].data.lastName, i)
			tableCell(r, g[i].data.patronymic, i)
			tableCell(r, g[i].contact.phone, i)
			tableCell(r, g[i].contact.email, i)
			tableCell(r, `<span class="material-icons" onclick=Student.edit(` + JSON.stringify(g[i]) + `)>edit</span>`, i)
			
			table.append(r)
		}
	})
}

Student.edit = function(s) {
	Student.__actual = s
	Student.__init_form(s)
	
	let c = query("#students .container")
	c.style["display"] = "flex"
	fadeIn(c)
}
