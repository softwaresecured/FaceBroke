<%@ page import="facebroke.util.ValidationSnipets"%>

<%
	String isValidSession = (String)session.getAttribute("valid");

	if(!ValidationSnipets.isValidSession(session) ){
%>
<%@ include file="register.jsp"%>
<%
	}else{
%>
<%@ include file="feed.jsp"%>
<%
	}
%>