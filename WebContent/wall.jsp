<%@ include file="header.jsp"%>

<%@ page
	import="java.util.List, facebroke.model.User, facebroke.model.Post, facebroke.model.Comment"%>

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
		
		out.print("<div class=\"row\"><div class=\"col-md-8 col-md-offset-4\">");
		Post p = rows.get(i);
		
		out.print("<div class=\"panel panel-default\">");
		out.print("<div class=\"panel-heading\">");
		out.print("<h4>" + p.getTitle() + "</h4>");
		out.print("<p>" + p.getCreator().getUsername() + " ---> " + p.getWall().getId() + "</p>");
		out.print("</div>");
		out.print("<div class=\"panel-body\">" + p.getContent() + "</div>");
		
		List<Comment> comments = p.getComments();
		
		out.print("<div class=\"panel-footer\">");
		out.print("<ul>");
		for (int j=0; j<comments.size(); j++){
			Comment c = comments.get(j);
			
			out.print("<li>" + c.getCreator().getUsername() + "</br>");
			out.print(c.getContent() + "</li>");
		}
		out.print("</ul>");
		out.print("</div>");
		out.print("</div>");
		
		out.print("</div></div>");
	}
} %>

	</table>
</div>

<%@ include file="footer.jsp"%>