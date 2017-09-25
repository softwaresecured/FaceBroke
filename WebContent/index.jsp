<%@ include file="header.jsp"%>

<%
	String isValidSession = (String)session.getAttribute("valid");

	if(!ValidationSnipets.isValidSession(session) ){
		//session.setAttribute("valid", "true");
		//session.setAttribute("user_id", "1");
		//session.setAttribute("user_fname", "Raj");
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
