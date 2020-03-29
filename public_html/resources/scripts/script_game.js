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
    console.log("mainCharacter2: ", mainCharacter2.left, " : ",  mainCharacter2.top);

    function setСoordinates(left, top) {
        let data = {};
        data = {"inform": "coordinates", "left": left, "top": top};
        console.log(data);
        $.ajax
        ({
            type: "POST",
            data: data,
            url: '/game',
            success: function (data) {
                // location.replace('/game');
            }
        });
    }

    setСoordinates(mainCharacter2.left, mainCharacter2.top);
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

let ws;

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
    let messageField = document.getElementById("message");
    var userNameField = document.getElementById("username");
    var message = userNameField.value + ": " + messageField.value;
    if (messageField.trim() !== '' ) {
        ws.send(message);
    }
    messageField.value = '';
}
