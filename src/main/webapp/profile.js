/*$(document).ready(function () {

	    $.ajax({
	        url: "http://localhost:8080/v1/api/profile/user",
	        type: "GET",
	        success: function (resp) {
				    let user = JSON.parse(sessionStorage.getItem("userData"));
		 console.log(" print data value which is get from api :: ", user);
           
	$("#username").text(user.fullName);
    $("#email").text(user.email);
    $("#mobile").text(user.mobile);
    $("#userid").text(user.id);
            
	        },
	        error: function () {
	            window.location.href = "login.html";
	        }
	    });

	});*/
	
	$(document).ready(function () {

    // Load data from sessionStorage
    let userData = sessionStorage.getItem("userData");

    if (!userData) {
        window.location.href = "login.html";
        return;
    }

    let user = JSON.parse(userData);

    // Populate view
    $("#username").text(user.fullName);
    $("#email").text(user.email);
    $("#mobile").text(user.mobile);
    $("#userid").text(user.id);

    // Populate edit inputs
    $("#editName").val(user.fullName);
    $("#editEmail").val(user.email);
    $("#editMobile").val(user.mobile);

    // Edit button click
    $("#editBtn").click(function () {
        $("#viewSection").hide();
        $("#editSection").show();
    });

    // Cancel button
    $("#cancelBtn").click(function () {
        $("#editSection").hide();
        $("#viewSection").show();
    });

    // Submit button
    $("#submitBtn").click(function () {

        // Get updated values
        let updatedUser = {
            id: user.id,
            fullName: $("#editName").val(),
            email: $("#editEmail").val(),
            mobile: $("#editMobile").val()
        };

        // OPTIONAL: API call here
        /*
        $.ajax({
            url: "http://localhost:8080/v1/api/profile/update",
            type: "POST",
            data: updatedUser,
            success: function(resp) {
                alert("Profile updated");
            }
        });
        */

        // Update UI
        $("#username").text(updatedUser.fullName);
        $("#email").text(updatedUser.email);
        $("#mobile").text(updatedUser.mobile);

        // Update sessionStorage
        sessionStorage.setItem("userData", JSON.stringify(updatedUser));

        // Switch back to view
        $("#editSection").hide();
        $("#viewSection").show();
    });

});

// Logout
function logout() {
    sessionStorage.clear();
    window.location.href = "login.html";
}

	
	

function showUpdateForm() {
    $("#updateForm").toggleClass("hidden");
}

function hideUpdateForm() {
    $("#updateForm").addClass("hidden");
}


$("#updateForm").submit(function(e) {
	e.preventDefault();

	$.ajax({
		url: "/v1/api/profile/update",
		type: "POST",
		xhrFields: { withCredentials: true },
		data: {
			userid: currentUserId,
			username: $("#u_username").val(),
			email: $("#u_email").val(),
			mobile: $("#u_mobile").val()
		},
		success: function(resp) {
			showMessage(resp.msg, resp.status);//Uses message from server
			$("#updateForm").addClass("hidden");
			location.reload();   // reload profile
		}
	
	});
});
	
	
	
	function logout() {
	    fetch("http://localhost:8080/v1/api/login/logout", {
	        method: "GET",
	        credentials: "include"			//Tells the browser to send cookies along with the request
	    })
	    .then(res => res.json())			//Reads response body & Converts JSON → JavaScript object
	    .then(data => {
	        alert(data.msg);
	        window.location.href = "index.html";
	    });
	}
	
	
	
	
	/*$(document).ready(function () {

    // Get stored data
    let userData = sessionStorage.getItem("userData");
	console.log("kjhgf  " ,userData);
    if (!userData) {
        // If no session → redirect to login
        window.location.href = "login.html";
        return;
    }

    let user = JSON.parse(userData);
	console.log("qwertyu  " ,user);
    // Set values in profile page
    $("#username").text(user.fullName);
    $("#email").text(user.email);
    $("#mobile").text(user.mobile);
    $("#userid").text(user.id);

});*/

