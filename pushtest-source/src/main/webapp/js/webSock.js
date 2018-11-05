var websocket = null;
var windowroom = "";
var imgNum = 1;

//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket(window.sockSite + "indexSocket");
} else {
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
    var message = JSON.parse(event.data);

    if (message.direction == "IMG") {
        setImg(message.img);
        updateImgNum();
    } else if (message.direction == "IMGNUM") {
        document.getElementById("imgNum").innerHTML = message.imgNum;
        document.getElementById("imgNum").style.background = getRandomColor();
    } else if (message.direction == "TEXTNUM") {
        document.getElementById("TextNum").innerHTML = message.textNum;
        document.getElementById("TextNum").style.background = getRandomColor();
    } else if (message.direction == "IMGSTATENO") {
        var roomStateNum = message.imgSerialNum;
        changeImgStateNO(roomStateNum);
        progressBar();
    } else if (message.direction == "IMGSTATEYES") {
        var roomStateNum = message.imgSerialNum;
        changeImgStateYES(roomStateNum);
        progressBar();
    } else if (message.direction == "ALLIMAGESTATE") {
        initialImgStates(message.allImageState);
    } else if (message.direction == "COMMAND") {
        if (message.command == "deleteAll") deleteAll();
    } else if (message.direction == "TEXT") {
        setMessageInnerHTML(message.text);
        updateTextNum();
    } else if (message.direction == "ANSWER") {
        setAnswer(message.answers.num, message.answers.result);
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
function sendRoom() {
    var message = document.getElementById('room').value;

    if (message.match(/^[0-9]+$/g)) {
        var result = {direction: "ROOM", room: message};
        websocket.send(JSON.stringify(result));
        windowroom = message;
        alert("你的房间号为：" + message + "，可以接收消息了！");
        deleteAll();
    } else {
        alert("房间号码无效!");
    }

}


//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    var message = document.getElementById('message');
    var div = document.createElement("div");
    var a = document.createElement("a");
    a.href = "https:www.baidu.com/s?wd=" + innerHTML;
    a.target = "_Blank";
    a.style.textDecoration = "none";
    var textNode = document.createTextNode(innerHTML);
    a.append(textNode);
    div.append(a);
    div.ondblclick = function () {
        this.parentNode.removeChild(this);
    }
    div.style.border = "2px solid " + getRandomColor();
    message.appendChild(div);
}

// 将图片加入imgdir
function setImg(imgdir) {
    var imgArea = document.getElementById('imgArea');
    var div = document.createElement("div");

    var buttom0 = document.createElement("button");
    buttom0.type = "button";
    buttom0.innerHTML = imgNum;
    buttom0.style.background = "#FAB74F";
    buttom0.setAttribute("class", "buttom0");
    imgNum = imgNum + 1;

    var buttom1 = document.createElement("button");
    buttom1.type = "button";
    buttom1.innerHTML = "查看";
    var buttom2 = document.createElement("button");
    buttom2.type = "button";
    buttom2.innerHTML = "删除";

    var buttom3 = document.createElement("button");
    buttom3.type = "button";
    buttom3.innerHTML = "no";
    buttom3.setAttribute("class", "buttom3");
    buttom3.onclick = function () {
        var result;
        if (this.innerHTML == "no") result = {direction: "IMGSTATENO", imgSerialNum: buttom0.innerHTML};
        if (this.innerHTML == "yes") result = {direction: "IMGSTATEYES", imgSerialNum: buttom0.innerHTML};
        websocket.send(JSON.stringify(result));
    };

    var img = document.createElement("img");
    img.src = imgdir;

    // 添加answer
    var input = document.createElement("input");
    input.type = "text";
    input.setAttribute("class", "answerClass");
    input.style.width = "45px";
    var buttom4 = document.createElement("button");
    buttom4.innerHTML = "提交";
    buttom4.onclick = function () {
        var result = {direction: "ANSWER", answers: {num: buttom0.innerHTML, result: input.value}};
        var status = {direction: "IMGSTATENO", imgSerialNum: buttom0.innerHTML};
        websocket.send(JSON.stringify(result));
        websocket.send(JSON.stringify(status));
        buttom4.style.background = getRandomColor();
        input.style.color = "red";
        input.style.fontWeight = "bold";
    };

    div.appendChild(img);
    div.appendChild(buttom0);
    div.appendChild(buttom1);
    div.appendChild(buttom3);
    div.appendChild(buttom2);
    div.appendChild(input);
    div.appendChild(buttom4);

    buttom2.onclick = function () {
        imgArea.removeChild(div);
        progressBar();
    };

    buttom1.onclick = function () {
        showbigimg(img.src);
        emphasizeClickImg(this);
    }
    img.onclick = function () {
        showbigimg(this.src);
        emphasizeClickImg(buttom1);
    }
    imgArea.appendChild(div);

    progressBar();
}

// 在整个浏览器屏幕简单显示图片
function showbigimg(imgsrc) {
    var showdiv = document.getElementById("showbigimg");
    showdiv.innerHTML = "";
    showdiv.style.opacity = "1";
    showdiv.style.height = "100%";
    showdiv.style.width = "100%";
    var img = document.createElement("img");
    img.src = imgsrc;
    img.style.width = "100%";
    showdiv.appendChild(img);
}

document.getElementById("showbigimg").onclick = function () {
    this.innerHTML = "";
    this.style.opacity = "0";
    this.style.height = "0px";
}

function emphasizeClickImg(buttoms) {
    var imgdivs = document.getElementById("imgArea").getElementsByTagName("div");
    for (var i = imgdivs.length - 1; i >= 0; i--) {
        imgdivs[i].style.background = 'initial';
    }
    buttoms.parentNode.style.background = '#FA6C0C';
}


// 与插件通信用的
// var headinput = document.getElementById("headinput");
// var headtijiao = document.getElementById("headtijiao");
// headtijiao.onclick = function () {
//     if (!windowroom.match(/^[a-zA-Z0-9]+$/g)) {
//         alert("非法房间号");
//         headinput.value = "no 发送";
//     } else {
//         var text = headinput.value;
//         var xhr = new XMLHttpRequest();
//         xhr.open('POST', window.site + 'communication', true);
//         xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
//         xhr.send("result=" + encodeURIComponent(text) + "&room=" + encodeURIComponent(windowroom));
//         headinput.value = "ok,已发送";
//     }
// }

// 转换图片状态
function changeImgStateNO(num) {
    var buttoms = document.getElementsByClassName("buttom0");
    for (var i = buttoms.length - 1; i >= 0; i--) {
        if (buttoms[i].innerHTML == num) {
            buttoms[i].parentNode.style.opacity = '1';
            buttoms[i].parentNode.style.background = 'initial';
            var fourbuttom = buttoms[i].parentNode.getElementsByTagName("button");
            for (var i = fourbuttom.length - 1; i >= 0; i--) {
                if (fourbuttom[i].innerHTML == "no" || fourbuttom[i].innerHTML == "yes") {
                    fourbuttom[i].innerHTML = "no";
                    fourbuttom[i].style.background = 'initial';
                }
            }
        }
    }
}


function changeImgStateYES(num) {
    var buttoms = document.getElementsByClassName("buttom0");
    for (var i = buttoms.length - 1; i >= 0; i--) {
        if (buttoms[i].innerHTML == num) {
            buttoms[i].parentNode.style.opacity = '0.5';
            buttoms[i].parentNode.style.background = 'black';
            var fourbuttom = buttoms[i].parentNode.getElementsByTagName("button");
            for (var i = fourbuttom.length - 1; i >= 0; i--) {
                if (fourbuttom[i].innerHTML == "no" || fourbuttom[i].innerHTML == "yes") {
                    fourbuttom[i].innerHTML = "yes";
                    fourbuttom[i].style.background = 'red';
                }
            }
        }
    }
}

function setAnswer(num, result) {
    var buttoms = document.getElementsByClassName("buttom0");
    for (var i = buttoms.length - 1; i >= 0; i--) {
        if (buttoms[i].innerHTML == num) {
            var inputNode = buttoms[i].parentElement.getElementsByClassName("answerClass")[0];
            inputNode.value = result;
            break;
        }
    }
}


// 初始化图片状态(传入需要更改状态的图片序列号数组)
function initialImgStates(roomnum) {
    for (var i = roomnum.length - 1; i >= 0; i--) {
        changeImgStateYES(roomnum[i]);
    }
    progressBar();
}

// 删除所有插入的信息
function deleteAll() {
    document.getElementById('message').innerHTML = "";
    document.getElementById('imgArea').innerHTML = "";
    document.getElementById("imgNum").innerHTML = 0;
    document.getElementById("TextNum").innerHTML = 0;
    imgNum = 1;
    progressBar();
}

// 颜色随机生成
function getRandomColor() {
    return '#' +
        (function (color) {
            return (color += '0123456789abcdef'[Math.floor(Math.random() * 16)])
            && (color.length == 6) ? color : arguments.callee(color);
        })('');
}

// update time
resetColor();

function resetColor() {
    setInterval('change()', 100);
}

// 显示当前完整日期
function change() {
    var showshow = document.getElementById("showshow");
    var myDate = new Date();
    // var diff = (myDate.getTime() - beginTime);
    // var hour = parseInt(diff/1000/60/60)%60;
    // var munite = parseInt(diff/1000/60)%60;
    // var miao = parseInt(diff/1000)%60;
    // var haomiao = parseInt(diff/100)%100;
    showshow.innerHTML = myDate;
}


// 进度显示
function progressBar() {
    var progress = document.getElementById("progressBar");
    progress.innerHTML = "";
    var allbutoms = document.getElementsByClassName("buttom3");
    var imgnums = allbutoms.length;
    var finishnum = 0;
    for (var i = 0; i < allbutoms.length; i++) {
        if (allbutoms[i].innerHTML == "yes") {
            finishnum++;
        }
    }

    var everyLong = parseInt((900 - imgnums * 2) / imgnums);
    for (var i = 0; i < finishnum; i++) {
        var div = document.createElement("div");
        div.style.display = "inline-block";
        div.style.width = everyLong + "px";
        div.style.height = "18px";
        div.style.background = "#D1CFCF";
        progress.appendChild(div);
    }
}

// update imgNum
function updateImgNum() {
    var allbutoms = document.getElementsByClassName("buttom3");
    document.getElementById("imgNum").innerHTML = allbutoms.length;
    document.getElementById("imgNum").style.background = getRandomColor();
}

// update textNum
function updateTextNum() {
    var message = document.getElementById('message');
    var divs = message.getElementsByTagName('div');
    document.getElementById("TextNum").innerHTML = divs.length;
    document.getElementById("TextNum").style.background = getRandomColor();
}