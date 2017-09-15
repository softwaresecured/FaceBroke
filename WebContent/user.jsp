<%@ include file="header.jsp" %>
<%@ page import="java.util.List" %>

<% List rows = (List)request.getAttribute("rows");
	for (int i=0; i<rows.size(); i++){
		Object[] arr = (Object[])rows.get(i);
		for(int j=0; j<arr.length; j++){
			out.print(arr[j] + "    ");
		}
		out.print("<br>");
	}%>
	
<%@ include file="footer.jsp" %>