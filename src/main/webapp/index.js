 function openLogin() {
        window.location.href = "login.html";
    }

    function openRegister() {
        window.location.href = "register.html";
    }
   $(document).ready(function () { 
	$("#samlLoginBtn").on("click", function() {
				// Redirect to backend OAuth start endpoint
				console.log("saml login clicked");
    window.location.href = "http://jpmeripehchaan.staging.nic.in/api/saml/login";
});
});
	
    