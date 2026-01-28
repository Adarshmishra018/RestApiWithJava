$(document).ready(function () {

		// Load data from sessionStorage

		// let userData = sessionStorage.getItem("userData");
		let userDataStr = sessionStorage.getItem("userData");

		console.log("userData: ", userDataStr);
		if (!userDataStr) {
			window.location.href = "login.html";
			return;
		}

		let user = JSON.parse(userDataStr);
		console.log("user: ", user);
		// shows user details
		$("#username").text(user.fullName);
		$("#email").text(user.email);
		$("#mobile").text(user.mobile);
		$("#userid").text(user.id);

		// Populate edit inputs
		$("#editName").val(user.fullName);
		$("#editEmail").val(user.email);
		$("#editMobile").val(user.mobile);

		// Edit button click
		$("#editBtn").click(function() {
			$("#viewSection").hide();
			$("#editSection").show();
		});

		// Cancel button
		$("#cancelBtn").click(function() {
			$("#editSection").hide();
			$("#viewSection").show();
		});

		// Submit button

		$("#submitBtn").on("click", function(e) {
			e.preventDefault(); // STOP form submit
			// Ensure user exists
			if (!user || !user.id) {
				console.error("User data missing");
				return;
			}

			let updatedUser = {
				id: user.id,
				fullName: $("#editName").val(),
				email: $("#editEmail").val(),
				mobile: $("#editMobile").val(),
				//s userId:''
			};

			$.ajax({
				url: "http://oauthclient.staging.nic.in/api/login/update",
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				data: JSON.stringify(updatedUser),
				success: function(resp) {
					alert("Update successful");
					showMessage(resp.msg, resp.status);
					$("#updateForm").addClass("hidden");
					location.reload();
				},
				error: function(xhr) {
					console.error("Update failed", xhr.responseText);
									}
			});
		    

        // Update UI
        $("#username").text(updatedUser.fullName);
        $("#email").text(updatedUser.email);
        $("#mobile").text(updatedUser.mobile);

        //set Updated details in session
        sessionStorage.setItem("userData", JSON.stringify(updatedUser));

        // Switch back to view
        $("#editSection").hide();
        $("#viewSection").show();
		});	
	}); 	
		function showUpdateForm() {
			$("#updateForm").toggleClass("hidden");
		}
		
		function hideUpdateForm() {
			$("#updateForm").addClass("hidden");
		}
		
		
		$("#updateForm").submit(function(e) {
			e.preventDefault();
		});

	
	
function logout() {
	    fetch("http://oauthclient.staging.nic.in/api/login/logout", {
	        method: "GET",
	        credentials: "include"			//Tells the browser to send cookies along with the request
	    })
	    .then(res => res.json())			//Reads response body & Converts JSON → JavaScript object
	    .then(data => {
	        alert(data.msg);
	        window.location.href = "index.html";
	    });
}
	
	
	
	
