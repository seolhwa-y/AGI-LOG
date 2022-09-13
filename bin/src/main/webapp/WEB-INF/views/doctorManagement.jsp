<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전문의관리</title>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<script src="/res/js/agiMain.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/company.css">
<script>
	function cModal(num) {
		if(num == 1){
			let modal = document.querySelector(".modal");
			modal.style.display = "block";
		}else if(num == 2){
			let modal = document.querySelector(".modal");
			modal.style.display = "none";
		}
	}
	function postAjaxJson(jobCode, clientData, fn) {
		alert(jobCode);
		const ajax = new XMLHttpRequest();
		ajax.onreadystatechange = function() {
			if(ajax.readyState == 4 && ajax.status == 200) { //4:데이터가 넘어옴
				alert(ajax.responseText);
				window[fn](ajax.responseText); //응답 데이터
			}
		};
		//post방식은 form이 필요, 해당 방식은 form이 없으므로 urlencoded방식으로 데이터 전송, 
		ajax.open("post", jobCode);
		ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); //post이지만  url방식으로 데이터가 왔음을 알려줌, 텍스트만 가능
																					//form데이터로(json)전환 하는 방식도 있음 
		ajax.send(clientData);
	}
	function checkDoctorCode(){
		//0:? 1:닉네임
		let doctorCode = document.getElementsByName("doCode")[0];
		let clientData = "code=1&doCode="+doctorCode.value;
		alert(clientData);
		postAjaxJson("CheckDoctorCode",clientData,"callBackOverlap");
	}
	function callBackOverlap(ajaxData) {
		if(ajaxData=="ok"){
			alert("사용 가능한 직원코드입니다.");
		}
		else {
			alert("이미 존재하는 직원코드입니다.");
		}
	}
	/* 비밀번호 유효성 체크 */
	function checkPassword() {
		let password = document.getElementsByName("doPassword")[0];
		
		// 1. 문자 
		if(isChar(password.value, true)) {
			// 2. 길이 
			if(isCharLength(password.value, 6, 15)) {
				document.querySelector(".checkPW").innerText = "올바른 비밀번호 입니다.";
			} else {
				document.querySelector(".checkPW").innerText = "비밀번호는 6자리 이상이어야 합니다.";
	
				return;
			}
		} else {
			let text = document.querySelector(".checkPW").innerText = "패스워드는 영문 대소문자, 숫자, 특수문자 중 3가지 이상의 문자를 사용하셔야 합니다.";
			return;
		}
	}
	/* 비밀번호 유효성 체크, 비밀번호 일치 체크 */
	function isPassword() {
		document.getElementsByName("checkDoPassword")[0].addEventListener("keyup", function(){
			let password = document.getElementsByName("doPassword")[0];
			let isPassword = document.getElementsByName("checkDoPassword")[0];
			if(password.value == isPassword.value) {
				document.querySelector(".isCheckPW").innerText = "비밀번호 일치";
			} else {
				document.querySelector(".isCheckPW").innerText = "비밀번호 일치하지 않음";
			}
		})
	}
	
	// 길이 체크
	function isCharLength(text, minimum, maximum) {
		let result = false;
		if (maximum != null) {
			if (text.length >= minimum && text.length <= maximum) result = true;
		}
		else {
			if (text.length >= minimum) result = true;
		}
		return result;
	}
	// 문자 체크
	function isChar(text, type) {
		let result;
		
		const largeChar = /[A-Z]/; //대문자 
		const smallChar = /[a-z]/; //소문자
		const num = /[0-9]/; //숫자
		const specialChar = /[!@#$%^&*]/; //특수문자
		
		let typeCount = 0;

		if (largeChar.test(text)) typeCount++; 
		if (smallChar.test(text)) typeCount++;
		if (num.test(text)) typeCount++;
		if (specialChar.test(text)) typeCount++;

		if(type){ //비밀번호 3가지 이상 조합 판단
			result = typeCount >= 3? true : false;
		}else{ //이름 한글로만 이루어졌는지 판단
			result = typeCount == 0? true : false;
		}
		return result;
	}
	
	function insertDoctor(){
	
		let code = document.getElementsByName("doCode")[0].value;
		let name = document.getElementsByName("doName")[0].value;
		let password = document.getElementsByName("doPassword")[0].value; 
		
		if(code.length > 3 && code.length == 0){
			alert("직원 코드는 3자리를 입력해주세요.");
			return;
		}
		
		clientData = "doCode="+code+"&doName="+name+"&doPassword="+password;
		postAjaxJson("InsertDoctor",clientData,"callBack");
	}
	
	function deleteDoctor(data){		
		clientData = "doCode="+data;
		postAjaxJson("DeleteDoctor",clientData,"callBack");
	}
	function callBack(ajaxData){
		let data = JSON.parse(ajaxData);
		const doctorList = document.getElementById("doctorList");		
		let info = "";
		

		info += "<table class=\"doctorMgr\">" ;
		info += "<tr>";
		info += "<th>직원코드</th>";
		info += "<th>직원이름</th>";
		info += "<th>관리</th>";
		info += "</tr>";
		for(idx=0; idx<data.length; idx++) {
			info += "<tr>";
			info += "<td>" + data[idx].doCode + "</td>";
			info += "<td>" + data[idx].doName + "</td>";
			info += "<td><button class=\"delBtn\">";
			info += "<i class=\"fa-solid fa-trash-can delBtn editBtn\" "
					+ "onClick=\"deleteDoctor("+ data[idx].doCode +")\"></i>삭제</button>";
					info += "</td>";
					info += "</tr>";
		}
		info += "</table>";

		doctorList.innerHTML = info;
		
		cModal(2);
	}
	
	

	function init() {
		if ("${companyAccessInfo.coName}" != "") {
			let accessArea = document.getElementById("accessArea");
			accessArea.innerHTML = "";

			accessArea.innerHTML = "<span> ${companyAccessInfo.coName}님 </span>";
			accessArea.innerHTML += "<span onclick=\"movePage(\'CompanyLogout\')\">로그아웃</span>";
			accessArea.innerHTML += "<span onclick=\"movePage(\'MoveMainPage\')\">일반회원</span>";
		}
	}
</script>
</head>
<body onload="init()">
	<div id="background">
		<div id="top">
			<div id="accessArea">
				
			</div>
			<div id="logo" onclick="movePage('MoveCompanyLoginPage')">
				<img src="/res/img/agi_logo.png" alt="image">
			</div>
			<div id="mainMenuArea">
				<ul id="mainMenuList">
					<li class="mainMenu" onclick="movePage('MoveDoctorManagement')">의사관리</li>
					<li class="mainMenu" onclick="movePage('MoveReservationManagement')">예약관리</li>
					<li class="mainMenu" onclick="movePage('MoveCheckDoctor')">환자관리</li>
				</ul>
			</div>
		</div>
		<div id="middle">
			<div id="rightArea" class="scrollBar">
				<div id="doctorMgrTitleDiv">
					<p id="doctorMgrTitle">전문의 관리</p>
					<button class="updBtn" id="addDocBtn" onClick="cModal('1')">
						<i class="fa-solid fa-pen updBtn editBtn"></i>추가하기
					</button>
				</div>
				<div id="doctorList">${doctor}</div>


				<div class="modal">
					<div class="modal_body">
						<div class="modal_head">
							<div class="smallJoinTitle">전문의 추가</div>
						</div>

						<div class="modal_content">
							<div class="mJoinGroup">
								<span class="necessaryInfo doTitle">직원코드</span>
								<input type="text" name="doCode" class="mBasicInput doc"
									placeholder="Doctor Code" required />
								<button class="checkBtn btn" onClick="checkDoctorCode()">중복확인</button>
							</div>
							<div class="mJoinGroup">
								<span class="necessaryInfo">직원이름</span>
									<input type="text" name="doName" class="mBasicInput doc"
									placeholder="Doctor Name" required />
							</div>
							<div class="mJoinGroup">
								<span class="necessaryInfo">비밀번호</span>
									<input type="password" name="doPassword"
									onkeyup="checkPassword()" class="mBasicInput doc"
									placeholder="Password"/>
							</div>
								<!-- 비밀번호 유효성 체크하여 보여줌 -->
								<div class="checkPW"></div>
							<div class="mJoinGroup">
								<input type="password" name="checkDoPassword" onkeyup="isPassword()"
									class="mBasicInput vp" placeholder="Verify Password"/>
							</div>	
								<!-- 비밀번호 동일한지 체크하여 보여줌 -->
								<div class = "isCheckPW"></div>
							
						</div>
						<div class="modal_foot">
							<button class="mBtnX btn" onClick="cModal('2')">취소</button>
							<button class="mBtnO btn" onClick="insertDoctor()">등록</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<form id="serverForm"></form>
</body>

</html>