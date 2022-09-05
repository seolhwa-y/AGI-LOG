function movePage(action) {
	let form = document.getElementById("serverForm");

	form.action = action;
	form.method = "get";
	form.submit();
}

function moveManagerPage(action,code) {
	//code 0:전문의 1:예약관리
	let form = document.getElementById("serverForm");
	form.appendChild(createInput("hidden","pageCode",code,null,null));
	form.action = action;
	form.method = "get";
	form.submit();
}

function createInput(type, name, value, className, placeholder) {
	let input = document.createElement("input");

	if (type != null) input.setAttribute("type", type);
	if (name != null) input.setAttribute("name", name);
	if (value != null) input.setAttribute("value", value);
	if (className != null) input.setAttribute("class", className);
	if (placeholder != null) input.setAttribute("placeholder", placeholder);

	return input;
}
function logout() {
	let form = document.getElementById("serverForm");
	form.action = "Logout";
	form.method = "post";
	form.submit();
}
/* Ajax :: GET */
function getAjaxJson(jobCode, clientData, fn) {
	const ajax = new XMLHttpRequest();
	const action = (clientData!="")?(jobCode + "?" + clientData):jobCode;
	
	ajax.onreadystatechange = function() {
		if(ajax.readyState == 4 && ajax.status == 200) { //4:데이터가 넘어옴
			window[fn](ajax.responseText); //응답 데이터
		}
	};

	ajax.open("get", action);
	ajax.send();
}
/* Ajax :: POST */
function postAjaxJson(jobCode, clientData, fn) {
	const ajax = new XMLHttpRequest();
	ajax.onreadystatechange = function() {
		if(ajax.readyState == 4 && ajax.status == 200) { //4:데이터가 넘어옴
			window[fn](ajax.responseText); //응답 데이터
		}
	};
	//post방식은 form이 필요, 해당 방식은 form이 없으므로 urlencoded방식으로 데이터 전송, 
	ajax.open("post", jobCode);
	ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); //post이지만  url방식으로 데이터가 왔음을 알려줌, 텍스트만 가능
																				//form데이터로(json)전환 하는 방식도 있음 
	ajax.send(clientData);
}