events = {
	onload: []
};

fixFormLabel = function(id) {
	let width = Math.max(...query(id + " .form LABEL", true).map(t => t.offsetWidth))
	query(id + " .form LABEL", true).forEach(t => t.style["width"] = width)
};

index_all = function() {
	__landing = query("#landing")
	__auth = {
		page: query("#landing #auth"),
		fields: {
			login: query("#landing #auth-login"),
			password: query("#landing #auth-password")
		}
	}
	__account = query("#account-name")
	__students = {
		page: query("#students"),
		actions: {
			block: query("#students .actions"),
			query_field: query("#student-query"),
			offset: query("#student-offset"),
			limit: query("#student-limit"),
		},
		list: query("#students .list"),
		container: query("#students .container"),
		form: query("#students .container .form"),
		fields: {
			first_name: query("#student-first-name"),
			last_name: query("#student-last-name"),
			patronymic: query("#student-patronymic"),
			phone: query("#student-phone"),
			email: query("#student-email"),
			group: query("#student-group")
		}
	}
	__teachers = {
		page: query("#teachers"),
		actions: {
			block: query("#teachers .actions"),
			query_field: query("#teachers-query"),
			offset: query("#teachers-offset"),
			limit: query("#teachers-limit"),
		},
		list: query("#teachers .list"),
		container: query("#teachers .container"),
		form: query("#teachers .container .form"),
		fields: {
			first_name: query("#teacher-first-name"),
			last_name: query("#teacher-last-name"),
			patronymic: query("#teacher-patronymic"),
			login: query("#teacher-login"),
			password: query("#teacher-password")
		}
	}
	__groups = {
		page: query("#groups"),
		actions: {
			block: query("#groups .actions"),
			query_field: query("#groups-query"),
			offset: query("#groups-offset"),
			limit: query("#groups-limit"),
		},
		list: query("#groups .list"),
		container: query("#groups #group-form-container"),
		form: query("#groups #group-form"),
		fields: {
			code: query("#group-code"),
			duration: query("#group-duration"),
			students: {
				container: query("#group-students"),
				list: query("#group-students-list"),
				select: query("#group-students-select")
			}
		},
		perfomance: {
			container: query("#perfomance-form-container"),
			form: query("#perfomance-form"),
			code: query("#perfomance-code"),
			add_discipline: {
				name: query("#prefomace-discipline-name"),
				select: query("#perfomance-discipline-teacher")
			},
			add_mark: {
				select: query("#perfomance-student"),
				name: query("#prefomace-discipline"),
				mark: query("#perfomance-mark")
			}
		}
	}
	__nav = query("NAV")
	__main = query("MAIN")
	__previous = null
}

onload = function() {
	index_all();
	
	(function() {
		let width = __nav.offsetWidth
		let mainWidth = query("BODY").offsetWidth - width
		__main.style["width"] = mainWidth
		__main.style["margin-left"] = width
		query("MAIN .container", true).forEach(t => {
			t.style["width"] = mainWidth
			t.style["margin-left"] = width
		})
	})();
	
	(function() {
		let height = query(".page .actions INPUT").offsetHeight
		query(".page .actions SPAN", true).forEach(t => {
			t.style["font-size"] = height + "px"		
		})
	})();
	
	fixFormLabel("#student");
	fixFormLabel("#auth");
	
	for (var i in events.onload) {
		events.onload[i]();
	}
	
	(function() {
		query(".container", true).forEach(t => t.remove())
		query(".page", true).forEach(t => t.remove())
	})();
	
	if (logged_in()) {
		let query = (window.location.search + "").split("?")[1]
		if (query) {
			let params = query.split("&")
			for (let i in params) {
				if (["students", "teachers", "groups"].find(t => t == params[i])) {
					let className = params[i][0].toUpperCase() + params[i].substring(1, params[i].length - 1)
					showPage(window[className], params[i], window["__" + params[i]])
					break
				}
			}
		}
	}
}

function showPage(api, name, indexed) {
	if (__previous) {
		__previous.remove()
	}
	__main.append(indexed.page)
	fadeIn(indexed.page)
	api.applyFilter()
	history.pushState("", "",  "?" + name)
	__previous = indexed.page
}