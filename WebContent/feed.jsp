<%@ include file="header.jsp"%>

  <div class="row">
    <div class="col-md-3">
      <h3>HOMEPAGE</h3>
    </div>
  </div>

  <c:set var="wall_context" scope="page" value="index?"/>
  <c:set var="postContext" scope="page" value="wall?user_id=${sessionScope.user_id}"/>
  <c:set var="new_post_context" scope="page" value="Create Post"/>
  <%@ include file="posts_widget.jsp"%>

    <%@ include file="footer.jsp"%>