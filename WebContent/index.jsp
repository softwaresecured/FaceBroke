<%@ include file="header.jsp" %>

<%
	String isValidSession = (String)session.getAttribute("valid");

	if(session.isNew() || isValidSession == null || isValidSession == "false" ){
		//session.setAttribute("valid", "true");
%>
<%@ include file="register.jsp" %>
<%
	}else{
%>
<%@ include file="home.jsp" %>
<%
	}
%>

<%@ include file="footer.jsp" %>
