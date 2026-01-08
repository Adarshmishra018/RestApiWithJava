document.getElementById("forgotForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const form = e.target;
    const password = form.password.value;
    const confirm = form.confirmPassword.value;

    if (password !== confirm) {
        alert("Passwords do not match");
        return;
    }

    fetch("/v1/api/login/reset", {
        method: "POST",
        body: new FormData(form)
    })
    .then(res => res.json())
    .then(data => {
        alert(data.msg);
        if (data.success) {
            window.location.href = "login.html";
        }
    });
});
