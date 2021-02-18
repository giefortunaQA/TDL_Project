'use strict';

//inputs
const itemName = document.querySelector("#itemName")
const itemNameUpdate = document.getElementById("itemNameUpdate")
const itemDone = document.querySelector(".radioResult")

//msg/prompts
const toDisplayCreateItem = document.querySelector("#createItemDiv")
const toDisplayReadItem = document.querySelector("#displayDivReadItem")
const toDisplayDeleteItem = document.querySelector("#displayDivDeleteItem")

//forms
const updateItemFormDiv = document.getElementById("updateItemFormDiv")
const updateItemForm = document.getElementById("updateItemForm");
const addTaskToList = document.getElementById("addTaskToListForm");
const updateItemBtn=document.getElementById("updateItemBtn");

var itemId=document.getElementById("itemId");

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
    edit.setAttribute("onclick", `onlyShow(updateItemFormDiv)`);
    listNameUpdate.setAttribute("placeholder", `${data.name}`)
    updateItemBtn.setAttribute("onclick",`setAndUpdate(${data.id})`)
    let editImg = document.createElement("img");
    editImg.src = "./img/pencil.svg";
    edit.appendChild(editImg);
    edit.setAttribute("title", "Edit this item")

    location.append(edit);
    location.appendChild(del);
}

const setAndUpdate=(id)=>{
    itemId.value=id;
    updateItemName(itemId);
}

const addCheckBox = (data, location) => {
    let box = document.createElement("input");
    box.type = "checkbox";
    box.setAttribute("class", "form-check form-check-input");
    box.id = "flexCheckDefault";
    box.setAttribute("onclick", `updateItemStatus(${data.id},${!data.done})`);
    location.append(box)
    return box;
}



const printToScreenItem = (record, display) => {
    for (let key in record) {
        if (key == "name") {
            const newLine = document.createElement("p");
            let actualText = document.createTextNode(`${record[key]}`);
            newLine.append(actualText);
            addEditDeleteItem(record, display);
            addCheckBox(record, display);
            display.append(newLine);
        }
    }
}

const printAllToScreenItem = (set, display) => {
    for (let record of set) {
        display.appendChild(document.createElement("hr"));
        printToScreenItem(record, display);
    }
    display.appendChild(document.createElement("hr"));
}

const createItem = () => {

    let formData = {
        name: itemName.value,
        done: false,
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
            hide(toDisplayCreate);
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

const updateItemName = (id) => {
    toDisplayUpdateItem.innerHTML = "";
    let formData = {
        name: itemNameUpdate.value,
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
const updateItemStatus = (id, status) => {
    let formData = {
        done: status
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
            let box = addCheckBox(data, toDisplayRead);
            box.checked = !data.done;

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
            
        })
        .catch(err => console.log(err));
}

