<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자유게시판</title>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="agiMain.css">
</head>
<body>
	<div id="background">
		<div id="top">
			<div id="accessArea">
				<span onclick="movePage('MoveLoginPage')">로그인</span>
                <span onclick="movePage('MoveJoinPage')">회원가입</span>
                <span onclick="movePage('MoveCompanyLoginPage')">기업회원</span>
			</div>
			<div id="logo" onclick="movePage('MoveMainPage')">
				<img src="img/agi_logo.png" alt="imgages">
			</div>
			<div id="mainMenuArea">
				<ul id="mainMenuList">
					<li class="mainMenu" onclick="movePage('MoveDailyDiaryPage')">육아일기</li>
					<li class="mainMenu" onclick="movePage('MoveMapPage')">지도</li>
					<li class="mainMenu" onclick="movePage('MoveCalendarPage')">캘린더</li>
					<li class="mainMenu" onclick="movePage('MoveBoardPage')">게시판</li>
					<li class="mainMenu" onclick="movePage('MoveMyPage')">마이페이지</li>
				</ul>
			</div>
		</div>
		<div id="middle">
			<div id="leftArea">
				<!-- navigator -->
				<!-- ------------------------------------------------------------------------ -->


				<nav class="side-menu">
					<ul class="menus">
						<li class="nav-text"><span>감성일기</span></li>
						<li class="nav-sText"><span>전체피드</span></li>
						<li class="nav-sText"><span>내 피드</span></li>
						<li class="nav-text"><span>건강일기</span></li>
						<li class="nav-sText"><span>내 피드</span></li>
						<li class="nav-sText"><span>통계기록</span></li>
						<li class="nav-sText"><span>진료기록</span></li>

					</ul>
				</nav>

				<!-- ------------------------------------------------------------------------ -->
			</div>
			<div id="rightArea" class="scrollBar"></div>
		</div>
	</div>
</body>

</html>