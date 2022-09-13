<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 인증</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/company.css">
<script>
	function init(){
		if("${companyBean.coName}" != ""){
			let accessArea = document.getElementById("accessArea");
			accessArea.innerHTML = "";
			
			accessArea.innerHTML = "<span> ${companyBean.coName}님 </span>";
			accessArea.innerHTML += "<span onclick=\"movePage(\'CompanyLogout\')\">로그아웃</span>";
			accessArea.innerHTML += "<span onclick=\"movePage(\'MoveMainPage\')\">일반회원</span>";	
		}
	}
	
	function checkManager() {
		let form = document.getElementById("serverForm");
		
		let coManagerCode = document.getElementsByName("coManagerCode")[0];

		form.appendChild(coManagerCode);
		
		form.action = "CheckManager";
		form.method = "post";
		form.submit();
	}
</script>
</head>
<body onload="init()">
	<div id="background">
		<div id="top">
			<div id="accessArea">

			</div>
			<div id="logo" onclick="movePage('MoveReservationManagement')">
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
				<div id="checkCompanyTitleDiv">
					<p id="checkCompanyTitle"><br/>관리자 코드를 입력해주세요.</p>
						<div class="companyCF">
							<input type="text" name="coManagerCode"  class="basicInput" placeholder="관리자 코드">
							<button type="button" class="basicBtn btn" value="관리자 인증" onclick="checkManager()">확인</button>
						</div>
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