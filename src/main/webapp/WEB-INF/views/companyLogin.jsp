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

<script>
	function companyLogin(){
		let form = document.getElementById("serverForm");
		let id = document.getElementsByName("coCode")[0];
		let pw = document.getElementsByName("coPassword")[0];
		alert(id.value);
		alert(pw.value);
		
		form.appendChild(id);
		form.appendChild(pw);
		
		console.log(form);
		
		form.action="CompanyLogin";
		form.method="post";
		
		form.submit();
	}
</script>
</head>
<body>
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
				<input type="text" name="coCode">
				<input type="password" name="coPassword">
				<input type="button" value="로그인" onclick="companyLogin()">
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