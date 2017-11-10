<%@ include file="header.jsp"%>

<h2>User Results</h2>
<br>

<div class="row">
      <c:forEach items="${user_rows}" var="u">
      	  
          	<div class="col-lg-3 col-md-4 col-sm-6">
	          <a href="wall?user_id=${u.id}">
		          <div class="panel panel-default">
		          	<div class="panel-body">
			          	<h4><img src="image?id=${u.profilePicture.id}" alt="User profile picture" class="img-rounded profile-header-img">  ${u.fname} ${u.lname}</h4>
			          	<h4><small>${u.username}</small></h4>
		          	</div>
		          </div>
	          </a>
	        </div>
	        
      </c:forEach>
   </div>     
  <%@ include file="footer.jsp"%>