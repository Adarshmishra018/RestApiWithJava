<%-- <%@ page contentType="text/html;charset=UTF-8" %>
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
</html> --%>





























<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="in.nic.model.SamlUser" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%
    // 1. Retrieve SAMLUser from session
    SamlUser user = (SamlUser) session.getAttribute("USER");
    String samlSessionIndex = (String) session.getAttribute("SAML_SESSION_INDEX");
    String samlNameId = (String) session.getAttribute("SAML_NAME_ID");
    String logoutUrl = "http://jpmeripehchaan.staging.nic.in/api/saml/requestlogout?"
            + "sessionIndex=" + (samlSessionIndex != null ? samlSessionIndex : "")
            + "&nameId=" + (samlNameId != null ? samlNameId : "");

    // 2. Convert SamlUser to formatted JSON string
    String samlUserJson = "{}";
    if (user != null) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            samlUserJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
        } catch (Exception e) {
            samlUserJson = "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SAML User Details</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .json-card {
            margin-top: 24px;
            background: #0f172a;
            border-radius: 12px;
            border: 1px solid #1e293b;
            overflow: hidden;
            text-align: left;
        }
        .json-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 16px;
            background: #1e293b;
            color: #94a3b8;
            font-size: 0.8rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .json-copy-btn {
            background: #334155;
            color: #f8fafc;
            border: none;
            padding: 4px 10px;
            border-radius: 6px;
            font-size: 0.75rem;
            cursor: pointer;
            width: auto;
            box-shadow: none;
        }
        .json-copy-btn:hover {
            background: #475569;
            transform: none;
        }
        .json-content {
            margin: 0;
            padding: 16px;
            color: #34d399; /* Green accent for SAML */
            background: #0f172a;
            font-family: 'Fira Code', Consolas, 'Courier New', monospace;
            font-size: 0.85rem;
            line-height: 1.5;
            max-height: 300px;
            overflow-y: auto;
            white-space: pre-wrap;
            word-break: break-all;
        }
    </style>
</head>
<body>

<div class="profile-card">
    <div class="profile-header">
        <div class="avatar-badge" style="background: linear-gradient(135deg, #059669, #10b981);">🔒</div>
        <h2>SAML 2.0 User Profile</h2>
        <p class="subtitle">Authenticated through Meri Pehchaan Identity Provider</p>
    </div>

<% if (user == null) { %>
    <div class="alert alert-danger" style="display:block;">
        ⚠️ Session expired or user is not logged in.
    </div>
    <div class="button-stack">
        <a href="index.html" class="btn">Return to Login</a>
    </div>
<% } else { %>
    <!-- Individual Profile Items -->
    <div class="profile-grid">
        <div class="profile-item">
            <span class="label">Parichay ID:</span>
            <span><%= user.getParichayId() != null ? user.getParichayId() : "—" %></span>
        </div>
        <div class="profile-item">
            <span class="label">First Name:</span>
            <span><%= user.getFirstName() != null ? user.getFirstName() : "—" %></span>
        </div>
        <div class="profile-item">
            <span class="label">Last Name:</span>
            <span><%= user.getLastName() != null ? user.getLastName() : "—" %></span>
        </div>
        <div class="profile-item">
            <span class="label">User ID:</span>
            <span><%= user.getUserId() != null ? user.getUserId() : "—" %></span>
        </div>
        <div class="profile-item">
            <span class="label">Mobile:</span>
            <span><%= user.getMobileNo() != null ? user.getMobileNo() : "—" %></span>
        </div>
        <div class="profile-item">
            <span class="label">Gender:</span>
            <span><%= user.getGender() != null ? user.getGender() : "—" %></span>
        </div>
        <div class="profile-item">
            <span class="label">Date of Birth:</span>
            <span><%= user.getDob() != null ? user.getDob() : "—" %></span>
        </div>
    </div>

    <!-- WHOLE JSON RESPONSE DISPLAY -->
    <div class="json-card">
        <div class="json-header">
            <span>SAML User Attributes (JSON)</span>
            <button type="button" class="json-copy-btn" onclick="copyJson('samlJson')">Copy JSON</button>
        </div>
        <pre class="json-content"><code id="samlJson"><%= samlUserJson %></code></pre>
    </div>

    <div class="button-stack" style="margin-top: 20px;">
        <a href="<%= logoutUrl %>" style="text-decoration: none;">
            <button type="button" class="btn btn-danger">Single Logout (SLO)</button>
        </a>
    </div>
<% } %>
</div>

<script>
function copyJson(elementId) {
    const text = document.getElementById(elementId).innerText;
    navigator.clipboard.writeText(text).then(() => {
        alert("JSON copied to clipboard!");
    });
}
</script>
</body>
</html>
