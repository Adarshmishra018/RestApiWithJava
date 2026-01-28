<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="in.nic.model.SamlUser" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        .label {
            font-weight: bold;
        }
    </style>
</head>
<body>

<h2>SAML User Details</h2>
<%
    SamlUser user = (SamlUser) session.getAttribute("USER");
	String samlSessionIndex = (String) session.getAttribute("SAML_SESSION_INDEX");
	String samlNameId = (String) session.getAttribute("SAML_NAME_ID");
	String logoutUrl = "http://jpmeripehchaan.staging.nic.in/api/saml/requestlogout?"
			+"sessionIndex=" + samlSessionIndex
			+"nameId" + samlNameId;

    if (user == null) {
%>
      <h3>Welcome👋 <%= (user != null ? user.getFirstName() : "") %></h3> 

        <p style="color:red;">User not logged in</p> 
<%
    } else {
%>

<p><span class="label">First Name:</span> <%= user.getFirstName() %></p>
<p><span class="label">Last Name:</span> <%= user.getLastName() %></p>
<p><span class="label">User ID:</span> <%= user.getUserId() %></p>
<p><span class="label">Mobile:</span> <%= user.getMobileNo() %></p>
<p><span class="label">Gender:</span> <%= user.getGender() %></p>
<p><span class="label">Date of Birth:</span> <%= user.getDob() %></p>

<br>
<a href="<%= logoutUrl %>">
    <button type="button">Logout</button>
</a>

<%
    }
%>

</body>
</html>
