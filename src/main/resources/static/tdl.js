'use strict';

//inputs
const listName = document.querySelector("#name")
const listNameUpdate = document.querySelector("#nameUpdate")
const idRead = document.querySelector("#listIdRead")
const idDelete = document.querySelector("#listIdDelete")
const updateBtn = document.getElementById("updateBtn");
var idCreateItem;

//dynamic messages/prompts
const toDisplayCreate = document.querySelector("#createItemDiv")
const toDisplayRead = document.querySelector("#displayDivRead")
const toDisplayUpdate = document.querySelector("#displayDivUpdate")
const toDisplayDelete = document.querySelector("#displayDivDelete")

//forms
const createItemDiv = document.getElementById("createItemDiv")
const createFormDiv = document.querySelector("#createFormDiv")
const updateFormDiv = document.querySelector("#updateFormDiv")
const createForm = document.getElementById("createForm")
const updateForm = document.getElementById("updateForm")

//outputs
const sideBarList = document.querySelector(".sideBarList")
const cards = document.querySelectorAll(".card");


//stop default
const formBtn = document.querySelector(".formBtn");
const stopDefault = (event) => {
    console.log(event);
    event.preventDefault();
}

formBtn.addEventListener("click", stopDefault);
const dummyDelete = () => {
    console.log("dummy delete");
}

const addEditDelete = (data, location) => {
    let del = document.createElement("button");
    del.setAttribute("class", "btn");
    del.setAttribute("data-toggle", "modal");
    del.setAttribute("data-target", "#deleteConfirm");
    del.setAttribute("onclick", `deleteList(${data.id})`);
    let delImg = document.createElement("img");
    delImg.src = "./img/trash.svg";
    del.appendChild(delImg);
    del.setAttribute("title", "Delete this list")

    let edit = document.createElement("button");
    edit.setAttribute("class", "btn ");
    edit.setAttribute("onclick", `onlyShow(updateFormDiv)`);
    listNameUpdate.setAttribute("placeholder", `${data.name}`)
    updateBtn.setAttribute("onclick", `updateList(${data.id})`);
    let editImg = document.createElement("img");
    editImg.src = "./img/pencil.svg";
    edit.appendChild(editImg);
    edit.setAttribute("title", "Edit this list")

    location.append(edit);
    location.append(del);
}

const printToScreen = (record, display) => {
    addEditDelete(record, display);
    for (let key in record) {
        if (key != "id") {
            const newLine = document.createElement("p");
            let actualText = document.createTextNode(`${key}: ${record[key]}`);
            let btn = createLinkBtn(actualText, record.id);
            newLine.append(btn);
            display.append(newLine);
        }
    }
}

const printAllToScreen = (set, display) => {
    for (let record of set) {
        display.appendChild(document.createElement("hr"));
        printToScreen(record, display);
    }
    display.appendChild(document.createElement("hr"));
}
const createLinkBtn = (text, id) => {
    let btn = document.createElement("button");
    btn.setAttribute("class", "btn btn-link");
    btn.setAttribute("onclick", `readById(${id})`);
    btn.appendChild(text);
    return btn;
}
const addToSidebar = (data) => {
    let text = document.createTextNode(`(${data.id}) : ${data.name}`);
    let btn = createLinkBtn(text, data.id);
    sideBarList.appendChild(btn);
}

const onlyShow = (thisCard) => {

    for (let card of cards) {
        if (card == thisCard) {
            card.style.display = "block";
        } else {
            card.style.display = "none";
        }
    }
}

const show = (object) => {
    object.style.display = "block";
}

const hide = (thisCard) => {
    thisCard.style.display = "none";
}


const createList = () => {
    let formData = {
        name: listName.value,
    }
    fetch("http://localhost:9092/toDoList/create", {
        method: 'post',
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then(res => res.json())
        .then(data => {
            console.log(`Request succeeded with JSON response ${data}`);
            show(toDisplayCreate);
            createItemDiv.style.display = "block";
            toDisplayCreate.innerHTML = `List created. Add task:`;
            listName.disabled = "disabled";
            itemName.disabled = false;
            idCreateItem.value = data.id;
            updateSidebar();
        })
        .catch((err) => console.log(err))
}

const findLatestId = () => {
    fetch("http://localhost:9092/toDoList/latest")
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data.id);
                    savedId(data.id);
                }).catch((err) => console.log(err))
        })
}

const readById = (id) => {
    toDisplayRead.innerHTML = "";
    fetch(`http://localhost:9092/toDoList/read/${id}`)
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    onlyShow(toDisplayRead);
                    printToScreen(data, toDisplayRead);

                    // readAllItemsInList(id);
                }).catch(err => console.log(err))
        })
}

const readAllLists = () => {
    toDisplayRead.innerHTML = "";
    fetch("http://localhost:9092/toDoList/read")
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    onlyShow(toDisplayRead);
                    printAllToScreen(data, toDisplayRead);

                }).catch((err) => console.log(err))
        })
}

const updateSidebar = () => {
    sideBarList.innerHTML = "";
    fetch("http://localhost:9092/toDoList/read")
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    for (let record of data) {
                        addToSidebar(record);
                        sideBarList.appendChild(document.createElement("br"))
                    }
                }).catch((err) => console.log(err))
        })
}

const updateList = (idVal) => {
    toDisplayUpdate.innerHTML = "";
    let formData = {
        name: listNameUpdate.value,
    }
    fetch(`http://localhost:9092/toDoList/update/${idVal}`, {
        method: 'put',
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then(res => res.json())
        .then(data => {
            console.log(`Request succeeded with JSON response ${data}`);
            console.log(data);
            onlyShow(updateFormDiv);
            toDisplayUpdate.innerHTML = "List Updated!";
            nameUpdate.diabled = "disabled";
            updateSidebar();

        })
        .catch((err) => console.log(err))
}

const deleteList = (idVal) => {
    fetch(`http://localhost:9092/toDoList/delete/${idVal}`, {
        method: 'delete'
    })
        .then(res => JSON.stringify(res))
        .then(data => {
            console.log(`List deleted ${data}`);
            onlyShow(toDisplayDelete);
            let msg = document.createTextNode("To Do List deleted permanently.")
            toDisplayDelete.appendChild(msg);
            updateSidebar();

        })
        .catch(err => console.log(err));
}
const readAllItemsInList = (id) => {
    fetch(`http://localhost:9092/item/read/in-list/${id}`)
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    toDisplayRead.append(document.createElement("hr"));
                    toDisplayRead.append(document.createTextNode("Tasks:"));
                    if (data.length == 0) {
                        toDisplayRead.append(document.createElement("hr"));
                        let italic = document.createElement("i");
                        let noTask = document.createTextNode("No tasks");
                        italic.appendChild(noTask);
                        toDisplayRead.append(italic);
                    } else {
                        // printToScreenItem(data, toDisplayRead);
                        console.log(data);
                    }
                }).catch((err) => console.log(err))
        })
}


function openNav() {
    document.getElementById("mySidepanel").style.width = "275px";
    updateSidebar();
}

/* Set the width of the sidebar to 0 (hide it) */
function closeNav() {
    document.getElementById("mySidepanel").style.width = "0";
}

