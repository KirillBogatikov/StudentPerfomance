events = {
	onload: []
};

onload = function() {
	(function() {
		let width = query("NAV").offsetWidth
		let mainWidth = query("BODY").offsetWidth - width
		query("MAIN").style["width"] = mainWidth
		query("MAIN").style["margin-left"] = width
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
		query(".page").style["display"] = "none"
		query(".container").style["display"] = "none"
	})();
}

function showStudents() {
	let s = query("#students")
	s.style["display"] = "flex"
	fadeIn(s)
	Student.applyFilter()
}