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

<div class="row">
	<nav aria-label="..." class="col-md-8 col-md-offset-2">
	  <ul class="pager">
	    <li class="previous disabled"><a href="#"><span aria-hidden="true">&larr;</span> Newer</a></li>
	    <li class="next"><a href="#">Older <span aria-hidden="true">&rarr;</span></a></li>
	  </ul>
	</nav>
</div>