/**
 * 
 */

$(document).ready(function() {     //Waits until HTML is fully loaded

	$("#registerForm").submit(function(e) {		//Attaches a submit event listener to the form
		alert("functioncalled");				//Shows a popup to confirm the function is being triggered
		e.preventDefault(); // stop form submission


		let name = $("#name").val();
		let email = $("#email").val();
		let mobile = $("#mobile").val();
		let password = $("#password").val();		//Reads values typed by the user
		let confirmPassword = $("#confirmPassword").val();	//#confirmPassword → confirm password input


		// REGEX
		const nameRegex = /^[A-Za-z ]{2,50}$/;
		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		const mobileRegex = /^[6-9]\d{9}$/;
		//const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;

		// NAME
		if (!nameRegex.test(name)) {
			showMessage("Enter valid name", "danger");
			return;
		}

		// EMAIL
		if (!emailRegex.test(email)) {
			showMessage("Enter valid email address", "danger");
			return;
		}

		// MOBILE
		if (!mobileRegex.test(mobile)) {
			showMessage("Enter valid mobile number", "danger");
			return;
		}

		// PASSWORD
	/*	if (!passwordRegex.test(password)) {
			showMessage(
				"Password must contain upper, lower, number & special chars", "danger");
			return;
		}

		// CONFIRM PASSWORD
		if (password !== confirmPassword) {
			showMessage("Passwords do not match", "danger");
			return;
		}*/

		// Data to send
		let userData = {				//Creates a JavaScript object//Collects form values//This object will be sent to backend//
			fullName: $("#name").val(),
			email: $("#email").val(),
			mobile: $("#mobile").val(),
			password: password
		};

		$.ajax({				//Starts an AJAX HTTP request      //Client → Server communication
			url: "http://oauthclient.staging.nic.in/v1/api/register/user",   //API endpoint on your Jersey server
			type: "POST",										//Sends data to server
			contentType: "application/json",					//Tells server:I am sending JSON data
			data: JSON.stringify(userData),						//Converts JS object to JSON string//Sends JSON in HTTP request body
			//data: userData,

			success: function(resp) {		//Runs after server sends a successful response // resp is parsed JSON response from server
				console.log("response :", resp);   // prints server response in browser console

				showMessage(resp.msg, resp.status);//Uses message from server

				$("#registerForm")[0].reset();//Clears all input fields//[0] → DOM element from jQuery object
			},

			error: function(xhr) {		//Runs only if:Server returns error 
			//	showMessage(xhr.responseText || "User Already exists", "Unsuccesful");// error message
				showMessage(resp.msg, resp.status);
				$("#registerForm")[0].reset();
			}
		});
	});										//	End of submit handler

});											//End of document.ready

function registerUser() {



}




function showMessage(msg, type) {				//to show Bootstrap alert
	$("#message")
		.removeClass("d-none alert-success alert-danger")
		.addClass("alert-" + type)						//
		.text(msg);									//Sets message text inside alert box
}
