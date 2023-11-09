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

var addMealButton = document.querySelector('#Add-meal-btn')
addMealButton.onclick = function() {
    window.location.href = "http://127.0.0.1:5500/add_meal.html"
}


document.querySelector('#add-product-btn').onclick = function() {
    window.location.href = "http://127.0.0.1:5500/add_product.html"
}

document.querySelector('#list-my-product-btn').onclick = function() {
    window.location.href = "http://127.0.0.1:5500/list_product.html"
}


document.querySelector('#change-meals-btn').onclick = function() {
    window.location.href = "http://127.0.0.1:5500/change_meal.html"
}


document.querySelector('#get-summary-btn').onclick = function() {
    window.location.href = "http://127.0.0.1:5500/get_summary.html"
}