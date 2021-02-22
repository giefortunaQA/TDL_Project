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
	del.id = `DeleteItem${data.id}`

	let edit = document.createElement("button");
	edit.setAttribute("class", "btn ");
	edit.setAttribute("onclick", `showAndSet(${data.id},updateItemFormDiv)`);
	itemId.value =data.id;
	itemNameUpdate.value=data.name;
	itemDone.value=data.done;
	updateItemBtn.setAttribute("onclick", `updateItem()`);
	let editImg = document.createElement("img");
	editImg.src = "./img/pencil.svg";
	edit.appendChild(editImg);
	edit.setAttribute("title", "Edit this task")
	edit.id = `EditItem${data.id}`

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
	console.log(record.done);
	if (record.done == 1) {
		let strike = document.createElement("strike");
		strike.appendChild(actualText);
		display.append(strike);
	} else {
		display.append(actualText);
	}

}

const printAllToScreenItem = (set, display) => {
	for (let record of set) {
		display.appendChild(document.createElement("br"));
		printToScreenItem(record, display);
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
	fetch("http://localhost:9090/item/create", {
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
	fetch(`http://localhost:9090/item/read/${id}`)
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
	fetch("http://localhost:9090/item/read")
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

const updateItem = () => {
	let id = itemId.value;
	let isDone = Boolean;
	if (itemDoneUpdate.value == "yes") {
		isDone = true;
	} else if (itemDoneUpdate.value == "no") {
		isDone = false;
	} else {
		alert("Input invalid. Please type yes or no.")
		return false;
	}
	let formData = {};
	if (itemDoneUpdate.value!=null){
		formData.done=isDone;
	}
	if (itemNameUpdate.value!=""){
		formData.name=itemNameUpdate.value;
	}
	fetch(`http://localhost:9090/item/update/${id}`, {
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
	fetch(`http://localhost:9090/item/delete/${id}`, {
		method: 'delete'
	})
		.then(res => JSON.stringify(res))
		.then(data => {
			console.log(`Item deleted ${data}`);
			readById(listId.value);
		})
		.catch(err => console.log(err));
}

