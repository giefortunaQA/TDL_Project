'use strict';

//inputs
const itemName = document.querySelector("#itemName")
const itemNameUpdate = document.getElementById("itemNameUpdate")
const itemDone = document.querySelector(".radioResult")

//msg/prompts
const toDisplayCreateItem = document.querySelector("#addedTasks")
const toDisplayReadItem = document.querySelector("#displayDivReadItem")
const toDisplayUpdateItem = document.querySelector("#displayDivUpdateItem")
const toDisplayDeleteItem = document.querySelector("#displayDivDeleteItem")

//forms
const updateItemFormDiv = document.getElementById("updateItemFormDiv")
const updateItemForm = document.getElementById("updateItemForm");
const addTaskToList = document.getElementById("addTaskToListForm");


const addDelete = (data, location) => {
    //add delete
    let del = document.createElement("button");
    del.setAttribute("class", "btn");
    del.setAttribute("data-toggle", "modal");
    del.setAttribute("data-target", "#deleteConfirm");
    del.setAttribute("onclick", `deleteItem(${data.id})`);
    let delImg = document.createElement("img");
    delImg.src = "./img/trash.svg";
    del.appendChild(delImg);
    del.setAttribute("title", "Delete this list")
    location.appendChild(del);
}

const addCheckBox = (data, location) => {
    let box = document.createElement("input");
    box.type = "checkbox";
    box.setAttribute("class", "form-check form-check-input");
    box.id = "flexCheckDefault";
    box.onclick = `updateItemStatus(${!data.done})`
    location.append(box)
    return box;

}

const printToScreenItem = (record, display) => {
    for (let key in record) {
        if (key == "done") {
            addCheckBox(record, display);
        } else if (key == "name") {
            const newLine = document.createElement("p");
            let actualText = document.createTextNode(`${record[key]}`);
            newLine.append(actualText);
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
        tdList: { id: idCreateItem.value }
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
            toDisplayCreate.style.display = "none";
            hide(toDisplayCreate);
            updateSidebar();
        })
        .catch((err) => console.log(err))
}

const readItemById = () => {
    toDisplayReadItem.innerHTML = "";
    const id = idReadItem.value;
    fetch(`http://localhost:9092/item/read/${id}`)
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    printToScreen(data, toDisplayReadItem);
                    toDisplayReadItem.style.display = "";
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
                    toDisplayReadItem.style.display = "";
                }).catch((err) => console.log(err))
        })
}

const updateItemName = (id) => {
    toDisplayUpdateItem.innerHTML = "";
    let formData = {
        name: itemNameUpdate.value,
    }
    fetch(`http://localhost:9092/toDoList/update/${id}`, {
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
            toDisplayUpdate.innerHTML = "List Updated!";
            nameUpdate.diabled = "disabled";
            updateSidebar();

        })
        .catch((err) => console.log(err))
}
const updateItemStatus = (id) => {
    fetch(`http://localhost:9092/toDoList/update/${id}`, {
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
            toDisplayUpdate.innerHTML = "List Updated!";
            nameUpdate.diabled = "disabled";
            updateSidebar();

        })
        .catch((err) => console.log(err))
}
const deleteItem = () => {
    const idVal = idDeleteItem.value;
    fetch(`http://localhost:9092/item/delete/${idVal}`, {
        method: 'delete'
    })
        .then(res => JSON.stringify(res))
        .then(data => {
            console.log(`List deleted ${data}`);
            toDisplayDeleteItem.innerHTML = "Item deleted!";
            toDisplayDeleteItem.style.display = "";
        })
        .catch(err => console.log(err));
}



