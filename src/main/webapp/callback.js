function logoutP() {
	    
	    $.ajax({
	        url: "http://oauthclient.staging.nic.in/api/login1/logout",
	        type: "POST",
	        success: function () {
	            console.log("Logout successful");
	            sessionStorage.clear();
    			window.location.href = "index.html";
	        },
	        error: function () {
	            console.log("Error");
	        }
	    });
	}
	
	
	
$("#refreshTokenBtn").click(function () {

    $("#msg").text("Refreshing token...");

    $.ajax({
        url: "http://oauthclient.staging.nic.in/api/login1/refresh",
        type: "POST",
        success: function () {
            $("#msg").text("Token refreshed successfully");
            console.log("Refresh successful");
        },
        error: function () {
            $("#msg").text("Refresh token failed. Please login again.");
        }
    });
});