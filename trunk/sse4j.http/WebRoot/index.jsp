<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>test</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	</head>
	<body>
		<form method="post" action="/sse4j/servlet/Searching">
			<input type="text" name="searching" value="<ws:poiInfo><arg0><id>200</id><key>110000</key></arg0></ws:poiInfo>" />
			<input type="submit" value="search" />
		</form>
	</body>
</html>
