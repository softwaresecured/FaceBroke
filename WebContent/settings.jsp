<%@ include file="header.jsp"%>

<%@ page import="java.util.List, facebroke.model.User"%>

<div class="table-responsive">
	<table class="table">

<%
	User wallOwner = (User)request.getAttribute("wall_owner");
%>

	</table>
</div>

<%@ include file="footer.jsp"%>