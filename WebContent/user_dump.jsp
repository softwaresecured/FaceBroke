<%@ include file="header.jsp"%>

<%@ page import="java.util.List, facebroke.model.User"%>

<h2>User Info</h2>

<div class="table-responsive">
	<table class="table">

		<%

  List<User> rows = (List<User>)request.getAttribute("rows");
  if(rows == null || rows.isEmpty()){
	out.print("No Results");
  }else {
	for (int i=0; i<rows.size(); i++){
		out.print("<tr>");
		out.print("<td>" + rows.get(i).getFname() + "</td>");
		out.print("<td>" + rows.get(i).getLname() + "</td>");
		out.print("<td>" + rows.get(i).getEmail() + "</td>");
		out.print("<td>" + rows.get(i).getUsername() + "</td>");
		out.print("<td>" + rows.get(i).getB64Pass() + "</td>");
		out.print("</tr>\n");
	}
  }
	%>

	</table>
</div>

<%@ include file="footer.jsp"%>
