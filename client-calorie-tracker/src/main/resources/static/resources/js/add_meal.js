var dateElement = document.querySelector('#date')
var clientErrorLayout = document.getElementById('client-error-container')
var clientErrorMessage = document.getElementById("client-error-message");

var productTbody = document.querySelector('#products-table tbody')
var listOfChosenProductTbody = document.querySelector('#list-of-chosen-product-table tbody')

/* var buttonAddProducts = document.querySelector("#btn-add-product")
buttonAddProducts.onclick = function(){
    let checkboxesOfProdcutTable = productTbody.querySelectorAll('input[type=checkbox]')
    // Find the checked checkbox in the Product table
    let checkedCheckboxProductTable = Array.from(checkboxesOfProdcutTable).filter(function(checkbox) {
        return checkbox.checked;
    });
    checkedCheckboxProductTable.forEach(function(checkbox) {
        var row1 = checkbox.parentNode.parentNode;
        var cells = row1.getElementsByTagName('td');
        var newRow = listOfChosenProductTbody.insertRow();
        // add name
        newRow.insertCell().innerHTML = cells[0].innerHTML
        // add weight      
        let weightCell = newRow.insertCell()
        weightCell.setAttribute("contenteditable", 'true')
        weightCell.style.outline = "none"
        // add checkbox
        newRow.insertCell().innerHTML = '<input type="checkbox" name="" id="">'
        checkbox.checked = false
    })
} */

/* document.querySelector("#btn-clear").onclick = function(e) {
    var checkboxesOfChosenProductTable = listOfChosenProductTbody.querySelectorAll('input[type=checkbox]')
    checkboxesOfChosenProductTable.forEach(checkbox => {
        if(checkbox.checked){
            listOfChosenProductTbody.removeChild(checkbox.parentNode.parentNode);
        }
    })
} */

var addMealBtn = document.querySelector('#btn-add-meal')
addMealBtn.onclick = changeLayout
function changeLayout(e) {
    let amountProductsChosen = listOfChosenProductTbody.querySelectorAll('tr').length
    if(amountProductsChosen > 0) {
        let isCorrectWeightInRow = true
        listOfChosenProductTbody.querySelectorAll('tr').forEach(row => {
            // check weight
            if(checkWeightColumns(row.cells[1].getElementsByTagName("input")[0]) == false){
                isCorrectWeightInRow = false
            }
            else {
                row.cells[1].parentNode.style.backgroundColor = '#fff'
                row.cells[1].setAttribute("contenteditable", 'false')
            }
        })
        
        if(isCorrectWeightInRow == false) {
            clientErrorMessage.innerText = 'Enter the value of the weight.\nProduct weight is a positive Float number, accurate to 2 decimal places.'
        }
        else{
            clientErrorMessage.innerText = ''
        }

        if(clientErrorMessage.innerText.length != 0) {
            clientErrorLayout.style.display = 'flex'
        }
        else {
            let dataDateContainer = document.querySelector('.data-date-container')
            dataDateContainer.style.display = "flex"
            dataDateContainer.style. sjustifyContent= "center"
            dataDateContainer.style.alignItems = "center"

            let maxDateString = new Date().toISOString().split('T')[0]
            let minDate = new Date()
            minDate.setFullYear(minDate.getFullYear() - 10)
            let minDateString = minDate.toISOString().split('T')[0]

            dateElement.setAttribute('min', minDateString)
            dateElement.setAttribute('max', maxDateString)
        }
    }
}

function checkWeightColumns(element) {
    element.value = element.value.trim()
    if(element.value.length == 0
        || !(/^[0-9]([0-9]+)?[.]?([0-9]{1,2})?$/.test(element.value))
        || parseFloat(element.value) == 0
        || parseFloat(element.value) > (3.4 ** 38))
    {
        element.parentNode.parentNode.style.backgroundColor = 'rgba(248, 38, 38, 0.4)'
        setAnimationError(element.parentNode.parentNode)
        return false
    }
    else {
        element.value = parseFloat(element.value).toString()
        return true
    }
}

var backToAddMeal = document.querySelector('#btn-back-to-add-meal')
backToAddMeal.onclick = function(e) {
    let dataDateContainer = document.querySelector('.data-date-container')
    dataDateContainer.style.display = "none";
}

document.querySelector("#client-error-box button").onclick = function hideErrorBox(e) {
    clientErrorLayout.style.display = "none";
    clientErrorMessage.innerHTML = ""
}

/* var productsInDatabase = productTbody.innerHTML
document.querySelector('#search-btn').onclick = function(e) {
    let searchInput = document.querySelector('#search-input')
    let nameProductSearch = searchInput.value.toLowerCase().trim()
    searchInput.value = searchInput.value.trim()
    if(nameProductSearch.length > 0) {
        productTbody.innerHTML = productsInDatabase
        let searchProducts = Array.from(productTbody.querySelectorAll('tr')).filter(function(row) {
            return row.cells[0].innerText.toLowerCase().includes(nameProductSearch)
        })
        productTbody.innerHTML = ''
        if(searchProducts.length > 0) {
            searchProducts.forEach(row => {
                productTbody.appendChild(row)
            })
        }
    }
    else {
        productTbody.innerHTML = productsInDatabase
    }
} */

/* var addBtn = document.querySelector('#btn-add')
addBtn.onclick = function(e) { */
function addBtnClick() {
    if(dateElement.value == '') {
        clientErrorMessage.innerText = 'Please enter date!'
        clientErrorLayout.style.display = 'flex'
    }
    else {
        let selectedDate = new Date(dateElement.value);
        let minDateString = dateElement.getAttribute('min');
        let maxDateString = dateElement.getAttribute('max');
        let minDate = new Date(minDateString);
        let maxDate = new Date(maxDateString);
        if (selectedDate < minDate || selectedDate > maxDate) {
            clientErrorMessage.innerText = 'The minimum value of the date is 10 years ago.\nThe maximum value of the date is today.'
            clientErrorLayout.style.display = 'flex'
        } else {
            return true;
        }

        return false;
        /* else {
            let dataDateContainer = document.querySelector('.data-date-container')
            dataDateContainer.style.display = "none";
            listOfChosenProductTbody.innerHTML = ''
            document.querySelector('#search-input'). value = ''
            productTbody.innerHTML = productsInDatabase
        } */
    }
}

function setAnimationError(el) {
    el.style.borderColor = "#C9544D"
    el.style.animation = 'shake 0.5s'
    setTimeout(() => {
        el.style.animation = ''
    }, 510);
}

document.querySelector("#sever-error-box button").onclick = function hideErrorBox(e) {
    let errorBox = document.getElementById("sever-error-container");
    let errorMessage = document.getElementById("sever-error-message");
    errorBox.style.display = "none";
    errorMessage.textContent = ""
}
