function setAnimationError(el) {
    el.style.borderColor = "#C9544D"
    el.style.animation = 'shake 0.5s'
    setTimeout(() => {
        el.style.animation = ''
    }, 510);
}

var nameElement = document.querySelector('#name')
var weightElement = document.querySelector('#weight')
var passwordElement = document.querySelector('#password')
var btnElement = document.querySelector('#btn-save')

btnElement.onclick = saveChange
function saveChange(e) {
    // input name
    nameElement.value = nameElement.value.trim()
    let nameErrorSpan = document.querySelector("#name-error-span")
    let patternName = /^[a-zA-Z\s]+$/
    if(nameElement.value.length == 0) {
        setAnimationError(nameElement)
        nameErrorSpan.innerHTML = ""
    }
    else if (!patternName.test(nameElement.value)){
        nameErrorSpan.innerHTML  = "The name only contains the following characters: a-z, A-Z and spaces"
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
        nameElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        nameErrorSpan.innerHTML = ""
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

    // input weight
    let weightErrorSpan = document.querySelector('#weight-error-span')
    if(weightElement.value.length == 0) {
        setAnimationError(weightElement)
        weightErrorSpan.innerHTML = ''
    }
    else if((/[^0-9.]/.test(weightElement.value))) {
        weightErrorSpan.innerHTML  = "The weight only contains the following characters: 0-9 and '.'"
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
    }
    else if(!(/^[0-9]([0-9]+)?[].?([0-9]+)?$/.test(weightElement.value))) {
        weightErrorSpan.innerHTML  = "Please enter a valid weight."
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
    }
    else if(!(/^[0-9]([0-9]+)?[.]?([0-9]{1,2})?$/.test(weightElement.value))) {
        weightErrorSpan.innerHTML  = "Please round to 2 decimal places"
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
    }
    else if(parseFloat(weightElement.value) == 0) {
        weightErrorSpan.style.display = 'block'
        weightErrorSpan.innerHTML  = "Weight cannot take the value 0"
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
    }
    else if(parseFloat(weightElement.value) > (3.4 ** 38)) {
        weightErrorSpan.innerHTML  = "The weight value is a positive Float number"
        setAnimationError(weightErrorSpan)
        setAnimationError(weightElement)
    }
    else {
        weightElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        weightErrorSpan.innerHTML = ''
    }

    // input password
    let passwordErrorSpan = document.querySelector('#password-error-span')
    if(passwordElement.value.length == 0) {
        setAnimationError(passwordElement)
        passwordErrorSpan.innerHTML = ''
    }
    else if(passwordElement.value.trim().length == 0) {
        passwordErrorSpan.innerHTML  = "Password cannot contain only space characters"
        setAnimationError(passwordErrorSpan)
        setAnimationError(passwordElement)
    }
    else if(passwordElement.value.length < 8) {
        passwordErrorSpan.innerHTML  = "Password must contain 8 characters or more"
        setAnimationError(passwordErrorSpan)
        setAnimationError(passwordElement)
    }
    else {
        passwordElement.style.borderColor = "rgba(54, 47, 47, 0.4)"
        passwordErrorSpan.innerHTML = ''
    }
}

nameElement.addEventListener("keypress", function(event) {
    // Khi nhập thì chữ cái (sau dấu cách hoặc là ký tự đầu tiên thì tự động viết hoa)
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
        saveChange()
    }
    if(oldValueEl.length > 99) {
        event.preventDefault()
    }
    if(btnElement.getAttribute('disabled') == false) {
        btnElement.setAttribute('disabled', 'true')
    }
})

weightElement.addEventListener('keypress', function(e) {
    if(e.keyCode == 13) {
        saveChange()
    }
    if(btnElement.getAttribute('disabled') == false) {
        btnElement.setAttribute('disabled', 'true')
    }
})

passwordElement.addEventListener('keypress', function(e) {
    if(e.keyCode == 13) {
        saveChange()
    }
    if(passwordElement.value.length > 49) {
        e.preventDefault()
    }
    if(btnElement.getAttribute('disabled') == false) {
        btnElement.setAttribute('disabled', 'true')
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
