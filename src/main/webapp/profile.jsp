 
 <%-- <%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

            <button id="editBtn">Update Profile</button>
        </div>

        <div id="editSection" class="hidden">
            <input type="text" id="editName" value="<%= user != null ? user.getFirstName() + " " + user.getLastName() : "" %>" placeholder="Name"><br><br>
            <input type="email" id="editEmail" value="<%= user != null ? user.getEmailId() : "" %>" placeholder="Email"><br><br>
            <input type="text" id="editMobile" value="<%= user != null ? user.getMobile() : "" %>" placeholder="Mobile"><br><br>

            <button id="submitBtn">Submit</button>
            <button id="cancelBtn">Cancel</button>
        </div>

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
  --%>
 
 
 
 
 
 <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="in.nic.model.UserDetails" %>
        <% // Get user object from session
        UserDetails user=(UserDetails) session.getAttribute("user_details");
            System.out.println(" print value of user Details which is come from call back url{} " + user);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset=" UTF-8">
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
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    text-align: center;
                }

                .hidden {
                    display: none;
                }
            </style>

            </head>

            <body>
                <div class="profile-card">
                    <h3>Welcome👋 <%= (user !=null ? user.getFirstName() : "" ) %>
                    </h3>

                    <div id="viewSection">
                        <p><b>Parichay ID:</b> <span id="parichayId">
                                <%= user !=null ? user.getParichayId() : "" %>
                            </span></p>
                        <p><b>User ID:</b> <span id="userId">
                                <%= user !=null ? user.getUserId() : "" %>
                            </span></p>
                        <p><b>Name:</b> <span id="username">
                                <%= user !=null ? user.getFirstName() + " " + user.getLastName() : "" %>
                            </span></p>
                        <p><b>DOB:</b> <span id="dob">
                                <%= user !=null ? user.getDob() : "" %>
                            </span></p>
                        <p><b>Gender:</b> <span id="gender">
                                <%= user !=null ? user.getGender() : "" %>
                            </span></p>
                        <p><b>Mobile:</b> <span id="mobile">
                                <%= user !=null ? user.getMobile() : "" %>
                            </span></p>
                        <p><b>Email:</b> <span id="email">
                                <%= user !=null ? user.getEmailId() : "" %>
                            </span></p>

                        <button id="editBtn">Update Profile</button>
                    </div>

                    <div id="editSection" class="hidden">
                        <input type="text" id="editName"
                            value="<%= user != null ? user.getFirstName() + " " + user.getLastName() : "" %>"
                            placeholder="Name"><br><br>
                        <input type="email" id="editEmail" value="<%= user != null ? user.getEmailId() : "" %>"
                            placeholder="Email"><br><br>
                        <input type="text" id="editMobile" value="<%= user != null ? user.getMobile() : "" %>"
                            placeholder="Mobile"><br><br>

                        <button id="submitBtn">Submit</button>
                        <button id="cancelBtn">Cancel</button>
                    </div>

                    <br>
                    <button onclick="logoutP()">Logout</button>
                    <br>
                    <span id="msg"></span>
                    <br>
                    <button id="refreshTokenBtn">Refresh Token</button>
                </div>

                <script src="/jquery-3.7.1.min.js"></script>
                <script src="callback.js"></script>

            </body>

            </html>  





















