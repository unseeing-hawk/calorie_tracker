var loginButton = document.querySelector('button[class="btn"]')
loginButton.onclick = registerAccount
function registerAccount(e) {
    var usernameElement = document.querySelector('#username')
    var passwordElement = document.querySelector('#password')
    var labelError = document.querySelector("#error-input")
    if(usernameElement.value.length === 0) {
        labelError.style.display = 'none'
        usernameElement.style.borderColor  = "red";
        usernameElement.style.animation = "shake 0.5s"
        setTimeout(function() {
            usernameElement.style.animation = ''
        }, 510)
    }
    else {
        usernameElement.style.borderColor  = "rgba(54, 47, 47, 0.4)"
    }
    if(passwordElement.value.length === 0) {
        labelError.style.display = 'none'
        passwordElement.style.borderColor  = "red";
        passwordElement.style.animation = "shake 0.5s"
        setTimeout(function() {
            passwordElement.style.animation = ''
        }, 510)
    }
    else {
        passwordElement.style.borderColor  = "rgba(54, 47, 47, 0.4)";
    }
    if(usernameElement.value.length !== 0 && passwordElement.value.length !== 0){
        
        if(usernameElement.value !== "admin" || passwordElement.value !== "123456" )
        {
            labelError.style.display = "block"
            labelError.style.animation = "shake 0.5s"
            setTimeout(function() {
                labelError.style.animation = ''
            }, 510)
        }
    }
}

document.addEventListener("keydown", function(event){
    if(event.code === "Enter") {
        registerAccount()
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
