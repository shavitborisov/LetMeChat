<%@ page import="myDB.*" %>
<%!
String getFormField(HttpServletRequest req, String param) {
	return (req.getParameter(param) != null ? req.getParameter(param) : "");
}

boolean isSubmitPressed(HttpServletRequest req, String id) {
	return (req.getParameter(id) != null);
}

void AlertAndRedirect(JspWriter out, String msg, String newPage) {
	try {
		String output = "<script type='text/javascript'>alert('" + msg + "');";
		
		if (newPage != null)
			output += "window.location.href = '" + newPage + "';";
		
		output += "</script>";
		out.println(output);
	} catch (Exception e){System.out.print(e.getMessage());}
}

void ClientRedirect(HttpServletResponse response, String newPage) {
	try {
		response.sendRedirect(newPage);
	} catch (Exception e){System.out.print(e.getMessage());}
}

void ServerRedirect(HttpServletRequest request, HttpServletResponse response, String newPage) {
	try {
		RequestDispatcher rd = request.getRequestDispatcher(newPage);
		rd.forward(request, response);
	} catch (Exception e){System.out.print(e.getMessage());}
}

MyDB GetDBConnection(ServletContext app) {
	if (app.getAttribute("DBConnection") == null) {
		try {
			MyDB mydb = new MyDB();
			app.setAttribute("DBConnection", mydb);
		} catch (Exception e){}
	}
	return (MyDB) (app.getAttribute("DBConnection") == null ? null : app.getAttribute("DBConnection"));
}
%>