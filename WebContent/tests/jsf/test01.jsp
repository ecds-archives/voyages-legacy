<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
</head>
<body>

<%

Class.forName("org.postgresql.Driver");

String url = "jdbc:postgresql://wilson.library.emory.edu/tasdb";
Properties props = new Properties();
props.setProperty("user", "tasuser");
props.setProperty("password", "tasuser");
Connection conn = DriverManager.getConnection(url, props);

Statement st = conn.createStatement();
st.execute("SELECT ship_name FROM voyages");
ResultSet rows = st.getResultSet();

while (rows.next())
{
	String shipname = rows.getString("ship_name");
	if (shipname != null)
	{
		out.write(shipname);
		out.write(": ");
		out.write(String.valueOf(shipname.length()));
		out.write("<br>");
	}
}

conn.close();

%>

</body>
</html>