<%@ include file="header.jsp"%>

<%@ page import="java.util.List, facebroke.model.User"%>

<%
	User target = (User)session.getAttribute("target");
%>

<div class="row">
	<div class="col-md-4 col-md-offset-4">
		<form action="settings" method="post">
			<input type="hidden" name="target_id" value=<%= request.getAttribute("target_user_id")%>>
			<div class="form-group">
				<label for="regUsername">Username</label>
				<input type="text" class="form-control" id="regUsername" name="regUsername" value="<%= target.getUsername()%>">
			</div>
			<div class="form-group">
				<label for="regEmail">Email address</label>
				<input type="email" class="form-control" id="regEmail" name="regEmail" value="<%= target.getEmail()%>">
			</div>
			<div class="form-group">
				<label for="regFirstName">First Name</label>
				<input type="text" class="form-control" id="regFirstName" name="regFirstName" value="<%= target.getFname()%>">
			</div>
			<div class="form-group">
				<label for="regLastName">Last Name</label>
				<input type="text" class="form-control" id="regLastName" name="regLastName" value="<%= target.getLname()%>">
			</div>
			<div class="form-group">
				<label for="regDOB">Date of Birth</label>
				<input type="date" class="form-control" id="regDOB" name="regDOB" value="<%= target.getDOB()%>" disabled="true">
			</div>
			<div class="form-group">
				<label for="regPassword">Password</label>
				<input type="password" class="form-control" id="regPassword" name="regPassword" placeholder="Password">
				<input type="password" class="form-control" id="regPasswordConfirm" name="regPasswordConfirm" placeholder="Confirm Password">
			</div>
			<button type="submit" class="btn btn-default btn-primary">Save Changes</button>
		</form>
	</div>
</div>
<%@ include file="footer.jsp"%>