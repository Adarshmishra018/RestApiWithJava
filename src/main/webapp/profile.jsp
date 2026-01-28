<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="in.nic.model.UserDetails" %>
<%
    // Get user object from session
    UserDetails user = (UserDetails) session.getAttribute("user_details");
System.out.println(" print value of user Details which is come from call back url{} " + user);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Profile</title>

<style>
    body {
        background: #f4f6f8;
    }
    .profile-card {
        max-width: 400px;
        margin: 100px auto;
        padding: 30px;
        border-radius: 10px;
        background: white;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        text-align: center;
    }
    .hidden {
        display: none;
    }
</style>

</head>
<body>
    <div class="profile-card">
        <h3>Welcome👋 <%= (user != null ? user.getFirstName() : "") %></h3> 

        <div id="viewSection">
            <p><b>Parichay ID:</b> <span id="parichayId"><%= user != null ? user.getParichayId() : "" %></span></p>
            <p><b>User ID:</b> <span id="userId"><%= user != null ? user.getUserId() : "" %></span></p>
            <p><b>Name:</b> <span id="username"><%= user != null ? user.getFirstName() + " " + user.getLastName() : "" %></span></p>
            <p><b>DOB:</b> <span id="dob"><%= user != null ? user.getDob() : "" %></span></p>
            <p><b>Gender:</b> <span id="gender"><%= user != null ? user.getGender() : "" %></span></p>
            <p><b>Mobile:</b> <span id="mobile"><%= user != null ? user.getMobile() : "" %></span></p>
            <p><b>Email:</b> <span id="email"><%= user != null ? user.getEmailId() : "" %></span></p>

           <%--  <button id="editBtn">Update Profile</button>
        </div>

        <div id="editSection" class="hidden">
            <input type="text" id="editName" value="<%= user != null ? user.getFirstName() + " " + user.getLastName() : "" %>" placeholder="Name"><br><br>
            <input type="email" id="editEmail" value="<%= user != null ? user.getEmailId() : "" %>" placeholder="Email"><br><br>
            <input type="text" id="editMobile" value="<%= user != null ? user.getMobile() : "" %>" placeholder="Mobile"><br><br>

            <button id="submitBtn">Submit</button>
            <button id="cancelBtn">Cancel</button>
        </div>
 --%>
        <br>
        <button onclick="logoutP()">Logout</button>
         <br>
         <span id="msg"></span>
         <br>
 		<button id="refreshTokenBtn">Refresh Token</button>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script src="callback.js"></script>

</body>
</html>
