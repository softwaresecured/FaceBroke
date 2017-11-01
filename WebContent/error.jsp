<%@ include file="header.jsp"%>

  <h2>Error</h2>

  <c:out value="${serverMessage}"/>
  <c:out value="${requestScope['javax.servlet.error.message']}"/>

  <%@ include file="footer.jsp"%>