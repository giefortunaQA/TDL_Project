'use strict';

//inputs
const listName = document.querySelector("#name")
const listNameUpdate = document.querySelector("#nameUpdate")
const idRead = document.querySelector("#listIdRead")
const idDelete = document.querySelector("#listIdDelete")
const updateBtn = document.getElementById("updateBtn");

const setListId = (id) => {
	let listId = document.getElementById("listId");
	listId.value = id;
}
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
const createItemSeparate = document.getElementById("createItemSeparate");

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
const addEditDelete = (data, location) => {
	let del = document.createElement("button");
	del.setAttribute("class", "btn");
	del.setAttribute("onclick", `deleteList(${data.id})`);
	let delImg = document.createElement("img");
	delImg.src = "./img/trash.svg";
	del.appendChild(delImg);
	del.setAttribute("title", "Delete this list")
	del.id = `Delete${data.id}`

	let edit = document.createElement("button");
	edit.setAttribute("class", "btn ");
	edit.setAttribute("onclick", `onlyShow(updateFormDiv)`);
	listNameUpdate.setAttribute("placeholder", `${data.name}`)
	updateBtn.setAttribute("onclick", `updateList(${data.id})`);
	let editImg = document.createElement("img");
	editImg.src = "./img/pencil.svg";
	edit.appendChild(editImg);
	edit.setAttribute("title", "Edit this list")
	edit.id = `Edit${data.id}`
	location.append(edit);
	location.append(del);
}

const addPlus = (id,location) => {
	let plus = document.createElement("button");
	plus.setAttribute("class", "btn");
	let plusImg = document.createElement("img");
	plusImg.src = "./img/plus-circle.svg";
	plus.appendChild(plusImg);
	plus.setAttribute("onclick", `onlyShow(createItemSeparate)`);
	plus.setAttribute("title", "Add new task");
	plus.id = `addTaskBtn${id}`
	location.appendChild(plus);
}

//with icons
const printToScreen = (record, display) => {
	addEditDelete(record, display);
	printToScreenOnly(record, display);
}
const printToScreenOnly = (record, display) => {
	for (let key in record) {
		if (key != "id") {
			const newLine = document.createElement("p");
			let actualText = document.createTextNode(`${record[key]}`);
			let btn = createLinkBtn(actualText, record.id);
			newLine.append(btn);
			display.append(newLine);
		}
	}
}


const printAllToScreen = (set, display) => {
	for (let record of set) {
		display.appendChild(document.createElement("hr"));
		printToScreenOnly(record, display);
		console.log(record.id);
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
	btn.id = `YourList${data.id}`
	sideBarList.appendChild(btn);
}

const updateSidebar = () => {
	sideBarList.innerHTML = "";
	fetch("http://localhost:9090/toDoList/read")
		.then((res) => {
			if (res.ok != true) {
				console.log("Status is not OK!");
			}
			res.json()
				.then((data) => {
					if (data.length == 0) {
						let italic = document.createElement("i");
						let text = document.createTextNode("No Lists.")
						italic.appendChild(text);
						sideBarList.appendChild(italic);
						console.log();
					}
					else {
						for (let record of data) {
							addToSidebar(record);
							sideBarList.appendChild(document.createElement("br"))
						}
					}
					console.log("Sidebar updated.");
				}).catch((err) => console.log(err))
		})
}

const createList = () => {
	let formData = {
		name: listName.value,
	}
	fetch("http://localhost:9090/toDoList/create", {
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
			readById(data.id);
			show(createItemSeparate);
			updateSidebar();
		})
		.catch((err) => console.log(err))
}

const findLatestId = () => {
	fetch("http://localhost:9090/toDoList/latest")
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
	fetch(`http://localhost:9090/toDoList/read/${id}`)
		.then((res) => {
			if (res.ok != true) {
				console.log("Status is not OK!");
			}
			res.json()
				.then((data) => {
					console.log(data);
					onlyShow(toDisplayRead);
					printToScreen(data, toDisplayRead);
					setListId(id);
					readAllItemsInList(id);
				}).catch(err => console.log(err))
		})
}

const readAllLists = () => {
	toDisplayRead.innerHTML = "";
	fetch("http://localhost:9090/toDoList/read")
		.then((res) => {
			if (res.ok != true) {
				console.log("Status is not OK!");
			}
			res.json()
				.then((data) => {
					console.log(data);
					onlyShow(toDisplayRead);
					if (data.length == 0) {
						let italic = document.createElement("i");
						let text = document.createTextNode("No Lists.")
						italic.appendChild(text);
						toDisplayRead.appendChild(italic);
					}
					else {
						printAllToScreen(data, toDisplayRead);
					}
				}).catch((err) => console.log(err))
		})
}


const updateList = (id) => {
	toDisplayUpdate.innerHTML = "";
	let formData = {
		name: listNameUpdate.value,
	}
	fetch(`http://localhost:9090/toDoList/update/${id}`, {
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
			readById(id);
			updateSidebar();
		})
		.catch((err) => console.log(err))
}

const deleteList = (idVal) => {
	toDisplayDelete.innerHTML = "";
	fetch(`http://localhost:9090/toDoList/delete/${idVal}`, {
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
	fetch(`http://localhost:9090/item/read/in-list/${id}`)
		.then((res) => {
			if (res.ok != true) {
				console.log("Status is not OK!");
			}
			res.json()
				.then((data) => {
					toDisplayRead.append(document.createElement("hr"));
					toDisplayRead.append(document.createTextNode("Tasks:"));
					addPlus(id,toDisplayRead);
					if (data.length == 0) {
						toDisplayRead.append(document.createElement("hr"));
						let italic = document.createElement("i");
						let noTask = document.createTextNode("No tasks");
						italic.appendChild(noTask);
						toDisplayRead.append(italic);
					} else {
						printAllToScreenItem(data, toDisplayRead);
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

