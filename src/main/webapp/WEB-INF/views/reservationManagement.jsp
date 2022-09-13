<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약관리</title><script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/company.css">
<script>
function init() {
	if ("${companyAccessInfo.coName}" != "") {
		let accessArea = document.getElementById("accessArea");
		accessArea.innerHTML = "";

		accessArea.innerHTML = "<span> ${companyAccessInfo.coName}님 </span>";
		accessArea.innerHTML += "<span onclick=\"movePage(\'CompanyLogout\')\">로그아웃</span>";
		accessArea.innerHTML += "<span onclick=\"movePage(\'MoveMainPage\')\">일반회원</span>";
	}
}
	
	function updateReservation(resCode, idx, idx2) {
		let form = document.getElementById("serverForm");

		alert(resCode + "," + idx + "," + idx2);
		let rcCode = document.getElementsByName("selectResState")[idx];
		let rcCode2 = rcCode.options[rcCode.selectedIndex].value;
		
		form.appendChild(createInput("hidden","resCode",resCode,null,null));

		let hidden1 = document.createElement("input");
		hidden1.type = "hidden";
		hidden1.name = "rcCode";
		hidden1.value = rcCode2;
		 
		form.appendChild(hidden1);
		
		if (idx2 != "") {
			alert("실행체크");
			let doCode = document.getElementsByName("selectDoctor")[idx2];
			let doCode2 = doCode.options[doCode.selectedIndex].value;
			
			let hidden2 = document.createElement("input");
			hidden2.type = "hidden";
			hidden2.name = "doCode";
			hidden2.value = doCode2;
			
			form.appendChild(hidden2);
		}

		form.action = "UpdateReservation";
		form.method = "post";
		form.submit();
	}
</script>
</head>
<body onload="init()">
	<div id="background">
		<div id="top">
			<div id="accessArea">
				<span onclick="movePage('MoveCompanyLoginPage')">로그인</span>
                <span onclick="movePage('MoveCompanyJoinPage')">회원가입</span>
                <span onclick="movePage('MoveMainPage')">일반회원</span>
			</div>
			<div id="logo" onclick="movePage('MoveReservationManagement')"><span id="txt">아기-로그</span>
				<img src="/res/img/logo.png" alt="images">
			</div>
			<div id="mainMenuArea">
				<ul id="mainMenuList">
					<li class="mainMenu" onclick="movePage('MoveDoctorManagement')">의사관리</li>
					<li class="mainMenu" onclick="movePage('MoveReservationManagement')">예약관리</li>
					<li class="mainMenu" onclick="movePage('IntoCheckDoctor')">환자관리</li>
				</ul>
			</div>
		</div>
		<div id="middle">
			<div id="rightArea" class="scrollBar">
				${resInfo}
			</div>
		</div>
		<div class="modal">
            <div class="modal_body">
				<div class="modal_head">
					<i class="fa-solid fa-xmark closeBtn editBtn"></i><br />
				</div>
				<div class="modal_content"></div>
				<div class="modal_foot"></div>
            </div>
        </div>
	</div>
	<form id="serverForm"></form>
</body>

</html>