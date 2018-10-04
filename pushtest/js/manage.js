    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket(window.sockSite + "manage");
    }
    else {
        alert('当前浏览器 Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        setMessageInnerHTML("WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        setMessageInnerHTML("WebSocket连接成功");
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        if (event.data.match(/^allImgNum:[0-9]+$/g)) {
            showAllImgNum(event.data);
        }else if(event.data.match(/^allTextNum:[0-9]+$/g)){
            showAllTextNum(event.data);
        }else if(event.data.match(/^room:[0-9a-zA-Z]+$/g)){
            showRoom(event.data);
        }
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        setMessageInnerHTML("WebSocket连接关闭");
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }
    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }

    //发送消息
    function sendMessage(message) {
        websocket.send(message);

    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {

    }

	function showAllImgNum(eventData){
		var allImgNum = document.getElementById("allImgNum");
		allImgNum.innerHTML = eventData;
	}

	function showAllTextNum(eventData){
		var allTextNum = document.getElementById("allTextNum");
		allTextNum.innerHTML = eventData;
	}

	function showRoom(eventData){
		var clientRoom = document.getElementById("clientRoom");
		var span = document.createElement("span");
        var textNode = document.createTextNode(eventData.replace("room:",""));
        span.append(textNode);

        clientRoom.append(span);
	}


	var clearRedis = document.getElementById("clearRedis");
	clearRedis.onclick=function(){
		sendMessage("clear");
		alert("clear all succeed!")
	}

	var clearSock = document.getElementById("clearSock");
	clearSock.onclick=function(){
		sendMessage("0");
		alert("clear succeed!")
	}

	var clearOneRoomOK = document.getElementById("clearOneRoomOK");
	clearOneRoomOK.onclick = function(){
		var clearOneRoom = document.getElementById("clearOneRoom");
		if (clearOneRoom.value.match(/^[a-zA-Z0-9]+$/g)) {
			sendMessage("room:"+clearOneRoom.value);
			alert("clear " + clearOneRoom.value + " succeed!")
		}else{
			alert("输入无效");
		}		
	}