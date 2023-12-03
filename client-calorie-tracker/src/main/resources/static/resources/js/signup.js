function setAnimationError(el) {
    el.style.borderColor = "#C9544D"
    el.style.animation = 'shake 0.5s'
    setTimeout(() => {
        el.style.animation = ''
    }, 510);
}

var nameElement = document.querySelector('#name')
var weightElement = document.querySelector('#weight')
var usernameElement = document.querySelector('#username')
var passwordElement = document.querySelector('#password')

var signupButton = document.querySelector('button[class="btn"]')
signupButton.onclick = registerAccount
function registerAccount(e) {
    let isCorrectFormatName = false
    let isCorrectFormatWeight = false
    let isCorrectFormatUsername = false
    let isCorrectFormatPassword = false

    // input name
    nameElement.value = nameElement.value.trim()
    let nameErrorSpan = document.querySelector('#name-error-span') 
    let nameLabel =  document.querySelector('#name-label') 
    let patternName = /^[a-zA-Z\s]+$/
    if(nameElement.value.length == 0) {
        nameLabel.style.color = '#C9544D'
        setAnimationError(nameLabel)
        setAnimationError(nameElement)
        nameErrorSpan.style.display = 'none'
    }
    else if (!patternName.test(nameElement.value)){
        nameErrorSpan.style.display = 'block'
        nameErrorSpan.innerHTML  = "The name only contains the following characters: a-z, A-Z and spaces"
        nameLabel.style.color = '#C9544D'
        setAnimationError(nameLabel)
        setAnimationError(nameErrorSpan)
        setAnimationError(nameElement)
    }
    else if(!(/^[A-Z][a-zA-Z]*(\s*\s[A-Z][a-zA-Z]*)*$/.test(nameElement.value))){
        nameErrorSpan.style.display = 'block'
        nameErrorSpan.innerHTML  = "Format error! The first letters of the name must be capitalized."
        nameLabel.style.color = '#C9544D'
        setAnimationError(nameLabel)
        setAnimationError(nameErrorSpan)
        setAnimationError(nameElement)
    }
    else {
        nameElement.style.color = '#0b0202c3'
        nameLabel.style.color = 'rgba(54, 47, 47, 1)'
        nameElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        nameErrorSpan.style.display = 'none'
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
        isCorrectFormatName = true
    }

    // input weight
    let weightErrorSpan = document.querySelector('#weight-error-span')
    let weightLabel = document.querySelector('#weight-label')
    if(weightElement.value.length == 0) {
        weightLabel.style.color = '#C9544D'
        setAnimationError(weightLabel)
        setAnimationError(weightElement)
        weightErrorSpan.style.display = 'none'
    }
    else if((/[^0-9.]/.test(weightElement.value))) {
        weightErrorSpan.style.display = 'block'
        weightLabel.style.color = '#C9544D'
        weightErrorSpan.innerHTML  = "The weight only contains the following characters: 0-9 and '.'"
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
        setAnimationError(weightLabel)
    }
    else if(!(/^[0-9]([0-9]+)?.?([0-9]+)?$/.test(weightElement.value))) {
        weightErrorSpan.style.display = 'block'
        weightLabel.style.color = '#C9544D'
        weightErrorSpan.innerHTML  = "Please enter a valid weight."
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
        setAnimationError(weightLabel)
    }
    else if(!(/^[0-9]([0-9]+)?.?([0-9]{1,2})?$/.test(weightElement.value))) {
        weightErrorSpan.style.display = 'block'
        weightLabel.style.color = '#C9544D'
        weightErrorSpan.innerHTML  = "Please round to 2 decimal places"
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
        setAnimationError(weightLabel)
    }
    else if(parseFloat(weightElement.value) == 0) {
        weightErrorSpan.style.display = 'block'
        weightLabel.style.color = '#C9544D'
        weightErrorSpan.innerHTML  = "Weight cannot take the value 0"
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
        setAnimationError(weightLabel)
    }
    else if(parseFloat(weightElement.value) > (3.4 ** 38)) {
        weightErrorSpan.style.display = 'block'
        weightLabel.style.color = '#C9544D'
        weightErrorSpan.innerHTML  = "The weight value is a positive Float number"
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
        setAnimationError(weightLabel)
    }
    else {
        weightLabel.style.color = 'rgba(54, 47, 47, 1)'
        weightElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        weightErrorSpan.style.display = 'none'
        isCorrectFormatWeight = true
    }
    
    // input username
    let usernameErrorSpan = document.querySelector('#username-error-span')
    let usernameLabel = document.querySelector('#username-label')
    if(usernameElement.value.length == 0) {
        usernameLabel.style.color = '#C9544D'
        setAnimationError(usernameLabel)
        setAnimationError(usernameElement)
        usernameErrorSpan.style.display = 'none'
    }
    else if(usernameElement.value.trim().length == 0) {
        usernameErrorSpan.style.display = 'block'
        usernameLabel.style.color = '#C9544D'
        usernameErrorSpan.innerHTML  = "Username cannot contain only space characters"
        setAnimationError(usernameErrorSpan)
        setAnimationError(usernameElement)
        setAnimationError(usernameLabel)
    }
    else if(usernameElement.value.length < 8){
        usernameErrorSpan.style.display = 'block'
        usernameLabel.style.color = '#C9544D'
        usernameErrorSpan.innerHTML  = "Username must contain 8 characters or more"
        setAnimationError(usernameErrorSpan)
        setAnimationError(usernameElement)
        setAnimationError(usernameLabel)
    }
    else{
        usernameLabel.style.color = 'rgba(54, 47, 47, 1)'
        usernameElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        usernameErrorSpan.style.display = 'none'
        isCorrectFormatUsername = true
    }
    
    // input password
    let passwordErrorSpan = document.querySelector('#password-error-span')
    let passwordLabel = document.querySelector('#password-label')
    if(passwordElement.value.length == 0) {
        passwordLabel.style.color = '#C9544D'
        setAnimationError(passwordLabel)
        setAnimationError(passwordElement)
        passwordErrorSpan.style.display = 'none'
    }
    else if(passwordElement.value.trim().length == 0) {
        passwordErrorSpan.style.display = 'block'
        passwordLabel.style.color = '#C9544D'
        passwordErrorSpan.innerHTML  = "Password cannot contain only space characters"
        setAnimationError(passwordErrorSpan)
        setAnimationError(passwordElement)
        setAnimationError(passwordLabel)
    }
    else if(passwordElement.value.length < 8) {
        passwordErrorSpan.style.display = 'block'
        passwordLabel.style.color = '#C9544D'
        passwordErrorSpan.innerHTML  = "Password must contain 8 characters or more"
        setAnimationError(passwordErrorSpan)
        setAnimationError(passwordElement)
        setAnimationError(passwordLabel)
    }
    else {
        passwordLabel.style.color = 'rgba(54, 47, 47, 1)'
        passwordElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        passwordErrorSpan.style.display = 'none'
        isCorrectFormatPassword = true
    }

    if(isCorrectFormatName && isCorrectFormatWeight  && isCorrectFormatUsername  && isCorrectFormatPassword ){
        document.getElementById('registerForm').submit();
    }

}

