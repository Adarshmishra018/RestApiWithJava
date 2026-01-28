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
		if (!email) {		//|| !password
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
		let loginData = {			//set email & passsword to loginData 
			email: email,
			password: password
		};

				$.ajax({                     // Start AJAX request (Client → Server)
					url: "http://oauthclient.staging.nic.in/api/login/user", // Login API
					type: "POST",
					data: loginData,								//sends data with APi call
		
					success: function(resp) {   // Runs if server return 200
						console.log("response :", resp);
						const userData = resp.data;
						console.log("response data 1111 :", userData);
						
						// Show response
						showMessage(resp.msg, resp.status);
						// redirect on successful login
						sessionStorage.setItem("userData", JSON.stringify(resp.data));//Store userData in Sesssion
						window.location.href = "profile.html";
						console.log("response :", resp);
					},
		
					error: function(xhr) {     // Runs if server returns error
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

			$("#parichayLoginBtn").on("click", function() {
				// Redirect to backend OAuth start endpoint
				console.log("response :");
				window.location.href = "http://oauthclient.staging.nic.in/api/login1/loginOAuth";
			});


});

/* Utility function to show Bootstrap alert */
function showMessage(msg, type) {
	$("#message")
		.removeClass("d-none alert-success alert-danger")
		.addClass("alert-" + type)
		.text(msg);
}

