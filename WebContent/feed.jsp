<%@ include file="header.jsp"%>

<h3>HOMEPAGE</h3>

<%@ page
	import="java.util.List, facebroke.model.User, facebroke.model.Post, facebroke.model.Comment"%>

<div class="table-responsive">
	<table class="table">

	<%
		List<Post> rows = (List<Post>)request.getAttribute("wall_posts");
		User wallOwner = (User)request.getAttribute("wall_owner");
	%>

		<% if(rows == null || rows.isEmpty()){
	out.print("No Results");
}else {
	for (int i=0; i<rows.size(); i++){
		Post p = rows.get(i);
		
		out.print("<tr>");
		out.print("<td>" + p.getCreator().getUsername() + "</td>");
		out.print("<td>" + p.getWall().getId() + "</td>");
		out.print("<td>" + p.getCreated() + "</td>");
		out.print("<td>" + p.getTitle() + "</td>");
		out.print("<td>" + p.getContent() + "</td>");
		out.print("</tr>\n");
		
		List<Comment> comments = p.getComments();
		
		for (int j=0; j<comments.size(); j++){
			Comment c = comments.get(j);
			
			out.print("<tr>");
			out.print("<td>" + c.getCreator().getUsername() + "</td>");
			out.print("<td>" + c.getContent() + "</td>");
			out.print("</tr>\n");
		}
	}
}
	%>

	</table>
</div>

<%@ include file="footer.jsp"%>