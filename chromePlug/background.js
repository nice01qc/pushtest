
var room = "123";
var ip = "118.25.1.128";
// var ip = "127.0.0.1:8080";
var site = "http://" + ip + "/pushtest";

// 监听chrome.runtime.sendMessage 发送过来的消息
chrome.runtime.onMessage.addListener(function (request, sender, callback) {	

    switch (request.directive) {
        case 'room': {
            room = request.roomNum;
            console.log("成功设置房间号：" + room);
            break;
        }
        case 'ip':{
        	ip = request.serverIp;
        	site = "http://" + ip + "/pushtest";
        	console.log("成功设置ip：" + site);
        	break;
        }
        case 'img': {
            sendImg();
            break;
        }
        case 'text': {
            sendtext(request.selText);
            break;
        }
        case 'command':{
        	sendcommand(request.command);
            break;
        }
    }    
});

// 发送图片信息给服务器
function sendImg(){
  chrome.tabs.captureVisibleTab(null, {
      format : "jpeg",
      quality : 80
  }, function(data) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', site+'/img', true);

    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send("img="+encodeURIComponent(data) + "&room=" + encodeURIComponent(room));
    });
}
// 发送text信息给服务器
function sendtext(text){
    var xhr = new XMLHttpRequest();
    xhr.open('POST', site + '/text', true);

    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send("text="+encodeURIComponent(text) + "&room=" + encodeURIComponent(room));
}
// 发送命令信息给服务器
function sendcommand(text){
    var xhr = new XMLHttpRequest();
    xhr.open('POST', site + '/command', true);

    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send("command="+encodeURIComponent(text) + "&room=" + encodeURIComponent(room));
}
