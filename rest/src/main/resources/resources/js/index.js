events = {
	onload: []
};

index_all = function() {
	__landing = query("#landing")
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
	},
	__nav = query("NAV")
	__main = query("MAIN")
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
	let fixFormLabel = function(id) {
		let width = Math.max(...query(id + " .form LABEL", true).map(t => t.offsetWidth))
		query(id + " .form LABEL", true).forEach(t => t.style["width"] = width)
	};
	fixFormLabel("#student")
	
	for (var i in events.onload) {
		events.onload[i]();
	}
	
	(function() {
		query(".container").remove()
		query(".page").remove()
	})();
	
	if (localStorage.getItem("token")) {
		__landing.remove()
		document.body.append(__nav)
		document.body.append(__main)
		
		let params = new URLSearchParams(window.location.search)
		if (params.get("students") == "") {
			showStudents()
		}
	} else {
		logout()
	}
}

function showStudents() {
	__main.append(__students.page)
	fadeIn(__students.page)
	Student.applyFilter()
	history.pushState("", "",  "?students")
}