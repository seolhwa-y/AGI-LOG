<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>환자 예약목록</title><script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/company.css">
</head>
<script>

function moveHealthData(data1,data2,data3){
	let form = document.getElementById("serverForm");
	
	form.appendChild(createInput("hidden","resCode",data1,null,null));
	form.appendChild(createInput("hidden","resBbCode",data2,null,null));
	form.appendChild(createInput("hidden","resDate",data3,null,null));
	
	form.action = "MoveHealthDataList";
	form.method = "post";
	form.submit();
}

function init(){
	if("${companyAccessInfo.coName}" != ""){
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
				<img src="/res/img/agi_logo.png" alt="images">
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
			<div id="doctorMgrTitleDiv">
					<p id="doctorMgrTitle">환자 관리</p>
				</div>
				<div id="doctorList">${patient}</div>
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