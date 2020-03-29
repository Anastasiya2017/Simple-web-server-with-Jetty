window.onload = function () {
    let personages = jQuery.parseJSON($("#personages").text());
    let name = '';
    let src = '';
    let nameEl;
    let numEl;
    let nameElAdd;
    let nameElEdit;
    let oldNameEd;

    /*personages.forEach(function sumNumber(currentValue) {
            console.log("currentValue= ", currentValue);
            var img = currentValue.img;
            var name = currentValue.name;
            // console.log("img " + i + "= ", img);
            // console.log("name " + i + "= ", name);
            document.getElementById('addcharacter' + i).src = img;
            document.getElementById('addcharacter' + i + 'name').innerText = name;
            i++;
            // $("#addcharacter1").attr(currentValue.img);
        }
    );
*/
    console.log("personages= ", personages);
    let max = personages.length < 4 ? personages.length : 3;
    for (let j = 0; j < max; j++) {
        let img = personages[j].img;
        let name = personages[j].name;
        console.log("img " + j + "= ", img);
        console.log("name " + j + "= ", name);
        document.getElementById('addcharacter' + (j + 1)).src = img;
        document.getElementById('addcharacter' + (j + 1) + 'name').innerText = name;
        document.getElementsByName('addcharacter' + (j + 1))[0].style.display = 'none';
        document.getElementsByName('delete' + (j + 1))[0].style.display = 'block';
        document.getElementsByName('edit' + (j + 1))[0].style.display = 'block';
    }


    // for (let i = 1; i < 4; i++) {
    //     document.getElementById('id01').style.display = 'none';
    // }


    document.body.onclick = function (e) {
        var el = e ? e.target : event.srcElement;
        var cls = el.className;
        nameEl = e.target.name;

        if (cls === "addcharacter") {
            nameElAdd = nameEl;
            numEl = parseInt(nameElAdd.match(/\d+/));
            console.log("kkkkkkkkkkkk", nameElAdd);
            document.getElementById('id01').style.display = 'block';
            // name = document.getElementById(id + "name").innerText;
            document.getElementsByName('characterName')[0].value = "";
            document.getElementById('characterImg').src = "";
            // name = document.getElementsByName('characterName')[0].value;
            // document.getElementById(id + "name").innerText = name;
        }

        if (el.tagName === "IMG") {
            src = el.src;
            document.getElementById('characterImg').src = src;
            document.getElementById('characterImgEd').src = src;
            // name = document.getElementsByName('characterName')[0].value;
            // document.getElementById(id + 'name').innerText = name;
        }

        if (cls === "cancel" || cls === "close") {
            document.getElementById('id01').style.display = 'none';
            name = '';
            src = '';
        }

        if (cls === "onCheck") {
            let checked = document.querySelector('input[name="raz"]:checked');
            if (checked.checked) {
                let idCh = 'add' + checked.parentElement.id;
                // console.log("idCh= " + idCh);
                let srcIm = document.getElementById(idCh).src;
                let chName = document.getElementById(idCh + "name").innerText;
                document.getElementById('mySelCharactImg').src = srcIm;
                document.getElementById('mySelCharactName').innerText = chName;
            }
        }

        if (cls === "savePers") {
            name = document.getElementsByName('characterName')[0].value;
            console.log(nameElAdd + 'name');
            document.getElementById(nameElAdd + 'name').innerText = name;
            document.getElementById(nameElAdd).src = src;
            if (src.trim() === '' || name.trim() === '') {
                $("#auth-info").html("добавьте имя и изображение");
            } else {
                addPersonage(src, name, numEl);
                document.getElementById('id01').style.display = 'none';
                console.log(nameElAdd);
                document.getElementsByName(nameElAdd)[0].style.display = 'none';
                document.getElementsByName('delete' + numEl)[0].style.display = 'block';
                document.getElementsByName('edit' + numEl)[0].style.display = 'block';
                name = '';
                src = '';
            }
        }

        if (cls === "editPers") {
            name = document.getElementById('characterNameEd').value
            console.log(nameElEdit + 'name');
            document.getElementById("addcharacter" + numEl + 'name').innerText = name;
            document.getElementById("addcharacter" + numEl).src = src;
            if (src.trim() === '' || name.trim() === '') {
                $("#auth-info-edit").html("добавьте имя и изображение");
            } else {
                editPersonage(src, name, oldNameEd);
                document.getElementById('modulEdit').style.display = 'none';
                console.log(nameElEdit);
                document.getElementsByName(nameElEdit)[0].style.display = 'none';
                document.getElementsByName('delete' + numEl)[0].style.display = 'block';
                document.getElementsByName('edit' + numEl)[0].style.display = 'block';
                name = '';
                src = '';
            }
        }

        if (cls === "delete") {
            numEl = parseInt(nameEl.match(/\d+/));
            document.getElementsByName('addcharacter' + numEl)[0].style.display = 'block';
            document.getElementsByName('delete' + numEl)[0].style.display = 'none';
            document.getElementsByName('edit' + numEl)[0].style.display = 'none';
            let nameDel = document.getElementById('addcharacter' + numEl + 'name').innerText;
            document.getElementById('addcharacter' + numEl + 'name').innerText = "";
            document.getElementById('addcharacter' + numEl).src = "";
            deletePersonage(nameDel, numEl);
            console.log("eeeeesss");
        }

        if (cls === "edit") {
            nameElEdit = nameEl;
            numEl = parseInt(nameElEdit.match(/\d+/));
            console.log("edit", nameElEdit);
            document.getElementById('modulEdit').style.display = 'block';
            var namePers = document.getElementById("addcharacter" + numEl + 'name').innerText;
            src = document.getElementById("addcharacter" + numEl).src;
            console.log(namePers, "   ", src ," jjjjjjjjjjjj")
            document.getElementById('characterNameEd').value = namePers;
            oldNameEd = namePers;
            document.getElementById('characterImgEd').src = src;
        }
    };

    // $("#saveChar").click(function addPersonage () {
    function addPersonage(src, name, numEl) {
        // if (src.trim() !== '' && name.trim() !== '') {
        let data = {};
        // data = {"characterName": $("#characterName").val(), "src": $("#characterImg").attr('src')};
        data = {"inform": "add", "characterName": name, "src": src, "numEl": numEl};
        console.log(data);
        $.ajax
        ({
            type: "POST",//Метод передачи
            data: data,//Передаваемые данные в JSON - формате
            url: '/personage',//Название сервлета
            success: function (serverData)//Если запрос удачен
            {
                // $("#auth-info").css({"background-color":serverData.backgroundColor, "height": "50px", "color":"white"});
                // $("#auth-info").html(serverData.serverInfo);
                console.log("yes!!")
            },
            error: function (e)//Если запрос не удачен
            {
                // $("#auth-info").css({"background-color":"#CC6666", "height": "50px", "color":"white"});
                // $("#auth-info").html("Запрос не удался!");
                console.log("no!!")
            }
        });
        // }
    }

    function editPersonage(src, name, oldNameEd) {
        let data = {};
        data = {"inform": "upd", "personageName": name, "src": src, "oldNameEd": oldNameEd};
        console.log(data);
        $.ajax
        ({
            type: "POST",
            data: data,
            url: '/personage',
            success: function (serverData) {
                // $("#auth-info").css({"background-color":serverData.backgroundColor, "height": "50px", "color":"white"});
                // $("#auth-info").html(serverData.serverInfo);
                console.log("yes!!")
            },
            error: function (e) {
                // $("#auth-info").css({"background-color":"#CC6666", "height": "50px", "color":"white"});
                // $("#auth-info").html("Запрос не удался!");
                console.log("no!!")
            }
        });
    }

    function deletePersonage(name, numEl) {
        let data = {};
        data = {"inform": "del", "personageName": name, "numEl": numEl};
        console.log(data);
        $.ajax
        ({
            type: "POST",
            data: data,
            url: '/personage',
            success: function (serverData) {
                // $("#auth-info").css({"background-color":serverData.backgroundColor, "height": "50px", "color":"white"});
                // $("#auth-info").html(serverData.serverInfo);
                console.log("yes!!")
            },
            error: function (e) {
                // $("#auth-info").css({"background-color":"#CC6666", "height": "50px", "color":"white"});
                // $("#auth-info").html("Запрос не удался!");
                console.log("no!!")
            }
        });
    }
};