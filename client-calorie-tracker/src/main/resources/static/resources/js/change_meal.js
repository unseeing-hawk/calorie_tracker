
document.querySelectorAll('.body-product-table tbody tr').forEach(row => {
    let mealtimeElement = row.cells[1]
    mealtimeElement.onclick = function(e) {
        mealtimeElement.innerHTML = ''
        let mealtime = document.querySelector('#meal-time').innerHTML
        mealtimeElement.innerHTML = mealtime
        let selectElment =  row.querySelector('select')
        selectElment.onclick = function(event) {
            event.stopPropagation();
        }
        selectElment.onchange = function (e){
            let indexSelected = selectElment.selectedIndex
            let valueSelected = selectElment.options[indexSelected].text
            mealtimeElement.innerHTML = valueSelected
        }
    }
})

var clientErrorContainer = document.getElementById('client-error-container')
var clientErrorMessage = document.getElementById("client-error-message")
var tbodyTableMeal = document.querySelector('.body-product-table tbody')
document.querySelector('#btn-apply-changes').onclick = function(e) {
    let isCorrectAllMealTime = true
    let isCorrectAllWeight = true

    tbodyTableMeal.querySelectorAll('tr').forEach(row => {
        let isCorrectMealTimeInRow = true
        let isCorrectWeightInRow = true

        // check meal time
        if(row.cells[1].innerHTML.includes('select')) {
            isCorrectMealTimeInRow = false
            isCorrectAllMealTime = false
        }

        // check weight
        if(checkWeightColums(row.cells[2]) == false){
            isCorrectWeightInRow = false
            isCorrectAllWeight = false
        }

        if(!isCorrectMealTimeInRow || !isCorrectWeightInRow){
            row.cells[2].parentNode.style.backgroundColor = 'rgba(248, 38, 38, 0.4)'
            setAnimationError(row.cells[2].parentNode)
        }
        else if(isCorrectMealTimeInRow && isCorrectWeightInRow) {
            row.cells[2].parentNode.style.backgroundColor = '#fff'
        }
    })

    let contentClientError = ''
    if(!isCorrectAllMealTime) {
        contentClientError = 'Please select meal time.'
    }
    
    if(!isCorrectAllWeight){
        if(contentClientError == '') {
            contentClientError = 'Please enter the value of the weight.\nThe weight is a positive float number, accurate to 2 decimal places.'
        }
        else {
            contentClientError += '\nPlease enter the value of the weight.\nThe weight is a positive float number, accurate to 2 decimal places.'
        }
    }

    clientErrorMessage.innerText = contentClientError
    if(contentClientError.length > 0) {
        clientErrorContainer.style.display = 'flex'
    }
}

function checkWeightColums(element) {
    element.innerText = element.innerText.trim()
    if(element.innerText.length == 0
        || !(/^[0-9]([0-9]+)?[.]?([0-9]{1,2})?$/.test(element.innerText))
        || parseFloat(element.innerText) == 0
        || parseFloat(element.innerText) > (3.4 ** 38))
    {
        return false
    }
    else {
        element.innerText = parseFloat(element.innerText).toString()
        return true
    }
}

document.querySelector("#btn-delete").onclick = function() {
    let checkboxs = tbodyTableMeal.querySelectorAll('input[type="checkbox"]')
    checkboxs.forEach(checkbox => {
        if(checkbox.checked){
            tbodyTableMeal.removeChild(checkbox.parentNode.parentNode);
        }
    })
}

document.querySelector("#client-error-box button").onclick = function(e) {
    clientErrorContainer.style.display = "none";
    clientErrorMessage.innerHTML = ''
}

function setAnimationError(el) {
    el.style.borderColor = "#C9544D"
    el.style.animation = 'shake 0.5s'
    setTimeout(() => {
        el.style.animation = ''
    }, 510);
}

var dateElement = document.querySelector('#date')
document.addEventListener('DOMContentLoaded', function(e) {
    let maxDateString = new Date().toISOString().split('T')[0]
    let minDate = new Date()
    minDate.setFullYear(minDate.getFullYear() - 10)
    let minDateString = minDate.toISOString().split('T')[0]

    dateElement.setAttribute('min', minDateString)
    dateElement.setAttribute('max', maxDateString)
})

document.querySelector('#search-btn').onclick = function(e) {
    if(dateElement.value == '') {
        clientErrorMessage.innerText = 'Please enter date!'
        clientErrorContainer.style.display = 'flex'
    }
    else {
        let selectedDate = new Date(dateElement.value);
        let minDateString = dateElement.getAttribute('min');
        let maxDateString = dateElement.getAttribute('max');
        let minDate = new Date(minDateString);
        let maxDate = new Date(maxDateString);
        if (selectedDate < minDate || selectedDate > maxDate) {
            clientErrorMessage.innerText = 'The minimum value of the date is 10 years ago.\nThe maximum value of the date is today.'
            clientErrorContainer.style.display = 'flex'
        }
    }
}

document.querySelector("#sever-error-box button").onclick = function hideErrorBox(e) {
    let errorBox = document.getElementById("sever-error-container");
    let errorMessage = document.getElementById("sever-error-message");
    errorBox.style.display = "none";
    errorMessage.textContent = ""
}
