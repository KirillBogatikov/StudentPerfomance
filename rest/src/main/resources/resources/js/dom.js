function query(s, a) {
	if (a) {
		let array = []
		document.querySelectorAll(s).forEach(e => array.push(e))
		return array
	}
	
	return document.querySelector(s)
}

const FPS = 60, time = 400;
const count = time * FPS / 1000;

function fadeIn(item, callback) {
	let id = "timeout_" + uuid4()
	let f = () => {
		let o = parseFloat(item.style["opacity"] || 0)
		item.style["opacity"] = (o + 1 / count)
		
		if (o < 1) {
			window[id] = setTimeout(f, 17)
		} else {
			window[id] = undefined
			if (callback) {
				callback()
			}
		}
	}
	f()
}

function fadeOut(item, callback) {
	let id = "timeout_" + uuid4()
	let f = () => {
		let o = parseFloat(item.style["opacity"] || 1)
		item.style["opacity"] = (o - 1 / count)
		
		if (o > 0) {
			window[id] = setTimeout(f, 17)
		} else {
			window[id] = undefined
			if (callback) {
				callback()
			}
		}
	}
	f()
}

window.__popups = []

function popup(message) {
	let popup = document.createElement("div")
	popup.className = "popup"
		
	let content = document.createElement("span")
	content.className = "pp-content"
	content.innerHTML = message
	popup.append(content)
	
	document.body.append(popup)
	
	let height = 20
	window.__popups.forEach(t => height += t.offsetHeight + 20)
	popup.style["bottom"] = height
	
	window.__popups.push(popup)
	fadeIn(popup)
	
	setTimeout(() => {
		fadeOut(popup, () => {
			popup.remove()
			window.__popups.shift()
		})
	}, 5000)
}

function tableCell(row, text, i) {
	let cell = document.createElement("td")
	cell.innerHTML = text || ""
	if (i % 2 == 1) {
		cell.className = "marked"
	}
	
	row.append(cell)
}