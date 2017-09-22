<% String err = (String)request.getAttribute("errorMessage");
	if( err!= null && !err.isEmpty()){
		out.print("<div class=\"row\"><div class=\"col-md-4 col-md-offset-4 alert alert-warning alert-dismissible fade in alert-message\" role=\"alert\"><button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">×</span></button>" + err + "</div></div>");
	}
	request.setAttribute("errorMessage", "");
%>

<div class="row">
	<div class="col-md-4 text-center">
		<h3>Login Above or Register</h3>
	</div>
	<div class="col-md-4 text-center">
		<h3>Login Above or Register</h3>
	</div>
	<div class="col-md-4 text-center">
		<h3>Login Above or Register</h3>
	</div>
</div>

<footer class="footer">
	<p class="text-muted">&copy; 2017 Fake Company, Inc.</p>
</footer>