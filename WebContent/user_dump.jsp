<%@ include file="header.jsp"%>

<%@ page import="java.util.List, facebroke.model.User"%>

<h2>User Info</h2>

<div class="table-responsive">
	<table class="table">
	
	<c:forEach items="${rows}" var="u">
		<tr>
			<td>${u.fname}</td>
			<td>${u.lname}</td>
			<td>${u.email}</td>
			<td>${u.username}</td>
			<td>${u.b64Pass}</td>
		</tr>	
	</c:forEach>

	</table>
</div>

<%@ include file="footer.jsp"%>
