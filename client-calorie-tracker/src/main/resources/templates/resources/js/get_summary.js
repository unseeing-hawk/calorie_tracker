var addBtn = document.querySelector('#btn-add')
addBtn.onclick = changeLayout
function changeLayout(e) {
    let dataDateContainer = document.querySelector('.product-container')
    dataDateContainer.style.display = "flex";
}
