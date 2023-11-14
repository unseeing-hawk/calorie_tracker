var nameElement = document.querySelector('#name')
var weightElement = document.querySelector('#weight')
var logintElement = document.querySelector('#username')
var passwordElement = document.querySelector('#password')

var signupButton = document.querySelector('button[class="btn"]')
signupButton.onclick = registerAccount
function registerAccount(e) {
    if(nameElement.value.length == 0) {
        nameElement.style.borderColor = "red"
        nameElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            nameElement.style.animation = ''
        }, 510);
    }

    if(weightElement.value.length == 0) {
        weightElement.style.borderColor = "red"
        weightElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            weightElement.style.animation = ''
        }, 510);
    }

    if(logintElement.value.length == 0) {
        logintElement.style.borderColor = "red"
        logintElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            logintElement.style.animation = ''
        }, 510);
    }

    if(passwordElement.value.length == 0) {
        passwordElement.style.borderColor = "red"
        passwordElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            passwordElement.style.animation = ''
        }, 510);
    }
    
}
