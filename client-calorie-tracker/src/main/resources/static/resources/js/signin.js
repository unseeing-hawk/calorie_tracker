function setAnimationError(el) {
    el.style.borderColor = "#C9544D"
    el.style.animation = 'shake 0.5s'
    setTimeout(() => {
        el.style.animation = ''
    }, 510);
}

var usernameElement = document.querySelector('#username')
var passwordElement = document.querySelector('#password')

var loginButton = document.querySelector('button[class="btn"]')
loginButton.onclick = loginAccount
function loginAccount(e) {
    let isCorrectFormatUsername = false
    let isCorrectFormatPassword = false
    
    let usernamErrorLabel = document.querySelector('#username-error-label')
    let passwordErrorLabel = document.querySelector("#password-error-label")

    // input username
    if(usernameElement.value.length === 0) {
        usernamErrorLabel.style.display = 'none'
        passwordErrorLabel.style.display = 'none'
        setAnimationError(usernameElement)
    }
    else if(usernameElement.value.trim().length == 0) {
        usernamErrorLabel.style.display = 'block'
        usernamErrorLabel.innerHTML = "Username cannot contain only space character"
        setAnimationError(usernameElement)
        setAnimationError(usernamErrorLabel)
    }
    else if(usernameElement.value.length < 8 || usernameElement.value.length > 30){
        usernamErrorLabel.style.display = 'block'
        usernamErrorLabel.innerHTML = "Username must contain from 8 to 30 characters"
        setAnimationError(usernameElement)
        setAnimationError(usernamErrorLabel)
    }
    else {
        usernameElement.style.borderColor  = "rgba(54, 47, 47, 0.4)"
        usernamErrorLabel.style.display = 'none'
        isCorrectFormatUsername = true
    }

    // input password
    if(passwordElement.value.length === 0) {
        passwordErrorLabel.style.display = 'none'
        setAnimationError(passwordElement)
    }
    else if(passwordElement.value.trim().length == 0){
        passwordErrorLabel.style.display = 'block'
        passwordErrorLabel.innerHTML = "Password cannot contain only space character"
        setAnimationError(passwordElement)
        setAnimationError(passwordErrorLabel)
    }
    else if(passwordElement.value.length < 8 || passwordElement.value.length > 50){
        passwordErrorLabel.style.display = 'block'
        passwordErrorLabel.innerHTML = "Password must contain from 8 to 50 characters"
        setAnimationError(passwordElement)
        setAnimationError(passwordErrorLabel)
    }
    else {
        passwordErrorLabel.style.display = 'none'
        passwordElement.style.borderColor  = "rgba(54, 47, 47, 0.4)";
        isCorrectFormatPassword = true
    }

    if(isCorrectFormatUsername && isCorrectFormatPassword ){
        document.getElementById('loginForm').submit();
    }
}

usernameElement.addEventListener('keypress', function(event) {
    if(event.code === "Enter") {
        loginAccount()
    }
    if(event.target.value.length > 29) {
        event.preventDefault()
    }
})

passwordElement.addEventListener('keypress', function(event) {
    if(event.code === "Enter") {
        loginAccount()
    }
    if(event.target.value.length > 49) {
        event.preventDefault()
    }
})

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

document.querySelector("#sever-error-box button").onclick = function hideErrorBox(e) {
    let errorBox = document.getElementById("sever-error-container");
    let errorMessage = document.getElementById("sever-error-message");
    errorBox.style.display = "none";
    errorMessage.textContent = ""
}