nameElement.addEventListener("keypress", function(event) {
    // Khi nhập thì chữ cái (sau dấu cách hoặc là ký tự đầu tiên thì tự động viết hoa)
    // When entering, letters (after a space or the first character are automatically capitalized)
    let el = event.target
    let oldValueEl = el.value
    let charInput = event.keyCode;
    let start = el.selectionStart
    let end = el.selectionEnd
    let previousChar = oldValueEl.charAt(start - 1)
    if(((charInput >= 97) && (charInput <= 122))) { 
        if(charInput != 32) {
            if((!event.ctrlKey && !event.metaKey && !event.altKey && previousChar == ' ') || start == 0) {
                let newChar = charInput - 32
                el.value = oldValueEl.substring(0, start) + String.fromCharCode(newChar) + oldValueEl.substring(end)
                el.setSelectionRange(start+1, start+1)
                event.preventDefault()
            }
        }
    }
    if(charInput == 13) {
        registerAccount()
    }
    if(oldValueEl.length > 99) {
        event.preventDefault()
    }
})

nameElement.oninvalid  = function(e) {
    e.preventDefault()
}

weightElement.addEventListener('keypress', function(e) {
    if(e.keyCode == 13) {
        registerAccount()
    }
})

weightElement.oninvalid  = function(e) {
    e.preventDefault()
}

usernameElement.addEventListener('keypress', function(e) {
    if(e.keyCode == 13) {
        registerAccount()
    }
    if(usernameElement.value.length > 29) {
        e.preventDefault()
    }
})

usernameElement.oninvalid  = function(e) {
    e.preventDefault()
}

passwordElement.addEventListener('keypress', function(e) {
    if(e.keyCode == 13) {
        registerAccount()
    }
    if(passwordElement.value.length > 49) {
        e.preventDefault()
    }
})

passwordElement.oninvalid  = function(e) {
    e.preventDefault()
}

document.querySelector("#errorBox button").onclick = function hideErrorBox(e) {
    let errorBox = document.getElementById("errorContainer");
    let errorMessage = document.getElementById("error-message");
    errorBox.style.display = "none";
    errorMessage.textContent = ""
}