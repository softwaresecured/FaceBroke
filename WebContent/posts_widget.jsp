<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="row">
	<div class="col-md-3 text-center">
		<c:if test="${onWall == 1}">
			<img src="image?id=${sessionScope.user_pic_id}" alt="User profile picture" class="img-rounded profile-img">
		</c:if>
	</div>


	<div class="col-md-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				<form action="${postContext}" method="post">
					<div class="form-group">
						<input type="hidden" name="creator_id"
							value="${sessionScope.user_id}"> <input type="hidden"
							name="type" value="TEXT"> <label for="content">${new_post_context}</label>
						<input type="text" class="form-control" id="content"
							name="content">
					</div>
					<div class="text-right">
						<button type="submit" class="btn btn-default btn-primary">Post</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>




<c:forEach items="${posts}" var="p">

	<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<div class="panel panel-default">
				<div class="panel-heading">
					<c:set var="header_content" scope="page" value="" />
					<c:if test="${!p.creator.id.equals(p.wall.user.id)}">
						<c:set var="header_content" scope="page"
							value="   <span class='glyphicon glyphicon-triangle-right name-sep'></span>   <a href='wall?user_id=${p.wall.user.id}'><img src='image?id=${p.wall.user.profilePicture.id}' alt='User profile picture' class='img-rounded profile-img-post'>${p.wall.user.fname} ${p.wall.user.lname}</a>" />
					</c:if>
					<h4>
						<a href="wall?user_id=${p.creator.id}"><img src='image?id=${p.creator.profilePicture.id}' alt='User profile picture' class='img-rounded profile-img-post'>${p.creator.fname}
							${p.creator.lname}</a>${header_content}</h4>
				</div>
				<div class="panel-body">${p.content}</div>

				<div class="panel-footer">
					<ul>
						<c:forEach items="${p.comments}" var="comm">
							<li><a href="wall?user_id=${comm.creator.id}"><img src='image?id=${comm.creator.profilePicture.id}' alt='User profile picture' class='img-rounded profile-img-comment'>${comm.creator.fname}
									${comm.creator.lname}</a><br>${comm.content}</li>
						</c:forEach>
					</ul>
					<form action="comment" method="post">
						<div class="form-group">
							<input type="hidden" name="creator_id"
								value="${sessionScope.user_id}"> <input type="hidden"
								name="post_id" value="${p.id}"> <input type="text"
								class="form-control" name="content"
								placeholder="Add a comment..." />
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</c:forEach>


<c:set var="back_path" scope="page"
	value="${wall_context}start=${param.start - sessionScope.postsPerPage}" />
<c:if test="${(param.start - sessionScope.postsPerPage) < 0}">
	<c:set var="back_path" scope="page" value="${wall_context}start=0" />
	<c:set var="back_disabled" scope="page" value="disabled" />
</c:if>
<c:set var="forward_path" scope="page"
	value="${wall_context}start=${param.start + sessionScope.postsPerPage}" />


<div class="row">
	<nav aria-label="..." class="col-md-8 col-md-offset-2">
		<ul class="pager">
			<li class="previous ${back_disabled}"><a href="${back_path}"
				class="${back_disabled}"><span aria-hidden="true">&larr;</span>
					Newer</a></li>
			<li class="next"><a href="${forward_path}">Older <span
					aria-hidden="true">&rarr;</span></a></li>
			<li><c:out value="" /></li>
		</ul>
	</nav>
</div>