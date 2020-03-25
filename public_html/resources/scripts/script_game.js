window.onload = function () {
    var id;
    var name;
    var src;
    document.body.onclick = function (e) {
        var el = e ? e.target : event.srcElement;
        var cls = el.className;

        if (cls === "addcharacter") {
            document.getElementById('id01').style.display = 'block';
            id = e.target.name;
            name = document.getElementById(id + "name").innerText;
            document.getElementsByName('characterName')[0].value = name;
            // name = document.getElementsByName('characterName')[0].value;
            // document.getElementById(id + "name").innerText = name;
        }

        if (el.tagName === "IMG") {
            src = el.src
            document.getElementById('characterImg').src = src;
            // name = document.getElementsByName('characterName')[0].value;
            // document.getElementById(id + 'name').innerText = name;
        }

        if (cls === "cancel") {
            name = document.getElementsByName('characterName')[0].value;
            document.getElementById(id + 'name').innerText = name;
            document.getElementById(id).src = src;
            document.getElementById('id01').style.display = 'none';
        }

        if (cls === "onCheck") {
            var checked = document.querySelector('input[name="raz"]:checked');
            if (checked.checked) {
                var idCh = 'add'+ checked.parentElement.id;
                // console.log("idCh= " + idCh);
                var srcIm = document.getElementById(idCh).src;
                var chName = document.getElementById(idCh + "name").innerText;
                document.getElementById('mySelCharactImg').src = srcIm;
                document.getElementById('mySelCharactName').innerText = chName;
            }
        }
    };
};

////////////////////////////////////////////////////////////////2

function move(e) {
    // Кросс-браузерное получение объекта событие:
    e = e || window.event;

    // Получаем keyCode:
    var keyCode = e.keyCode;
    // console.log(keyCode);

    if (keyCode === 13) {
        sendMessage();
    }
    // let borders = document.querySelector('#square');
    // console.log("borders= " ,  borders.getBoundingClientRect().left);

    // Берем элемент:
    // let borders = document.getElementById("square");
    let mainCharacter = document.getElementById("PerSonag");
    let borders = document.querySelector('#square').getBoundingClientRect();
    let mainCharacter2 = document.querySelector('#PerSonag').getBoundingClientRect();
    // Определяем нажатие клавиш "влево", "вверх"
    // "вправо" и "вниз", коды клавиш 37, 38, 39, 40:
    if ((keyCode === 37) && (borders.left <= mainCharacter2.left)) {
        mainCharacter.style.left = parseInt(mainCharacter.style.left || 0) - 2;
    } else if ((keyCode === 38 && (borders.top <= mainCharacter2.top))) {
        mainCharacter.style.top = parseInt(mainCharacter.style.top || 0) - 2;
    } else if ((keyCode === 39 && (borders.right >= mainCharacter2.right))) {
        // console.log("право");
        mainCharacter.style.left = parseInt(mainCharacter.style.left || 0) + 2;
    } else if ((keyCode === 40 && (borders.bottom >= mainCharacter2.bottom))) {
        mainCharacter.style.top = parseInt(mainCharacter.style.top || 0) + 2;
    } else {
        // Если нажаты другие клавиши: выходим
        return;
    }
    // Отменяем действие по умолчанию:
    e.preventDefault(); // Gecko, ...
    e.returnValue = false; // IE
}

// Устанавливаем обработчик на весь документ:
document.onkeydown = move;
///////////////////////////////////////////////////////////////////////1 chat

var ws;

function init() {
    ws = new WebSocket("ws://localhost:8080/game");
    ws.onopen = function (event) {
    };
    ws.onmessage = function (event) {
        var $textarea = document.getElementById("messages");
        $textarea.value = $textarea.value + event.data + "\n";
    };
    ws.onclose = function (event) {
    }
}

function sendMessage() {
    var messageField = document.getElementById("message");
    var userNameField = document.getElementById("username");
    var message = userNameField.value + ": " + messageField.value;
    if (messageField !== " ") {
        ws.send(message);
    }
    messageField.value = '';
}
