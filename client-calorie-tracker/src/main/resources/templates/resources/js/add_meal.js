var addMealBtn = document.querySelector('#btn-add-meal')
addMealBtn.onclick = changeLayout
function changeLayout(e) {
    let dataDateContainer = document.querySelector('.data-date-container')
    dataDateContainer.style.display = "flex";
    dataDateContainer.style. justifyContent= "center";
    dataDateContainer.style.alignItems = "center";
}

var backToAddMeal = document.querySelector('#btn-back-to-add-meal')
backToAddMeal.onclick = function(e) {
    let dataDateContainer = document.querySelector('.data-date-container')
    dataDateContainer.style.display = "none";
}


var addBtn = document.querySelector('#btn-add')
addBtn.onclick = function(e) {
    let dataDateContainer = document.querySelector('.data-date-container')
    dataDateContainer.style.display = "none";
}
