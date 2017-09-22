<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ page import="facebroke.util.JspSnippets"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="resources/css/facebroke.css">
<title>FaceBroke</title>
</head>
<body>

	<nav class="navbar navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href=<%= request.getContextPath() %>>FaceBroke</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
		<% if(!JspSnippets.isValidSession(session)){
		%>
			<form class="navbar-form navbar-right">
				<div class="form-group">
					<input type="text" placeholder="Email or Username"
						class="form-control">
				</div>
				<div class="form-group">
					<input type="password" placeholder="Password" class="form-control">
				</div>
				<button type="submit" class="btn btn-success">Log in</button>
			</form>
		<% }else{%>
			<div class="navbar-right dropdown">
				<a href="#" class="dropdown-toggle user-dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
				<%  String name = (String)session.getAttribute("user_fname");
					if(name!=null){
						out.print(name);
					}else{
						out.print("INVALID NAME");
					}
				%>
				<span class="caret"></span></a>
                <ul class="dropdown-menu">
                  <li><a href="#">Action</a></li>
                  <li><a href="#">Another action</a></li>
                  <li><a href="#">Something else here</a></li>
                </ul>
			</div>
		<% } %>
		</div>
		<!--/.navbar-collapse -->
	</div>
	</nav>

	<div class="container">