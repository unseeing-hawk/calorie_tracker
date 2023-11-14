var showhideElements = document.querySelectorAll('.input-box .showhide')
for(var i of showhideElements) {
      i.onclick = changeTypePassword
}

function changeTypePassword (){
    let passwordElemnt = document.getElementById('password')
    password.type = password.type === "text" ? "password" : "text"
    let inputPwd = document.querySelector('.input-password #password')
    inputPwd.focus()
}


var nameElement = document.querySelector('#name')
var weightElement = document.querySelector('#weight')
var passwordElement = document.querySelector('#password')

var saveChagneButton = document.querySelector('#btn-save')
saveChagneButton.onclick = registerAccount
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

    if(passwordElement.value.length == 0) {
        passwordElement.style.borderColor = "red"
        passwordElement.style.animation = 'shake 0.5s'
        setTimeout(() => {
            passwordElement.style.animation = ''
        }, 510);
    }
    
}