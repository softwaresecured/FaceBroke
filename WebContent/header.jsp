<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="facebroke.util.ValidationSnipets"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="resources/img/favicon.ico" />

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
			<% if(!ValidationSnipets.isValidSession(session)){
		%>
			<form class="navbar-form navbar-right" action="login" method="post">
				<div class="form-group">
					<input type="text" name="user_cred" placeholder="Email or Username"
						class="form-control">
				</div>
				<div class="form-group">
					<input type="password" name="password" placeholder="Password"
						class="form-control">
				</div>
				<button type="submit" class="btn btn-success">Log in</button>
			</form>
			<% }else{%>
			<div class="navbar-right dropdown">
				<a href="#" class="dropdown-toggle user-dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"> <%  String name = (String)session.getAttribute("user_fname");
					if(name!=null){
						out.print(name);
					}else{
						out.print("INVALID NAME");
					}
				%> <span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="wall">Wall</a></li>
					<li><a href="settings">Settings</a></li>
					<li><a href="logout">Logout</a></li>
				</ul>
			</div>
			<% } %>
		</div>
		<!--/.navbar-collapse -->
	</div>
	</nav>

	<div class="container">

		<% String err = (String)request.getAttribute("authMessage");
	if( err!= null && !err.isEmpty()){
		out.print("<div class=\"row\"><div class=\"col-md-4 col-md-offset-4 alert alert-warning alert-dismissible fade in alert-message\" role=\"alert\"><button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">x</span></button>" + err + "</div></div>");
	}
	request.setAttribute("authMessage", "");
%>