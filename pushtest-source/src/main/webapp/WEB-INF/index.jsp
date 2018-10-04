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
	<title>message</title>
	<meta http-equiv="content-type" content="text/html;charset=UTF-8">
	<link href="css/webSock.css" rel="stylesheet" type="text/css"/>
	<link rel="shortcut icon" href="img/index.ico">
	<script src="js/para.js" ></script>
</head>
<body>
<div id="showbigimg"></div>
<header>
        <span>
            提交答案：<input type="text=" id="headinput" value="">
            <button id="headtijiao">提交</button>
        </span>
	<span>输入房间号：</span><input id="room" type="text"/>
	<button onclick="sendRoom()">确定</button>
	<button onclick="closeWebSocket()">关闭连接</button>
	<span id="imgNum"></span><span id="TextNum"></span>
	<span style="display: inline-block;border-radius: 30px;margin-left: 50px;" id="showshow">111</span>
</header>


<div id="content">
    <h5>message</h5>
    <h5>img</h5>
    <h1 id="progressBar"></h1>
	<div id="textArea">
		<div id="message" style="overflow:scroll; width:300px; height: 1000px;">
			<div id="firstdiv">以下消息双击可以删除,图片也可以</div>
		</div>
	</div>
	<div id="imgArea" style="overflow:scroll; width:705px; height: 1000px;"></div>
</div>

<script src="js/webSock.js"></script>

</body>

</html>