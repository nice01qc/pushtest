<%--
  Created by IntelliJ IDEA.
  User: nice01qc
  Date: 2018/4/4
  Time: 21:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>WebSocket</title>
    <link href="css/manage.css" rel="stylesheet" type="text/css"/>
	<link rel="shortcut icon" href="img/manage.ico">
	<script src="js/para.js" ></script>
</head>
<body>


	<div id="part1">

		<div id="allImgNum">allImgNum:0</div><br>
		<div id="allTextNum">allTextNum:0</div><br>
		<div id="myroom">房间号：</div>
		<div id="clientRoom">

		</div>

	</div>

	<div id="part2">
		<button id="clearRedis">清理所有缓存</button><br>
		<button id="clearSock">清理所有Socket</button><br>
		<span>清理指定room Socket:</span>
		<input type="text" name="room" id="clearOneRoom" /> <button id="clearOneRoomOK">确认</button><br>


	</div>

<script src="js/manage.js"></script>
</body>

</html>