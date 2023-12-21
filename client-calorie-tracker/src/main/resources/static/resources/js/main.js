var settingsBox = document.querySelector('#setting-box')
settingsBox.onmouseover = function (e) {
    let element = document.querySelector('#setting-box a')
    element.style.display = "contents"
    element.style.cursor = 'pointer'
}

settingsBox.onmouseout = function (e) {
    document.querySelector('#setting-box a').style.display = "none"
}

var signoutBox = document.getElementById('signout-box')
signoutBox.onmouseover = function(e) {
    let element = document.querySelector('#signout-box button')
    element.style.display = "contents"
    element.style.cursor = 'pointer'
}

signoutBox.onmouseout = function (e) {
    document.querySelector('#signout-box button').style.display = "none"
}

document.querySelector("#sever-error-box button").onclick = function hideErrorBox(e) {
    let errorBox = document.getElementById("sever-error-container");
    let errorMessage = document.getElementById("sever-error-message");
    errorBox.style.display = "none";
    errorMessage.textContent = ""
}
