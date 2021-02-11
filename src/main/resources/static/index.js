'use strict';

const listName = document.querySelector("#name")
const desc = document.querySelector("#desc")
const listNameUpdate = document.querySelector("#nameUpdate")
const descUpdate = document.querySelector("#descUpdate")
const idRead = document.querySelector("#listIdRead")
const idUpdate = document.querySelector("#listIdUpdate")
const idDelete=document.querySelector("#listIdDelete")
const idDeleteItem=document.querySelector("#listIdDeleteItem")
const itemName=document.querySelector("#nameItem")
const itemDone=document.querySelector(".radioResult")
const idReadItem = document.querySelector("#listIdReadItem")

const toDisplayCreate = document.querySelector("#displayDivCreate")
const toDisplayRead = document.querySelector("#displayDivRead")
const toDisplayUpdate = document.querySelector("#displayDiv")
const toDisplayDelete = document.querySelector("#displayDivDelete")
const toDisplayCreateItem=document.querySelector("#displayDivCreateItem")
const toDisplayReadItem = document.querySelector("#displayDivReadItem")
const toDisplayUpdateItem = document.querySelector("#displayDivItem")
const toDisplayDeleteItem = document.querySelector("#displayDivDeleteItem")

const printToScreen = (record,display) => {
    
    for (let key in record) {
        const newLine = document.createElement("p");
        let actualText = document.createTextNode(`${key}: ${record[key]}`);
        newLine.append(actualText);
        display.append(newLine)
    }
    
    
}

const printAllToScreen = (set,display) => {
   
    for (let record of set) { 
        display.appendChild(document.createTextNode("~~~~~~~~~~~~"));
        printToScreen(record,display);
    }
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
            console.log(`Request succeeded with JSON response ${data}`)
            toDisplayCreate.innerHTML="List Created!"
            toDisplayCreate.style.visibility = "visible";
        })
        .catch((err) => console.log(err))
}

const readById = () => {
    toDisplayRead.innerHTML="";
    const id=idRead.value;
    fetch(`http://localhost:9092/toDoList/read/${id}`)
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    printToScreen(data,toDisplayRead);
                    toDisplayRead.style.visibility = "visible";
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
                    printAllToScreen(data,toDisplayRead);
                    toDisplayRead.style.visibility = "visible";
                }).catch((err) => console.log(err))
        })
}

// const updateList = () => {
//     const idVal=idUpdate.value
//     let formData = {
//         name: listNameUpdate.value,
//         description: descUpdate.value
//     }
//     fetch(`http://localhost:9092/toDoList/update/${idVal}`, {
//         method: 'put',
//         headers: {
//             "Content-type": "application/json"
//         },
//         body: JSON.stringify(formData)
//     })
//         .then(res => res.json())
//         .then(data => {
//             console.log(`Request succeeded with JSON response ${data}`);
//             console.log(data);
//             toDisplayUpdate.innerHTML="List Updated!";
//             toDisplayUpdate.style.visibility="visible";
//         })
//         .catch((err) => console.log(err))
// }

const deleteList=()=>{
    const idVal=idDelete.value;
    fetch(`http://localhost:9092/toDoList/delete/${idVal}`,{
        method: 'delete'
    })
    .then(res=>JSON.stringify(res))
    .then(data=>{
        console.log(`List deleted ${data}`);
        toDisplayDelete.innerHTML="List deleted!";
        toDisplayDelete.style.visibility="visible";
    })
    .catch(err=> console.log(err));
}

const createItem = () => {
    let formData = {
        name: itemName.value,
        done: itemDone.value
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
            toDisplayCreateItem.innerHTML="Item Created!"
            toDisplayCreateItem.style.visibility = "visible";
        })
        .catch((err) => console.log(err))
}

const readItemById = () => {
    toDisplayReadItem.innerHTML="";
    const id=idReadItem.value;
    fetch(`http://localhost:9092/item/read/${id}`)
        .then((res) => {
            if (res.ok != true) {
                console.log("Status is not OK!");
            }
            res.json()
                .then((data) => {
                    console.log(data);
                    printToScreen(data,toDisplayReadItem);
                    toDisplayReadItem.style.visibility = "visible";
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
                    printAllToScreen(data,toDisplayReadItem);
                    toDisplayReadItem.style.visibility = "visible";
                }).catch((err) => console.log(err))
        })
}

const deleteItem=()=>{
    const idVal=idDeleteItem.value;
    fetch(`http://localhost:9092/item/delete/${idVal}`,{
        method: 'delete'
    })
    .then(res=>JSON.stringify(res))
    .then(data=>{
        console.log(`List deleted ${data}`);
        toDisplayDeleteItem.innerHTML="Item deleted!";
        toDisplayDeleteItem.style.visibility="visible";
    })
    .catch(err=> console.log(err));
}