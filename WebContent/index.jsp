<%@ include file="header.jsp" %>

<h1>FaceBroke</h1>

<div></div>
<form action="Dummy" method="post">
	User ID:
	<input type="text" name="userid"></br></br>
	<input type="radio" name="injection" value="allow">Allow SQL Injection
	<input type="radio" name="injection" value="prevent" checked="checked">Prevent SQL Injection</br></br>
	<input type="submit" value="submit">
</form>
</div>

<%@ include file="footer.jsp" %>