<%@ include file="header.jsp"%>

<h3>${wall_owner.fname} ${wall_owner.lname}: ${wall_owner.id}</h3>

<c:set var="wall_context" scope="page" value="wall?user_id=${wall_owner.id}&"/>
<%@ include file="posts_widget.jsp"%>

<%@ include file="footer.jsp"%>