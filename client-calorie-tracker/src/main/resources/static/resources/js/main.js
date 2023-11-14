var iconBoxs = document.querySelectorAll('.header-box .btn-box div')

for (var i of iconBoxs) {
    console.log(i)
    i.onmouseover = onmouseoverBxCogIcon
    i.onmouseout = onmouseoutBxCogIcon
}

function onmouseoverBxCogIcon(e) {
    let element = this.querySelector('a')
    element.style.display = "contents"
    element.style.cursor = 'pointer'
}

function onmouseoutBxCogIcon(e) {
    let element = this.querySelector('a')
    element.style.display = "none"
}
