'use strict';

const listName = document.querySelector("#name")
const desc = document.querySelector("#desc")
const listNameUpdate = document.querySelector("#nameUpdate")
const descUpdate = document.querySelector("#descUpdate")
const idCreateItem = document.querySelector("#listId")
const idRead = document.querySelector("#listIdRead")
const idUpdate = document.querySelector("#listIdUpdate")
const idDelete = document.querySelector("#listIdDelete")
const idDeleteItem = document.querySelector("#listIdDeleteItem")
const itemName = document.querySelector("#itemName")
const itemDone = document.querySelector(".radioResult")
const idReadItem = document.querySelector("#listIdReadItem")
const createFormForm=document.getElementById("createFormForm")

const toDisplayCreate = document.querySelector("#createItemDiv")
const toDisplayRead = document.querySelector("#displayDivRead")
const toDisplayUpdate = document.querySelector("#displayDiv")
const toDisplayDelete = document.querySelector("#displayDivDelete")
const toDisplayCreateItem = document.querySelector("#displayPCreateItem")
const toDisplayReadItem = document.querySelector("#displayDivReadItem")
const toDisplayUpdateItem = document.querySelector("#displayDivItem")
const toDisplayDeleteItem = document.querySelector("#displayDivDeleteItem")

const createForm = document.querySelector("#createForm")
const sideBarList = document.querySelector(".sideBarList")

const resetCont = () => {
    cont.style.display = "none";
}

const formBtn = document.querySelector(".formBtn");
const stopDefault = (event) => {
    console.log(event);
    event.preventDefault();
}
formBtn.addEventListener("click", stopDefault);

const addEditDelete=(location)=>{
    //add delete
    let del=document.createElement("button");
    del.setAttribute("class","btn");
    del.setAttribute("onclick","dummyDelete()");
    let delImg=document.createElement("img");
    delImg.src="./img/trash.svg";
    del.appendChild(delImg);
    del.setAttribute("title","Delete this list")
   
    //add edit
    let edit=document.createElement("button");
    edit.setAttribute("class","btn ");
    edit.setAttribute("onclick","dummyUpdate()");
    let editImg=document.createElement("img");
    editImg.src="./img/pencil.svg";
    edit.appendChild(editImg);
    edit.setAttribute("title","Edit this list")
    location.append(edit);
    location.append(del);
   }
const printToScreen = (record, display) => {
    addEditDelete(display);
    for (let key in record) {
        const newLine = document.createElement("p");
        let actualText = document.createTextNode(`${key}: ${record[key]}`);
        newLine.append(actualText);
        display.append(newLine);
    }
    
}

const printAllToScreen = (set, display) => {
    for (let record of set) {
        display.appendChild(document.createElement("hr"));
        printToScreen(record, display);
    }
    display.appendChild(document.createElement("hr"));
}

// dummy update
const dummyUpdate=()=>{
    console.log("update btn made and pushed");
}

const dummyDelete=()=>{
    console.log("dummy delete");
}


const addToList = (data) => {
    let text = document.createTextNode(`(${data.id}) : ${data.name}`);
    let btn=document.createElement("button");
    btn.setAttribute("class","btn btn-link");
    btn.setAttribute("onclick",`readThis(${data.id})`);
    let p=document.createElement("p");
    btn.appendChild(text);
    sideBarList.appendChild(btn);
    addEditDelete(sideBarList);
}

const show = (object) => {
    object.style.display = "block";
}

const hide = (object) => {
    object.style.display = "none";
}


const createList = () => {
    let formData = {
        name: listName.value,
        description: desc.value
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
            addToList(data);
            itemName.disabled = false;
            idCreateItem.disabled = false;
            toDisplayCreate.innerHTML = `Enter id: ${data.id} to add task`;
            show(toDisplayCreate);
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

const readThis=(thisId)=>{
    toDisplayRead.innerHTML = "";
    fetch(`http://localhost:9092/toDoList/read/${thisId}`)
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    printToScreen(data, toDisplayRead);
                    toDisplayRead.style.display = "block";
                    readAllItemsInList();
                }).catch(err => console.log(err))
        })
}

const readById = () => {
    toDisplayRead.innerHTML = "";
    var id = idRead.value;
    fetch(`http://localhost:9092/toDoList/read/${id}`)
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    printToScreen(data, toDisplayRead);
                    toDisplayRead.style.display = "block";
                    readAllItemsInList(id);
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
                    printAllToScreen(data, toDisplayRead);
                    toDisplayRead.style.display = "block";
                }).catch((err) => console.log(err))
        })
}

const updateSidebar = () => {
    sideBarList.innerHTML="";
    fetch("http://localhost:9092/toDoList/read")
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    for (let record of data) {
                        addToList(record);
                        sideBarList.appendChild(document.createElement("br"))
                    }
                }).catch((err) => console.log(err))
        })
}

const updateList = () => {
    let idVal = idUpdate.value
    let formData = {
        name: listNameUpdate.value,
        description: descUpdate.value
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
            toDisplayUpdate.innerHTML = "List Updated!";
            toDisplayUpdate.style.display = "";
        })
        .catch((err) => console.log(err))
}

const deleteList = () => {
    const idVal = idDelete.value;
    fetch(`http://localhost:9092/toDoList/delete/${idVal}`, {
        method: 'delete'
    })
        .then(res => JSON.stringify(res))
        .then(data => {
            console.log(`List deleted ${data}`);
            toDisplayDelete.innerHTML = "List deleted!";
            toDisplayDelete.style.display = "";
        })
        .catch(err => console.log(err));
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
            toDisplayCreateItem.appendChild(data.name);
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

const readAllItemsInList = () => {
    fetch(`http://localhost:9092/item/read/same-list/1`)
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    printAllToScreen(data,toDisplayRead);
                    console.log(data);
                }).catch((err) => console.log(err))
        })
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

const reset=(form)=>{
    form.reset();
}

function openNav() {
    document.getElementById("mySidepanel").style.width = "275px";
    updateSidebar();
  }
  
  /* Set the width of the sidebar to 0 (hide it) */
  function closeNav() {
    document.getElementById("mySidepanel").style.width = "0";
  }