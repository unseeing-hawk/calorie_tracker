var loginButton = document.querySelector('button[class="btn"]')
loginButton.onclick = moveMainHTML
function moveMainHTML(e) {
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