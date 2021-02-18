'use strict';

//inputs
const itemName = document.querySelector("#itemName")
const itemNameUpdate = document.getElementById("itemNameUpdate")
const itemDone = document.querySelector("#itemDone")
const itemDoneUpdate = document.getElementById("itemDoneUpdate")

//msg/prompts
const toDisplayCreateItem = document.querySelector("#createItemDiv")
const toDisplayReadItem = document.querySelector("#displayDivReadItem")
const toDisplayDeleteItem = document.querySelector("#displayDivDeleteItem")

//forms
const updateItemFormDiv = document.getElementById("updateItemFormDiv")
const updateItemForm = document.getElementById("updateItemForm");
const addTaskToList = document.getElementById("addTaskToListForm");
const updateItemBtn = document.getElementById("updateItemBtn");

var itemId = document.getElementById("itemId");

const addEditDeleteItem = (data, location) => {
    //add delete
    let del = document.createElement("button");
    del.setAttribute("class", "btn");
    del.setAttribute("onclick", `deleteItem(${data.id})`);
    let delImg = document.createElement("img");
    delImg.src = "./img/trash.svg";
    del.appendChild(delImg);
    del.setAttribute("title", "Delete this task")

    let edit = document.createElement("button");
    edit.setAttribute("class", "btn ");
    edit.setAttribute("onclick", `showAndSet(${data.id},updateItemFormDiv)`);
    listNameUpdate.setAttribute("placeholder", `${data.name}`)
    itemId.value = data.id;
    updateItemBtn.setAttribute("onclick", `updateItem(${itemId.value})`);
    let editImg = document.createElement("img");
    editImg.src = "./img/pencil.svg";
    edit.appendChild(editImg);
    edit.setAttribute("title", "Edit this task")

    location.append(edit);
    location.appendChild(del);
}
const showAndSet = (id, form) => {
    onlyShow(form);
    itemId.value = id;
}


const printToScreenItem = (record, display) => {
    addEditDeleteItem(record, display);
    let actualText = document.createTextNode(`${record.name}`);
    display.append(actualText);
    let italic = document.createElement("i");
    console.log(record.done);
    if (record.done == "true") {
        let msg = document.createTextNode("     (done!)")
        italic.appendChild(msg);
        display.append(italic);
    } else {
        let msg = document.createTextNode("     (not done.)")
        italic.appendChild(msg);
        display.append(italic);
    }

}

const printAllToScreenItem = (set, display) => {
    for (let record of set) {
        display.appendChild(document.createElement("br"));
        printToScreenItem(record, display);
    }
    // display.appendChild(document.createElement("hr"));
}

const toBool = (yesOrNo) => {
    if (yesOrNo == "yes") {
        return true;
    } else if (yesOrNo == "no") {
        return false;
    }
}
const createItem = () => {
    let isDone = Boolean;
    if (itemDone.value == "yes") {
        isDone = true;
    } else if (itemDone.value == "no") {
        isDone = false;
    } else {
        alert("Input invalid. Please type yes or no.")
        return false;
    }
    let formData = {
        name: itemName.value,
        done: isDone,
        tdList: { id: listId.value }
    }
    fetch("http://localhost:9092/item/create", {
        method: 'post',
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then(res => res.json())
        .then(data => {
            console.log(`Request succeeded with JSON response ${data}`)
            printToScreenItem(data, toDisplayCreateItem);
            readById(listId.value);
            updateSidebar();
        })
        .catch((err) => console.log(err))
}


const readItemById = (id) => {
    fetch(`http://localhost:9092/item/read/${id}`)
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    onlyShow(toDisplayRead);
                    printToScreenItem(data, toDisplayRead);
                }).catch(err => console.log(err))
        })
}

const readAllItems = () => {
    toDisplayReadItem.innerHTML = "";
    fetch("http://localhost:9092/item/read")
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    printAllToScreen(data, toDisplayReadItem);
                }).catch((err) => console.log(err))
        })
}

const updateItem = (id) => {
    let isDone = Boolean;
    if (itemDoneUpdate.value == "yes") {
        isDone = true;
    } else if (itemDoneUpdate.value == "no") {
        isDone = false;
    } else {
        alert("Input invalid. Please type yes or no.")
        return false;
    }
    let formData = {
        name: itemNameUpdate.value,
        done: isDone
    }
    fetch(`http://localhost:9092/item/update/${id}`, {
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
            onlyShow(updateItemFormDiv);
            readById(listId.value);
        })
        .catch((err) => console.log(err))
}

const deleteItem = (id) => {
    fetch(`http://localhost:9092/item/delete/${id}`, {
        method: 'delete'
    })
        .then(res => JSON.stringify(res))
        .then(data => {
            console.log(`Item deleted ${data}`);
            readById(listId.value);
        })
        .catch(err => console.log(err));
}