<%-- 
            <%@ page contentType="text/html;charset=UTF-8" language="java" %>
                <%@ page import="in.nic.model.UserDetails" %>
                    <%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
                        <% // 1. Get user object from session UserDetails user=(UserDetails)
                            session.getAttribute("user_details"); // 2. Convert UserDetails to formatted JSON string
                            using Jackson String userJson="{}" ; if (user !=null) { try { ObjectMapper mapper=new
                            ObjectMapper(); userJson=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user); }
                            catch (Exception e) { userJson="{\" error\": \"" + e.getMessage() + "\" }"; } } // 3. Get
                            the COMPLETE raw JSON from Parichay (ALL fields) String rawParichayJson=(String)
                            session.getAttribute("raw_parichay_json"); // 4. Pretty-print it for display ObjectMapper
                            rawMapper=new ObjectMapper(); Object jsonObj=rawMapper.readValue(rawParichayJson,
                            Object.class);
                            prettyRawJson=rawMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj); %>
                            <!DOCTYPE html>
                            <html lang="en">

                            <head>
                                <meta charset="UTF-8">
                                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                <title>User Profile | Parichay OAuth</title>
                                <link rel="stylesheet" href="style.css">
                                <style>
                                    /* JSON Response Box Styling */
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
                                        color: #38bdf8;
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
                                        <div class="avatar-badge">🛡️</div>
                                        <h2>Welcome, <%= (user !=null && user.getFirstName() !=null ?
                                                user.getFirstName() : "User" ) %> 👋</h2>
                                        <p class="subtitle">Authenticated via Jan-Parichay OAuth 2.0 PKCE</p>
                                    </div>

                                    <div id="viewSection">
                                        <!-- Individual Profile Fields -->
                                        <div class="profile-grid">
                                            <div class="profile-item">
                                                <span class="label">Parichay ID:</span>
                                                <span id="parichayId">
                                                    <%= user !=null && user.getParichayId() !=null ?
                                                        user.getParichayId() : "—" %>
                                                </span>
                                            </div>
                                            <div class="profile-item">
                                                <span class="label">User ID:</span>
                                                <span id="userId">
                                                    <%= user !=null && user.getUserId() !=null ? user.getUserId() : "—"
                                                        %>
                                                </span>
                                            </div>
                                            <div class="profile-item">
                                                <span class="label">Full Name:</span>
                                                <span id="username">
                                                    <%= user !=null ? ((user.getFirstName() !=null ? user.getFirstName()
                                                        : "" ) + " " + (user.getLastName() !=null ? user.getLastName()
                                                        : "" )).trim() : "—" %>
                                                </span>
                                            </div>
                                            <div class="profile-item">
                                                <span class="label">Date of Birth:</span>
                                                <span id="dob">
                                                    <%= user !=null && user.getDob() !=null ? user.getDob() : "—" %>
                                                </span>
                                            </div>
                                            <div class="profile-item">
                                                <span class="label">Gender:</span>
                                                <span id="gender">
                                                    <%= user !=null && user.getGender() !=null ? user.getGender() : "—"
                                                        %>
                                                </span>
                                            </div>
                                            <div class="profile-item">
                                                <span class="label">Mobile:</span>
                                                <span id="mobile">
                                                    <%= user !=null && user.getMobile() !=null ? user.getMobile() : "—"
                                                        %>
                                                </span>
                                            </div>
                                            <div class="profile-item">
                                                <span class="label">Email:</span>
                                                <span id="email">
                                                    <%= user !=null && user.getEmailId() !=null ? user.getEmailId()
                                                        : "—" %>
                                                </span>
                                            </div>
                                        </div>

                                        <!-- WHOLE JSON RESPONSE DISPLAY -->
                                        <div class="json-card">
                                            <div class="json-header">
                                                <span>Parichay User Details (JSON)</span>
                                                <button type="button" class="json-copy-btn"
                                                    onclick="copyJson('oauthJson')">Copy JSON</button>
                                            </div>
                                            <pre class="json-content"><code id="oauthJson"><%= userJson %></code></pre>
                                        </div>

                                        <div id="msg"
                                            style="display: block; text-align: center; margin: 12px 0; font-weight: 600; font-size: 0.9rem; color: var(--primary);">
                                        </div>

                                        <div class="button-stack" style="margin-top: 20px;">
                                            <button id="refreshTokenBtn" class="btn btn-secondary">
                                                Refresh Token
                                            </button>
                                            <button onclick="logoutP()" class="btn btn-danger">
                                                Logout
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
                                <script src="callback.js"></script>
                                <script>
                                    function copyJson(elementId) {
                                        const text = document.getElementById(elementId).innerText;
                                        navigator.clipboard.writeText(text).then(() => {
                                            alert("JSON copied to clipboard!");
                                        });
                                    }
                                </script>
                            </body>

                            </html> --%>