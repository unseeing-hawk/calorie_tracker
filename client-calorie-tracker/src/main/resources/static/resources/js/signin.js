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
loginButton.onclick = registerAccount
function registerAccount(e) {
    let usernamErrorLabel = document.querySelector('#username-error-label')
    let passwordErrorLabel = document.querySelector("#password-error-label")
    
    // input username
    if(usernameElement.value.length === 0 || usernameElement.value.trim().length == 0) {
        usernamErrorLabel.style.display = 'none'
        passwordErrorLabel.style.display = 'none'
        setAnimationError(usernameElement)
        usernameElement.value = ""
    }
    else if (usernameElement.value.length < 8){
        usernamErrorLabel.style.display = 'block'
        usernamErrorLabel.innerHTML = "Username must contain 8 characters or more!"
        setAnimationError(usernameElement)
        setAnimationError(usernamErrorLabel)
    }
    else {
        usernameElement.style.borderColor  = "rgba(54, 47, 47, 0.4)"
        usernamErrorLabel.style.display = 'none'
    }

    // input password
    if(passwordElement.value.length === 0) {
        passwordErrorLabel.style.display = 'none'
        setAnimationError(passwordElement)
    }
    else if (passwordElement.value.length < 8){
        passwordErrorLabel.style.display = 'block'
        passwordErrorLabel.innerHTML = "Password must contain 8 characters or more!"
        setAnimationError(passwordElement)
        setAnimationError(passwordErrorLabel)
    }
    else {
        passwordErrorLabel.style.display = 'none'
        passwordElement.style.borderColor  = "rgba(54, 47, 47, 0.4)";
    }
    
    if(usernameElement.value.trim().length >= 8 && passwordElement.value.length >= 8){
        usernamErrorLabel.style.display = 'none'
        let notCorrectFormatUsername = !(/^[0-9a-zA-Z\_\.]+$/.test(usernameElement.value)) || !(/[a-zA-Z]/.test(usernameElement.value))
        let notCorrectFormatPassword = (/^\s|\s$/.test(passwordElement.value)) || !(/^[a-zA-Z0-9\s\,\.\/\<\>\?\;\'\:\"\[\]\{\}\=\-\\\+\_\)\(\*\&\^\%\$\#\@\!\~\`\)]*$/.test(passwordElement.value))
        if (notCorrectFormatUsername || notCorrectFormatPassword) {
            passwordErrorLabel.style.display = 'block'
            passwordErrorLabel.innerHTML = "Incorrect username or password!"
            setAnimationError(passwordErrorLabel)
        }
        else {
            // Get data from database and check passwords and accounts
            // Try input with given username and password.
            if(usernameElement.value !== "admin123456" || passwordElement.value !== "12345678"){
                passwordErrorLabel.style.display = 'block'
                passwordErrorLabel.innerHTML = "Incorrect username or password!"
                setAnimationError(passwordErrorLabel)
            }
        }
    }
}

usernameElement.addEventListener('keypress', function(event) {
    if(event.code === "Enter") {
        registerAccount()
    }
    if(event.target.value.length > 29) {
        event.preventDefault()
    }
})

passwordElement.addEventListener('keypress', function(event) {
    if(event.code === "Enter") {
        registerAccount()
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
