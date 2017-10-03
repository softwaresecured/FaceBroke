<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:forEach items="${posts}" var="p">
	<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4>${p.title}</h4>
					<p>${p.creator.username} ---> ${p.wall.user.username}</p>
				</div>
				<div class="panel-body">${p.content}</div>

				<div class="panel-footer">
					<ul>
						<c:forEach items="${p.comments}" var="comm">
							<li>${comm.creator.username}<br>${comm.content}</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>
</c:forEach>


<c:set var="back_path" scope="page" value="${sessionScope.wall_context}?start=${sessionScope.start - sessionScope.postsPerPage}"/>
<c:if test="${(sessionScope.start - sessionScope.postsPerPage) < 0}">
	<c:set var="back_path" scope="page" value="${sessionScope.wall_context}?start=0"/>
	<c:set var="back_disabled" scope="page" value="disabled"/>
</c:if>
<c:set var="forward_path" scope="page" value="${sessionScope.wall_context}?start=${sessionScope.start + sessionScope.postsPerPage}"/>


<div class="row">
	<nav aria-label="..." class="col-md-8 col-md-offset-2">
	  <ul class="pager">
	    <li class="previous ${back_disabled}"><a href="${back_path}" class="${back_disabled}"><span aria-hidden="true">&larr;</span> Newer</a></li>
	    <li class="next"><a href="${forward_path}">Older <span aria-hidden="true">&rarr;</span></a></li>
	    <li><c:out value=""/></li>
	  </ul>
	</nav>
</div>