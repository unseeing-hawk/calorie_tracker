document.querySelector('#btn-back').onclick = function() {
    if(confirm("You want to exit??"))
    {
        window.location.href = "http://127.0.0.1:5500/main.html";
    }
}

document.querySelector('#btn-add').onclick = function() {
    window.location.href = "http://127.0.0.1:5500/main.html";
}
