<%@ include file="header.jsp"%>
  <%@ taglib uri="http://www.owasp.org/index.php/Category:OWASP_CSRFGuard_Project/Owasp.CsrfGuard.tld" prefix="csrf" %>

    <div class="row reg-block">
      <div class="col-md-8 text-center">
        <h2>Login Above or Register</h2>
      </div>
      <div class="col-md-4">
        <csrf:form action="register" method="post">
          <div class="form-group">
            <label for="regUsername">Username</label>
            <input type="text" class="form-control" id="regUsername" name="regUsername" placeholder="Username" value="${regUsername}"></div>
            <div class="form-group">
              <label for="regEmail">Email address</label>
              <input type="email" class="form-control" id="regEmail" name="regEmail" placeholder="Email" value="${regEmail}"></div>
              <div class="form-group">
                <label for="regFirstName">First Name</label>
                <input type="text" class="form-control" id="regFirstName" name="regFirstName" placeholder="First Name" value="${regFirstName}"></div>
                <div class="form-group">
                  <label for="regLastName">Last Name</label>
                  <input type="text" class="form-control" id="regLastName" name="regLastName" placeholder="Last Name" value="${regLastName}"></div>
                  <div class="form-group">
                    <label for="regDOB">Date of Birth</label>
                    <input type="date" class="form-control" id="regDOB" name="regDOB" value="${regDOB}"></div>
                    <div class="form-group">
                      <label for="regPassword">Password</label>
                      <input type="password" class="form-control" id="regPassword" name="regPassword" placeholder="Password" autocomplete="off">
                        <input type="password" class="form-control" id="regPasswordConfirm" name="regPasswordConfirm" placeholder="Confirm Password" autocomplete="off"></div>
                        <button type="submit" class="btn btn-default btn-primary">Submit</button>
                      </csrf:form>
                    </div>
                  </div>

                  <div class="container">
                    <div class="row">
                      <footer class="footer">
                        <p class="text-muted">&copy; 2017 Fake Company, Inc.</p>
                      </footer>
                    </div>
                  </div>

                  <%@ include file="footer.jsp"%>