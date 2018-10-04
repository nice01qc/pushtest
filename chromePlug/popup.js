var myroom = document.getElementById("myroom");
var mybutton = document.getElementById("mybutton");
var myshowroom = document.getElementById("showroom");

var myshowid = document.getElementById("showid");
var myserverid = document.getElementById("myserverid");
var myserver = document.getElementById("myserver");



var localRootNum = localStorage.getItem("room");
var localServerId = localStorage.getItem("ip");
if (localRootNum) {
  myshowroom.innerHTML = localRootNum;
  myroom.value=localRootNum;

  chrome.runtime.sendMessage({
    directive: 'room',
    roomNum: localRootNum
  }, null);
}

if (localServerId) {
  myshowid.innerHTML = localServerId;
  myserver.value = localServerId;

  chrome.runtime.sendMessage({
    directive: 'ip',
    serverIp: localServerId
  }, null);
}


mybutton.onclick=function(){
  if (myroom.value == "" || myroom.value == null || myroom.value == undefined || !myroom.value.match(/^[a-zA-Z0-9]+$/g)){
    alert("房间号格式不正确，只能由数字和字母组成！");
    return;
  }
  myshowroom.innerHTML=myroom.value;

  chrome.runtime.sendMessage({
    directive: 'room',
    roomNum: myroom.value
  }, null);

  localStorage.removeItem("room");
  localStorage.setItem("room",myroom.value);
  myroom.value = myroom.value;
};

myserverid.onclick=function(){
  if (myserver.value == "" || myserver.value == null || myserver.value == undefined){
    alert("server ip is not right!");
    return;
  }
  myshowid.innerHTML=myserver.value;

  chrome.runtime.sendMessage({
    directive: 'ip',
    serverIp: myserver.value
  }, null);

  localStorage.removeItem("ip");
  localStorage.setItem("ip",myserver.value);
  myserver.value = myserver.value;
};

