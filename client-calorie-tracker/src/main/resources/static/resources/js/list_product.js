function setAnimationError(el) {
    el.style.borderColor = "#C9544D"
    el.style.animation = 'shake 0.5s'
    setTimeout(() => {
        el.style.animation = ''
    }, 510);
}

var clientErrorContainer = document.getElementById('client-error-container')
var errorMessage = document.getElementById("client-error-message");

function validateApplyForm() {
    let isCorrectAllName = true
    let isCorrectAllCalories = true
    let isCorrectAllProteins = true
    let isCorrecAlltFats = true
    let isCorrectAllCarbohydrates = true

    document.querySelectorAll("tbody tr").forEach(row => {
        let isCorrectNameInRow = true
        let isCorrectCaloriesInRow = true
        let isCorrectProteinsInRow = true
        let isCorrectFatsInRow = true
        let isCorrectCarbohydratesInRow = true
        // check name
        if (checkNameColumn(row.cells[0].querySelector("input"), row.cells[0]) == false) {
            isCorrectAllName = false
            isCorrectNameInRow = false
        }
        // check Calories
        if(checkCaloriesColums(row.cells[1].querySelector("input"), row.cells[1]) == false) {
            isCorrectAllCalories = false
            isCorrectCaloriesInRow = false
        }
        // check Proteins
        if(checkProteinFatsCarbohydratesColums(row.cells[2].querySelector("input"), row.cells[2]) == false) {
            isCorrectAllProteins = false
            isCorrectProteinsInRow = false
        }
        // check Fats
        if(checkProteinFatsCarbohydratesColums(row.cells[3].querySelector("input"), row.cells[3]) == false){
            isCorrecAlltFats = false
            isCorrectFatsInRow = false
        }
        // check Carbohydrates
        if(checkProteinFatsCarbohydratesColums(row.cells[4].querySelector("input"), row.cells[4]) == false){
            isCorrectAllCarbohydrates = false
            isCorrectCarbohydratesInRow = false
        }

        if(isCorrectNameInRow && isCorrectCaloriesInRow && isCorrectProteinsInRow && isCorrectFatsInRow && isCorrectCarbohydratesInRow) {
            row.cells[0].parentNode.style.backgroundColor = '#fff'
        }
    })

    let contentError = ''
    if(isCorrectAllName == false) {
        contentError = contentError + 'Product names range in length from 1 to 100 characters.'
    }
    if(isCorrectAllCalories == false) {
        if(contentError == '') {
            contentError = contentError + 'Calories are a non-negative integer.'
        }
        else {
            contentError = contentError + '\nCalories are a non-negative integer.'
        }
    }
    if(isCorrectAllProteins == false) {
        if(contentError == '') {
            contentError = contentError + 'Proteins are positive float numbers, accurate to two decimal places.'
        }
        else {
            contentError = contentError + '\nProteins are positive float numbers, accurate to two decimal places.'
        }
    }
    if(isCorrecAlltFats == false) {
        if(contentError == '') {
            contentError = contentError + 'Fats are positive float numbers, accurate to two decimal places.'
        }
        else {
            contentError = contentError + '\nFats are positive float numbers, accurate to two decimal places.'
        }
    }
    if(isCorrectAllCarbohydrates == false) {
        if(contentError == '') {
            contentError = contentError + 'Carbohydrates are positive float numbers, accurate to two decimal places.'
        }
        else {
            contentError = contentError + '\nCarbohydrates are positive float numbers, accurate to two decimal places.'
        }
    }
    errorMessage.innerText = contentError
    if(contentError.length != 0) {
        clientErrorContainer.style.display = 'flex'
    }
    else {
        return true
    }
    return false
}

// document.querySelector('#btn-delete').onclick = function(e) {
//     let checkboxes = document.querySelectorAll('tbody input[type="checkbox"]')
//     checkboxes.forEach(checkbox => {
//         if(checkbox.checked) {
//             let row = checkbox.parentNode.parentNode
//             row.parentNode.removeChild(row)
//         }
//     })
// }

function checkNameColumn(elementToCheck, elementToAnimate) {
    elementToCheck.value = elementToCheck.value.trim()
    if(elementToCheck.value.length == 0 || elementToCheck.value.length > 100) {
        elementToAnimate.parentNode.style.backgroundColor = 'rgba(248, 38, 38, 0.4)'
        setAnimationError(elementToAnimate.parentNode)
        return false
    }
    else {
        arrayNames = elementToCheck.value.split(/\s+/)
        elementToCheck.value = ""
        let standardFormatName = ''
        for(var i in arrayNames) {
            if(i != (arrayNames.length - 1)) {
                standardFormatName+= arrayNames[i] + "  "
            }
            else {
                standardFormatName += arrayNames[i]
            }
        }
        elementToCheck.value = standardFormatName
        return true
    }
}

function checkCaloriesColums(elementToCheck, elementToAnimate) {
    elementToCheck.value = elementToCheck.value.trim()
    if(elementToCheck.value.length == 0
        || (/[^0-9]/.test(elementToCheck.value))
        || parseInt(elementToCheck.value) > 2147483647)
    {
        elementToAnimate.parentNode.style.backgroundColor = 'rgba(248, 38, 38, 0.4)'
        setAnimationError(elementToAnimate.parentNode)
        return false
    }
    else {
        elementToCheck.value = parseInt(elementToCheck.value).toString()
        return true
    }
}

function checkProteinFatsCarbohydratesColums(elementToCheck, elementToAnimate) {
    elementToCheck.value = elementToCheck.value.trim()
    if(elementToCheck.value.length == 0
        || !(/^[0-9]([0-9]+)?[.]?([0-9]{1,2})?$/.test(elementToCheck.value))
        // || parseFloat(elementToCheck.value) == 0
        || parseFloat(elementToCheck.value) > (3.4 ** 38))
    {
        elementToAnimate.parentNode.style.backgroundColor = 'rgba(248, 38, 38, 0.4)'
        setAnimationError(elementToAnimate.parentNode)
        return false
    }
    else {
        elementToCheck.value = parseFloat(elementToCheck.value).toString()
        return true
    }
}

document.querySelector("#client-error-box button").onclick = function hideErrorBox(e) {
    clientErrorContainer.style.display = "none";
    errorMessage.innerHTML = ""
}

document.querySelectorAll('tbody tr').forEach(el => {
    el.cells[0].addEventListener("keypress", function(e) {
        if(e.keyCode != 13){
            let oldValueEl = e.target.innerText
            if(oldValueEl.length > 99) {
                e.preventDefault()
            }
        }
        else{
            e.preventDefault()
        }
    })
    for(let i = 1; i < 5; i++) {
        el.cells[i].addEventListener("keypress", function(e) {
            if(e.keyCode == 13){
                e.preventDefault()
            }
        })
    }
})

document.querySelector("#sever-error-box button").onclick = function hideErrorBox(e) {
    let errorBox = document.getElementById("sever-error-container");
    let errorMessage = document.getElementById("sever-error-message");
    errorBox.style.display = "none";
    errorMessage.textContent = ""
}
