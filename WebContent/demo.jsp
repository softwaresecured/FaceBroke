<%@ include file="header.jsp"%>
  <%@ taglib uri="http://www.owasp.org/index.php/Category:OWASP_CSRFGuard_Project/Owasp.CsrfGuard.tld" prefix="csrf" %>

    <!-- Example row of columns -->
    <div class="row">
      <div class="col-md-4">
        <h2>Heading</h2>
        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui.</p>
        <p>
          <a class="btn btn-default" href="#" role="button">View details &raquo;</a>
        </p>
      </div>
      <div class="col-md-4">
        <h2>Heading</h2>
        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui.</p>
        <p>
          <a class="btn btn-default" href="#" role="button">View details &raquo;</a>
        </p>
      </div>
      <div class="col-md-4">
        <h2>Heading</h2>
        <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
        <p>
          <a class="btn btn-default" href="#" role="button">View details &raquo;</a>
        </p>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <h2>SQL Injection Demo</h2>
        <csrf:form action="demo" method="post">
          User ID:
          <input type="text" name="userid"></br>
        </br>
        <input type="radio" name="injection" value="allow">Allow SQL Injection
          <input type="radio" name="injection" value="prevent" checked="checked">Prevent SQL Injection
          </br>
        </br>
        <input class="btn btn-default" type="submit" value="submit"></csrf:form>
      </div>
    </div>

    <%@ include file="footer.jsp"%>
