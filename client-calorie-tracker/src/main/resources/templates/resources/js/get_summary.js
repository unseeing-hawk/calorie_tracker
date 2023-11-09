var backToMainButton = document.querySelector('#btn-back')
backToMainButton.onclick = moveMainHTML
function moveMainHTML(e) {
    if(confirm("You want to exit??"))
    {
        window.location.href = "http://127.0.0.1:5500/main.html";
    }
}


var backToMainButton = document.querySelector('#btn-back-to-main')
backToMainButton.onclick = moveMainHTML
function moveMainHTML(e) {
    if(confirm("You want to exit??"))
    {
        window.location.href = "http://127.0.0.1:5500/main.html";
    }
}

var addBtn = document.querySelector('#btn-add')
addBtn.onclick = changeLayout
function changeLayout(e) {
    let dataDateContainer = document.querySelector('.product-container')
    dataDateContainer.style.display = "flex";
}