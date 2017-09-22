<%@ include file="header.jsp"%>

<%
	String isValidSession = (String)session.getAttribute("valid");

	if(!JspSnippets.isValidSession(session) ){
		session.setAttribute("valid", "true");
%>
<%@ include file="register.jsp"%>
<%
	}else{
%>
<%@ include file="home.jsp"%>
<%
	}
%>

<%@ include file="footer.jsp"%>
