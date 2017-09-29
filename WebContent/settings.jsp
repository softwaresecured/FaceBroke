<%@ include file="header.jsp"%>

<%@ page import="java.util.List, java.util.ArrayList, java.util.Calendar, facebroke.model.User"%>




<%
	User target = (User)session.getAttribute("target");
%>

<div class="row">
	<div class="col-md-4 col-md-push-8">
		<%  ArrayList<String> settingsErrors = (ArrayList<String>)session.getAttribute("settingsErrors");
		if( settingsErrors!= null && !settingsErrors.isEmpty()){
			for(String i : settingsErrors){
				out.print("<div class=\"alert alert-warning alert-dismissible fade in\" role=\"alert\">");
				out.print("<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">");
				out.print("<span aria-hidden=\"true\">x</span>");
				out.print("</button>");
				out.print(i);
				out.print("</div>");
			}
		}
		session.setAttribute("settingsErrors", new ArrayList<String>());
		
		ArrayList<String> settingsUpdated = (ArrayList<String>)session.getAttribute("settingsUpdated");
		if( settingsUpdated!= null && !settingsUpdated.isEmpty()){
			for(String i : settingsUpdated){
				out.print("<div class=\"alert alert-warning alert-dismissible fade in\" role=\"alert\">");
				out.print("<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">");
				out.print("<span aria-hidden=\"true\">x</span>");
				out.print("</button>");
				out.print(i);
				out.print("</div>");
			}
		}
		session.setAttribute("settingsUpdated", new ArrayList<String>());
	%>
	</div>
	<div class="col-md-4">
		<form action="settings?id=<%= request.getAttribute("target_user_id")%>" method="post">
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
				<input type="date" class="form-control" id="regDOB" name="regDOB" disabled="true" value="<%= target.getDOBString()%>">
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