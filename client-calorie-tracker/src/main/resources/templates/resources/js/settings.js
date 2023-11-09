var ExitButton = document.querySelector('#btn-back')
ExitButton.onclick = moveMainHTML
function moveMainHTML(e) {
    if(confirm("You want to exit??"))
    {
        window.location.href = "http://127.0.0.1:5500/main.html";
    }
    
}

var saveButton = document.querySelector('#btn-save')
saveButton.onclick = savemoveMainHTML
function savemoveMainHTML(e) {
    window.location.href = "http://127.0.0.1:5500/main.html";
}

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
