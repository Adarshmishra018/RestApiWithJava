$(document).ready(function() {      // Waits until HTML is fully loaded

	$("#loginForm").submit(function(e) {   // Attach submit event to login form
		alert("login function called");
		e.preventDefault();               // Stop normal form submission	
		// Read user inputs
		let email = $("#email").val();
		let password = $("#password").val();


		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		//const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;

		// Basic validation
		if (!email ) {		//|| !password
			alert("Email and password are required", "danger");
			return;
		}

		// EMAIL
		if (!emailRegex.test(email)) {
			showMessage("Enter valid email address", "danger");
			return;
		}

		// PASSWORD
		/*if (!passwordRegex.test(password)) {
			showMessage(
				"Password must contain upper, lower, number & special chars", "danger");
			return;
		}*/

		// Data to send to backend
		let loginData = {
			email: email,
			password: password
		};

		$.ajax({                     // Start AJAX request (Client → Server)
			url: "http://oauthclient.staging.nic.in/v1/api/login/user", // Login API
			type: "POST",
			//contentType: "application/x-www-form-urlencoded"
			// contentType: "application/json",               // Sending JSON
			//data: JSON.stringify(loginData),               // Convert JS → JSON
			data: loginData,

			success: function(resp) {   // Runs if server return 200
				console.log("response :", resp);
	
				// Show message returned by server
				showMessage(resp.msg, resp.status);
			// redirect on successful login
				if (resp.status == "success") {
					 sessionStorage.setItem("userData", JSON.stringify(resp.data));
					 window.location.href = "profile.html";
					 console.log("response :", resp);
					 debugger;
				}
			},

			error: function(xhr) {     // Runs if server returns error
			//	showMessage(xhr.responseText || "Login failed", "danger");
				let message = "Login failed";

				if (xhr.responseJSON && xhr.responseJSON.msg) {
					message = xhr.responseJSON.msg;   // "Invalid password"
				}

				showMessage(message, "danger");

				// Reset form
				$("#loginForm")[0].reset();
			}
		
		});
});
	
	
	
	
	$("#parichayLoginBtn").on("click", function () {
            // Redirect to backend OAuth start endpoint
            console.log("response :");
           window.location.href = "http://oauthclient.staging.nic.in/v1/api/login1/loginOAuth";
        });
        
       // $("#parichayLoginBtn").on("click", function () {
			/* const params = new URLSearchParams({
	        client_id: "oauthcliente4l1qijq8cmwsob27gpqf",
	        redirect_uri: "http://oauthclient.staging.nic.in/v1/api/login1/callback",
	        scope: "user_details",
	        state: crypto.randomUUID(),
	        code_challenge_method: "S256",
	        code_challenge: "bWosNXhfXulSnB4dQCsbRi1xxv4VUen1zOgciqzIXj4",
	        response_type: "code"
	    });

	    const url = "http://oauthclient.staging.nic.in/v1/api/login1/v1/oauth2/authorize?" + params.toString();
*/
	    // 🔁 Redirect browser
	  //  window.location.href = url;
   /* $.ajax({
        url: "http://oauthclient.staging.nic.in/v1/api/login1/loginOAuth",
        type: "GET",
        success: function (res) {
			console.info(res);
            window.open(res.authUrl, "_blank"); // new tab
        }
    });*/
/*        $("#parichayLoginBtn").on("click", function () {
    $.ajax({
        url: "http://oauthclient.staging.nic.in/v1/api/login1/loginOAuth",
        type: "GET",
        success: function (res) {
            window.open(res.authUrl, "_blank"); // new tab
        }
    });*/

//});


});

/* Utility function to show Bootstrap alert */
function showMessage(msg, type) {
	$("#message")
		.removeClass("d-none alert-success alert-danger")
		.addClass("alert-" + type)
		.text(msg);
}

 