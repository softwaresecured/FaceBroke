<%@ include file="header.jsp"%>

<h3>HOMEPAGE</h3>

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

<%@ include file="footer.jsp"%>
