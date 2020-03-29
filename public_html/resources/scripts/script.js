window.onload = function () {
    let personages = jQuery.parseJSON($("#personages").text());
    let name = '';
    let src = '';
    let nameEl;
    let numEl;
    let nameElAdd;
    let nameElEdit;
    let oldNameEd;

    let max = personages.length < 4 ? personages.length : 3;
    for (let j = 0; j < max; j++) {
        let img = personages[j].img;
        let name = personages[j].name;
        document.getElementById('addcharacter' + (j + 1)).src = img;
        document.getElementById('addcharacter' + (j + 1) + 'name').innerText = name;
        document.getElementsByName('addcharacter' + (j + 1))[0].style.display = 'none';
        document.getElementsByName('delete' + (j + 1))[0].style.display = 'block';
        document.getElementsByName('edit' + (j + 1))[0].style.display = 'block';
    }

    document.body.onclick = function (e) {
        let el = e ? e.target : event.srcElement;
        let cls = el.className;
        nameEl = e.target.name;

        if (cls === "addcharacter") {
            nameElAdd = nameEl;
            numEl = parseInt(nameElAdd.match(/\d+/));
            document.getElementById('id01').style.display = 'block';
            document.getElementsByName('characterName')[0].value = "";
            document.getElementById('characterImg').src = "";
        }

        if (el.tagName === "IMG") {
            src = el.src;
            document.getElementById('characterImg').src = src;
            document.getElementById('characterImgEd').src = src;
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
                let srcIm = document.getElementById(idCh).src;
                let chName = document.getElementById(idCh + "name").innerText;
                document.getElementById('mySelCharactImg').src = srcIm;
                document.getElementById('mySelCharactName').innerText = chName;
            }
        }

        if (cls === "savePers") {
            name = document.getElementsByName('characterName')[0].value;
            document.getElementById(nameElAdd + 'name').innerText = name;
            document.getElementById(nameElAdd).src = src;
            if (src.trim() === '' || name.trim() === '') {
                $("#auth-info").html("добавьте имя и изображение");
            } else {
                addPersonage(src, name, numEl);
                document.getElementById('id01').style.display = 'none';
                document.getElementsByName(nameElAdd)[0].style.display = 'none';
                document.getElementsByName('delete' + numEl)[0].style.display = 'block';
                document.getElementsByName('edit' + numEl)[0].style.display = 'block';
                name = '';
                src = '';
            }
        }

        if (cls === "editPers") {
            name = document.getElementById('characterNameEd').value
            document.getElementById("addcharacter" + numEl + 'name').innerText = name;
            document.getElementById("addcharacter" + numEl).src = src;
            if (src.trim() === '' || name.trim() === '') {
                $("#auth-info-edit").html("добавьте имя и изображение");
            } else {
                editPersonage(src, name, oldNameEd);
                document.getElementById('modulEdit').style.display = 'none';
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
        }

        if (cls === "edit") {
            nameElEdit = nameEl;
            numEl = parseInt(nameElEdit.match(/\d+/));
            document.getElementById('modulEdit').style.display = 'block';
            let namePers = document.getElementById("addcharacter" + numEl + 'name').innerText;
            src = document.getElementById("addcharacter" + numEl).src;
            document.getElementById('characterNameEd').value = namePers;
            oldNameEd = namePers;
            document.getElementById('characterImgEd').src = src;
        }

        if (cls === "startGame") {
            let imgMyPers = document.getElementById('mySelCharactImg').src;
            let nameMyPers = document.getElementById('mySelCharactName').innerText;
            if (imgMyPers.trim() === '' || nameMyPers.trim() === '') {
                console.log(imgMyPers, nameMyPers);
                document.getElementById('gamePlay').style.background = 'navy';
            } else {
                selectPersonage(nameMyPers);

            }
        }
    };

    function addPersonage(src, name, numEl) {
        let data = {};
        data = {"inform": "add", "characterName": name, "src": src, "numEl": numEl};
        console.log(data);
        $.ajax
        ({
            type: "POST",
            data: data,
            url: '/personage'
        });
    }

    function editPersonage(src, name, oldNameEd) {
        let data = {};
        data = {"inform": "upd", "personageName": name, "src": src, "oldNameEd": oldNameEd};
        console.log(data);
        $.ajax
        ({
            type: "POST",
            data: data,
            url: '/personage'
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
            url: '/personage'
        });
    }

    function selectPersonage(name) {
        let data = {};
        data = {"inform": "selectPersonage", "personageName": name};
        console.log(data);
        $.ajax
        ({
            type: "POST",
            data: data,
            url: '/personage',
            success: function (data) {
                location.replace('/game');
            }
        });
    }
};