<%@ include file="header.jsp"%>

<h2>Error:</h2>

<% String error = (String)request.getAttribute("serverMessage");
	if( error!= null && !error.isEmpty()){
		out.print("<h4>" + error + "</h4>");
	}
	request.setAttribute("serverMessage", "");
%>

<%@ include file="footer.jsp"%>