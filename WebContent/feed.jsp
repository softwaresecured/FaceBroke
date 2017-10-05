<%@ include file="header.jsp"%>

<h3>HOMEPAGE</h3>

<c:set var="wall_context" scope="page" value="index?"/>
<c:set var="postContext" scope="page" value="wall?user_id=${sessionScope.user_id}"/>
<c:set var="new_post_context" scope="page" value="Create Post"/>
<%@ include file="posts_widget.jsp"%>

<%@ include file="footer.jsp"%>
