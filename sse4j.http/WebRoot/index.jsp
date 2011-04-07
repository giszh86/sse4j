<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>test</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	</head>
	<body>
		<form method="post" action="/sse4j/servlet/Searching">
			<input type="text" name="xml" size="100" value="<ws:poiInfo><arg0><id>200</id><key>110000</key></arg0></ws:poiInfo>" />			
			<input type="submit" value="poiInfo" />
		</form>
		<form method="post" action="/sse4j/servlet/Searching">
			<input type="text" name="xml" size="100" value="<ws:search><arg0><count>50</count><distance></distance><geometryWKT></geometryWKT><key>110000</key><keyword>中关村北大街</keyword><preference>POI</preference></arg0></ws:search>" />			
			<input type="submit" value="search" />
		</form>
	</body>
</html>
