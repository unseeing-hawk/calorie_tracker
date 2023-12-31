var inputsDateLayout = document.querySelector('.inputs-date-layout')
var summaryTableLayout = document.querySelector('.summary-table-layout')

var clientErrorContainer = document.getElementById('client-error-container')
var clientErrorMessage = document.getElementById("client-error-message")


// document.querySelector('#btn-back').onclick = function(e) {
//     summaryTableLayout.style.display = "none";
//     inputsDateLayout.style.display = "block";
// }

var startDateElement = document.getElementById('start-date')
var endDateElement = document.getElementById('end-date')
document.addEventListener('DOMContentLoaded', function(e) {
    let maxDateString = new Date().toISOString().split('T')[0]
    let minDate = new Date()
    minDate.setFullYear(minDate.getFullYear() - 10)
    let minDateString = minDate.toISOString().split('T')[0]

    startDateElement.setAttribute('min', minDateString)
    startDateElement.setAttribute('max', maxDateString)

    endDateElement.setAttribute('min', minDateString)
    endDateElement.setAttribute('max', maxDateString)
})


function checkDate() {
    let contentError = ''
    if(startDateElement.value == '' || endDateElement.value == '') {
        contentError = 'Please enter date!'
        if(startDateElement.value == '') {
            setAnimationError(startDateElement)
        }
        else {
            startDateElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        }
        if(endDateElement.value == '') {
            setAnimationError(endDateElement)
        }
        else {
            endDateElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        }
    }
    else {
        let minDateString = startDateElement.getAttribute('min');
        let maxDateString = startDateElement.getAttribute('max');
        let minDate = new Date(minDateString);
        let maxDate = new Date(maxDateString);

        let startDate = new Date(startDateElement.value)
        let endDate = new Date(endDateElement.value)

        if (startDate < minDate || startDate > maxDate) {
            contentError = 'The minimum value of the date is 10 years ago.\nThe maximum value of the date is today.'
            setAnimationError(startDateElement)
        }
        else {
            startDateElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        }
        if (endDate < minDate || endDate > maxDate) {
            contentError = 'The minimum value of the date is 10 years ago.\nThe maximum value of the date is today.'
            setAnimationError(endDateElement)
        }
        else {
            endDateElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        }

        if(contentError == '') {
            if(startDate > endDate) {
                contentError = 'The start date must be less than or equal to the end date.'
            }
        }
    }
    if(contentError != '') {
        clientErrorMessage.innerText = contentError
        clientErrorContainer.style.display = 'flex'
        return false
    }

    // summaryTableLayout.style.display = "flex"
    // inputsDateLayout.style.display = "none"
    return true
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

document.querySelector("#sever-error-box button").onclick = function hideErrorBox(e) {
    let errorBox = document.getElementById("sever-error-container");
    let errorMessage = document.getElementById("sever-error-message");
    errorBox.style.display = "none";
    errorMessage.textContent = ""
}
