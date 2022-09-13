<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>기업 로그인</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/company.css">

<script>
	function init(){
		if("${message}" != ""){
			alert("${message}");
		}
	}
	function companyLogin(){
		let form = document.getElementById("serverForm");
		let id = document.getElementsByName("coCode")[0];
		let pw = document.getElementsByName("coPassword")[0];
		
		form.appendChild(id);
		form.appendChild(pw);
		
		console.log(form);
		
		form.action="CompanyLogin";
		form.method="post";
		
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
			<div id="logo" onclick="moveManagerPage('MoveCheckManager',1)"><span id="txt">아기-로그</span>
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
					<p id="checkCompanyTitle">기업 로그인</p>
					<div class="companyCF">
						<input type="text" name="coCode" class="basicInput" placeholder="기업 아이디">
						<input type="password" name="coPassword" class="basicInput" placeholder="기업 비밀번호">
						<input type="button" value="로그인" class="basicBtn btn" onclick="companyLogin()">
					</div>
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