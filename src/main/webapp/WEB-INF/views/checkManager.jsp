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
<script>
	function init(){
		if("${companyBean.coName}" != ""){
			let accessArea = document.getElementById("accessArea");
			accessArea.innerHTML = "";
			
			accessArea.innerHTML = "<span> ${companyBean.coName} </span>";
			accessArea.innerHTML += "<span onclick=\"movePage(\'CompanyLogout\')\">로그아웃</span>";
			accessArea.innerHTML += "<span onclick=\"movePage(\'MoveMainPage\')\">일반회원</span>";	
		}
		
	}
	
	function checkManager(action) {
		let form = document.getElementById("serverForm");
		let managerCode = document.getElementsByName("coManagerCode")[0];

		form.appendChild(managerCode);
		
		form.action = action;
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
			<div id="logo" onclick="moveManagerPage('MoveCheckManager',1)">
				<img src="/res/img/agi_logo.png" alt="images">
			</div>
			<div id="mainMenuArea">
				<ul id="mainMenuList">
					<li class="mainMenu" onclick="moveManagerPage('MoveCheckManager',0)">의사관리</li>
					<li class="mainMenu" onclick="moveManagerPage('MoveCheckManager',1)">예약관리</li>
					<li class="mainMenu" onclick="movePage('MoveCheckDoctor')">환자관리</li>
				</ul>
			</div>
		</div>
		<div id="middle">
			<div id="rightArea" class="scrollBar">
				<input type="text" name="coManagerCode">
				<input type="button" value="매니저 인증" onclick="checkManager('CheckManager')">
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