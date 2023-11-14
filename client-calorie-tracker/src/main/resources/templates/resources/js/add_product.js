var nameElement = document.querySelector("#name")
var caloriesElement = document.querySelector("#calories")
var proteinsElement = document.querySelector("#proteins")
var fatsElement = document.querySelector("#fats")
var carbohydratesElement = document.querySelector("#carbohydrates")

document.querySelector('#btn-add').onclick = function() {
    if(nameElement.value.length == 0) {
        nameElement.style.borderColor = "red"
        nameElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            nameElement.style.animation = ''
        }, 510);
    }
    else {
        nameElement.style.borderColor  = "rgba(54, 47, 47, 0.4)"
    }

    if(caloriesElement.value.length == 0) {
        caloriesElement.style.borderColor = "red"
        caloriesElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            caloriesElement.style.animation = ''
        }, 510);
    }
    else {
        caloriesElement.style.borderColor  = "rgba(54, 47, 47, 0.4)"
    }

    if(proteinsElement.value.length == 0) {
        proteinsElement.style.borderColor = "red"
        proteinsElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            proteinsElement.style.animation = ''
        }, 510);
    }
    else {
        proteinsElement.style.borderColor  = "rgba(54, 47, 47, 0.4)"
    }

    if(fatsElement.value.length == 0) {
        fatsElement.style.borderColor = "red"
        fatsElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            fatsElement.style.animation = ''
        }, 510);
    }
    else {
        fatsElement.style.borderColor  = "rgba(54, 47, 47, 0.4)"
    }

    if(carbohydratesElement.value.length == 0) {
        carbohydratesElement.style.borderColor = "red"
        carbohydratesElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            carbohydratesElement.style.animation = ''
        }, 510);
    }
    else {
        carbohydratesElement.style.borderColor  = "rgba(54, 47, 47, 0.4)"
    }
}
