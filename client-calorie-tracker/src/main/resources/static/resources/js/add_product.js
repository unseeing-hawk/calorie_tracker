function setAnimationError(el) {
    el.style.borderColor = "#C9544D"
    el.style.animation = 'shake 0.5s'
    setTimeout(() => {
        el.style.animation = ''
    }, 510);
}

var nameElement = document.querySelector("#name")
var caloriesElement = document.querySelector("#calories")
var proteinsElement = document.querySelector("#proteins")
var fatsElement = document.querySelector("#fats")
var carbohydratesElement = document.querySelector("#carbohydrates")

document.querySelector('#btn-add').onclick = addProduct
function addProduct() {
    // input name
    nameElement.value = nameElement.value.trim()
    let nameLabel =  document.querySelector('#name-label') 
    let nameErrorSpan = document.querySelector('#name-error-span') 
    if(nameElement.value.length == 0) {
        nameLabel.style.color = '#C9544D'
        setAnimationError(nameLabel)
        setAnimationError(nameElement)
        nameErrorSpan.innerHTML = ''
    }
    else if(nameElement.value.length > 100) {
        nameErrorSpan.innerHTML  = "Name must be limited to 100 characters"
        nameLabel.style.color = '#C9544D'
        setAnimationError(nameLabel)
        setAnimationError(nameErrorSpan)
        setAnimationError(nameElement)
    }
    else {
        nameElement.style.color = '#0b0202c3'
        nameLabel.style.color = 'rgba(54, 47, 47, 1)'
        nameElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        nameErrorSpan.innerHTML = ''
        arrayNames = nameElement.value.split(/\s+/)
        nameElement.value = ""
        for(var i in arrayNames) {
            if(i != (arrayNames.length - 1)) {
                nameElement.value += arrayNames[i] + " "
            }
            else {
                nameElement.value += arrayNames[i]
            }
        }
    }

    // input calories
    let caloriestLabel = document.querySelector('#calories-label')
    let caloriesErrorSpan = document.querySelector('#calories-error-span')
    if(caloriesElement.value.length == 0) {
        caloriestLabel.style.color = '#C9544D'
        setAnimationError(caloriestLabel)
        setAnimationError(caloriesElement)
        caloriesErrorSpan.innerHTML = ''
    }
    else if((/[^0-9]/.test(caloriesElement.value))) {
        caloriestLabel.style.color = '#C9544D'
        caloriesErrorSpan.innerHTML  = "The calories only contains the following characters: 0-9"
        setAnimationError(caloriesErrorSpan)
        setAnimationError(caloriesElement)
        setAnimationError(caloriestLabel)
    }
    else if(parseInt(caloriesElement.value) > 2147483647) {
        caloriestLabel.style.color = '#C9544D'
        caloriesErrorSpan.innerHTML  = "Calories are non-negative integers"
        setAnimationError(caloriesErrorSpan)
        setAnimationError(caloriesElement)
        setAnimationError(caloriestLabel)
    }
    else {
        caloriestLabel.style.color = 'rgba(54, 47, 47, 1)'
        caloriesElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        caloriesErrorSpan.innerHTML = ''
        caloriesElement.value = parseInt(caloriesElement.value).toString()
    }

    // input proteins
    let proteinsLabel = document.querySelector('#proteins-label')
    let proteinsErrorSpan = document.querySelector('#proteins-error-span')
    checkInputInFloatFormat(proteinsElement, proteinsLabel, proteinsErrorSpan)

    // input fats 
    let fatsLabel = document.querySelector('#fats-label')
    let fatsErrorSpan = document.querySelector('#fats-error-span')
    checkInputInFloatFormat(fatsElement, fatsLabel, fatsErrorSpan)

    // input carbohydrates carbohydratesElement
    let carbohydratesLabel = document.querySelector('#carbohydrates-label')
    let carbohydratesErrorSpan = document.querySelector('#carbohydrates-error-span')
    checkInputInFloatFormat(carbohydratesElement, carbohydratesLabel, carbohydratesErrorSpan)
    
}

function checkInputInFloatFormat(element, label, errorSpan) {
    if(element.value.length == 0) {
        label.style.color = '#C9544D'
        setAnimationError(label)
        setAnimationError(element)
        errorSpan.innerHTML = ''
    }
    else if((/[^0-9.]/.test(element.value))) {
        label.style.color = '#C9544D'
        errorSpan.innerHTML  = `The ${label.innerText.toLowerCase()} only contains the following characters: 0-9 and '.'`
        setAnimationError(errorSpan)
        setAnimationError(element)
        setAnimationError(label)
    }
    else if(!(/^[0-9]([0-9]+)?[.]?([0-9]+)?$/.test(element.value))) {
        label.style.color = '#C9544D'
        errorSpan.innerHTML  = `Please enter a valid ${label.innerText.toLowerCase()}`
        setAnimationError(errorSpan)
        setAnimationError(element)
        setAnimationError(label)
    }
    else if(!(/^[0-9]([0-9]+)?[.]?([0-9]{1,2})?$/.test(element.value))) {
        label.style.color = '#C9544D'
        errorSpan.innerHTML  = "Please round to 2 decimal places"
        setAnimationError(errorSpan)
        setAnimationError(element)
        setAnimationError(label)
    }
    else if(parseFloat(element.value) == 0) {
        label.style.color = '#C9544D'
        errorSpan.innerHTML  = `${label.innerText} cannot take the value 0`
        setAnimationError(errorSpan)
        setAnimationError(element)
        setAnimationError(label)
    }
    else if(parseFloat(element.value) > (3.4 ** 38)) {
        label.style.color = '#C9544D'
        errorSpan.innerHTML  = `The ${label.innerText.toLowerCase()} value is a positive Float number`
        setAnimationError(errorSpan)
        setAnimationError(element)
        setAnimationError(label)
    }
    else {
        label.style.color = 'rgba(54, 47, 47, 1)'
        element.style.borderColor = "rgba(54, 47, 47, 0.4)"
        errorSpan.innerHTML = ''
        element.value = parseFloat(element.value).toString()
    }
}

nameElement.addEventListener("keypress", function(e) {
    if(e.keyCode == 13) {
        addProduct()
    }
    else {
        let oldValueEl = e.target.value
        if(oldValueEl.length > 99) {
            e.preventDefault()
        }
    }
})

caloriesElement.addEventListener("keypress", function(e) {
    if(e.keyCode == 13) {
        addProduct()
    }
})

proteinsElement.addEventListener("keypress", function(e) {
    if(e.keyCode == 13) {
        addProduct()
    }
})

fatsElement.addEventListener("keypress", function(e) {
    if(e.keyCode == 13) {
        addProduct()
    }
})

carbohydratesElement.addEventListener("keypress", function(e) {
    if(e.keyCode == 13) {
        addProduct()
    }
})

document.querySelector("#sever-error-box button").onclick = function hideErrorBox(e) {
    let errorBox = document.getElementById("sever-error-container");
    let errorMessage = document.getElementById("sever-error-message");
    errorBox.style.display = "none";
    errorMessage.textContent = ""
}
