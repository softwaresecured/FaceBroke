<%@ include file="header.jsp"%>

<%@ page import="java.util.List, facebroke.model.User, facebroke.model.Post"%>

<%
	List<Post> rows = (List<Post>)request.getAttribute("posts");
	User wallOwner = (User)request.getAttribute("wall_owner");
%>

<h3><%= wallOwner.getFname() + " " + wallOwner.getLname()%></h3>

<%@ include file="footer.jsp"%>