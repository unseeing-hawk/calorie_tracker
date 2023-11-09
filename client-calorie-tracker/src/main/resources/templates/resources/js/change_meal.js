var backToMainButton = document.querySelector('#btn-back-to-main')
backToMainButton.onclick = moveMainHTML
function moveMainHTML(e) {
    if(confirm("You want to exit??"))
    {
        window.location.href = "http://127.0.0.1:5500/main.html";
    }
}
