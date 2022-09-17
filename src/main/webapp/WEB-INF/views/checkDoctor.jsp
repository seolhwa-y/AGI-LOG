<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>담당의 인증</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/company.css">
<script>
	function checkDoctor() {
		let form = document.getElementById("serverForm");
		let doCode = document.getElementsByName("doctorCode")[0].value;
		let doPassword = document.getElementsByName("doctorPassword")[0].value;
	
	
		let clientData = "doCode="+doCode+"&doPassword="+doPassword;
		

		form.appendChild(createInput("hidden","doCode",doCode,null,null));
		form.appendChild(createInput("hidden","doPassword",doPassword,null,null));
		
		form.action = "MovePatientManagement";
		form.method = "post";
		form.submit();
	}
	function init(){
		if("${companyAccessInfo.coName}" != ""){
			let accessArea = document.getElementById("accessArea");
			accessArea.innerHTML = "";
			
			accessArea.innerHTML = "<span> ${companyAccessInfo.coName}님 </span>";
			accessArea.innerHTML += "<span onclick=\"movePage(\'CompanyLogout\')\">로그아웃</span>";
		}
		if("${message}" != ""){
			alert("${message}");
		}
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
					<div id="checkCompanyTitleDiv">
						<p id="checkCompanyTitle"><br/>담당의 정보를 입력해주세요.</p>
							<div class="companyCF">
								<input type="text" name="doctorCode"  class="basicInput" placeholder="담당의 코드">
								<input type="password" name="doctorPassword"  class="basicInput" placeholder="담당의 비밀번호">
								<button type="button" class="basicBtn btn" value="담당의 인증" onclick="checkDoctor()">확인</button>
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
		</div>
	<form id="serverForm"></form>
</body>

</html>