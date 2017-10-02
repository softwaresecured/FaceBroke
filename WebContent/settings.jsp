<%@ include file="header.jsp"%>

<div class="row">
	<div class="col-md-4 col-md-push-8">
	
	<c:if test="${settingsErrors != ''}">
		<c:forEach items="${settingsErrors}" var="e">
			<div class="alert alert-warning alert-dismissible fade in" role="alert">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close">
					<span aria-hidden="true">x</span>
				</button>
				${e}
			</div>
		</c:forEach>
		<c:set var="settingsErrors" scope="session" value=""/>
	</c:if>	
	
	<c:if test="${settingsUpdated != ''}">
		<c:forEach items="${settingsUpdated}" var="u">
			<div class="alert alert-warning alert-dismissible fade in" role="alert">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close">
					<span aria-hidden="true">x</span>
				</button>
				${u}
			</div>
		</c:forEach>
		<c:set var="settingsUpdated" scope="session" value=""/>
	</c:if>	

	</div>
	
	<div class="col-md-4">
		<form action="settings?id=${target_user_id}" method="post">
			<input type="hidden" name="target_id" value="${target_user_id}">
			<div class="form-group">
				<label for="regUsername">Username</label>
				<input type="text" class="form-control" id="regUsername" name="regUsername" value="${target.username}">
			</div>
			<div class="form-group">
				<label for="regEmail">Email address</label>
				<input type="email" class="form-control" id="regEmail" name="regEmail" value="${target.email}">
			</div>
			<div class="form-group">
				<label for="regFirstName">First Name</label>
				<input type="text" class="form-control" id="regFirstName" name="regFirstName" value="${target.fname}">
			</div>
			<div class="form-group">
				<label for="regLastName">Last Name</label>
				<input type="text" class="form-control" id="regLastName" name="regLastName" value="${target.lname}">
			</div>
			<div class="form-group">
				<label for="regDOB">Date of Birth</label>
				<input type="date" class="form-control" id="regDOB" name="regDOB" disabled="true" value="${target.getDOBString()}">
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