<%@ include file="header.jsp"%>

<%@ page import="java.util.List, facebroke.model.User, facebroke.model.Post"%>

<div class="table-responsive">
	<table class="table">

<%
	List<Post> rows = (List<Post>)request.getAttribute("wall_posts");
	User wallOwner = (User)request.getAttribute("wall_owner");
%>

<h3><%= wallOwner.getFname() + " " + wallOwner.getLname() + " : " + wallOwner.getId()%></h3>

<% if(rows == null || rows.isEmpty()){
	out.print("No Results");
}else {
	for (int i=0; i<rows.size(); i++){
		out.print("<tr>");
		out.print("<td>" + rows.get(i).getCreator().getUsername() + "</td>");
		out.print("<td>" + rows.get(i).getWall().getId() + "</td>");
		out.print("<td>" + rows.get(i).getCreated() + "</td>");
		out.print("<td>" + rows.get(i).getTitle() + "</td>");
		out.print("<td>" + rows.get(i).getContent() + "</td>");
		out.print("</tr>\n");
	}
}
	%>

	</table>
</div>

<%@ include file="footer.jsp"%>