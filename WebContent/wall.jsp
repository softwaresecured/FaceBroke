<%@ include file="header.jsp"%>

  <div class="row">
    <div class="col-md-3 text-center">
      <h3>${wall_owner.fname} ${wall_owner.lname}</h3>
    </div>
  </div>

  <c:set var="onWall" scope="page" value="${1}"/>
  <c:set var="wall_context" scope="page" value="wall?user_id=${wall_owner.id}&"/>
  <c:set var="postContext" scope="page" value="wall?user_id=${wall_owner.id}"/>
  <c:set var="new_post_context" scope="page" value="Post on ${wall_owner.fname}'s wall"/>

  <%@ include file="posts_widget.jsp"%>

    <%@ include file="footer.jsp"%>