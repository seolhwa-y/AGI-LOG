<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>통계기록</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">

<!-- 카카오 스크립트 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<!-- 네이버 스크립트 -->
<script	src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>

<!-- 테마 CSS -->
<link rel="stylesheet" href="https://naver.github.io/billboard.js/release/latest/dist/theme/datalab.min.css">
<!-- 차트 JAVA SCRIPT -->
<script src="https://naver.github.io/billboard.js/release/latest/dist/billboard.pkgd.min.js"></script>
<script>
Kakao.init('2afdabad57ed92e1cc9de5bd4baed321');
function getInfo() {
	let accessArea = document.getElementById("accessArea");
	if ("${accessInfo.type}"!= null&&"${accessInfo.type}"!="") {
		accessArea.innerHTML = "";
		accessArea.innerHTML = "<span> ${accessInfo.suNickName}님 </span>";
		if ("${accessInfo.type}"== "kakao") {
			accessArea.innerHTML +="<span onclick=\"kakaoLogout();\">로그아웃</span>"
		} else if ("${ accessInfo.type }"== "naver") {
			accessArea.innerHTML +="<span onclick=\"naverLogout(); return false;\">로그아웃</span>"
		} else ;
		accessArea.innerHTML += "<span onclick=\"movePage('MoveCompanyLoginPage')\">기업회원</span>";
	}
}
function openPopUp() {
	testPopUp = window
		.open("https://nid.naver.com/nidlogin.logout",
			"_blank",
			"toolbar=yes,scrollbars=yes,resizable=yes,width=1,height=1");
}
function closePopUp() {
	testPopUp.close();
}

function naverLogout() {
	openPopUp();
	setTimeout(function() {
		closePopUp();
	}, 1000);
	logout();
}
function kakaoLogout() {
	if (Kakao.Auth.getAccessToken()) {
		Kakao.API.request({
			url: '/v1/user/unlink',
			success: function(response) {
				logout();
			},
			fail: function(error) {
				console.log(error)
			},
		})
		Kakao.Auth.setAccessToken(undefined)
	}
}

// 셀렉트 박스 선택시 아이 추세 변경
function cngHealthStatus() {
	// 선택된 아기의 코드
	var babyCode = document.getElementById("babyName").value;
	console.log("현재 선택된 아이 코드 : " + babyCode);
}
</script>
</head>
<body onload="getInfo()">
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
				<img src="/res/img/agi_logo.png" alt="images">
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
						<li class="nav-sText" onclick="movePage('MoveDailyDiaryPage')"><span>전체피드</span></li>
						<li class="nav-sText" onclick="movePage('MoveMyDailyDiaryPage')"><span>내 피드</span></li>
						<li class="nav-text"><span>건강일기</span></li>
						<li class="nav-sText" onclick="movePage('MoveHealthDiaryPage')"><span>내 피드</span></li>
						<li class="nav-sText" onclick="movePagePost('MoveHealthStatusPage')"><span>통계기록</span></li>
						<li class="nav-sText" onclick="movePage('MoveDoctorComment')"><span>진료기록</span></li>

					</ul>
				</nav>

				<!-- ------------------------------------------------------------------------ -->
			</div>
			<div id="rightArea" class="scrollBar">
			<!-- 차트 영역 -->
			    <div id="barChart"></div>
			    <script>
			    	// 그래프 생성시 고정으로 나오는 선
			        var chart1 = bb.generate({
			            data: {
			                columns: [
			                    ["몸무게", 30, 40, 50, 60, 50, 40],
			                    ["키", 130, 135, 140, 145, 150, 160]
			                ],
			                type: "bar"
			            },
			            bar: {
			                width: {
			                    ratio: 0.5
			                }
			            },
			            bindto: "#barChart"
			        });
			        // 시간이 지나면 생기는 하나의 선
			        setTimeout(function () {
			            chart1.load({
			                columns: [
			                    ["평균", 130, 140, 150, 155, 160, 170]
			                ]
			            });
			        }, 1000);
			    </script>
			
			    <!-- Markup -->
			    <div id="lineChart"></div>
			    <script>
			        // for ESM environment, need to import modules as:
			        // import bb, {line} from "billboard.js";
			        var chart2 = bb.generate({
			            data: {
			                columns: [
			                    ["키", 100, 120, 130, 140, 150, 160],
			                    ["몸무게", 10, 20, 30, 35, 40, 45]
			                ],
			                type: "line", // for ESM specify as: line()
			            },
			            bindto: "#lineChart"
			        });
			
			        setTimeout(function () {
			            chart2.load({
			                columns: [
			                    ["키", 110, 120, 130, 140, 150, 160]
			                ]
			            });
			        }, 1000);
			
			        setTimeout(function () {
			            chart2.load({
			                columns: [
			                    ["평균", 95, 105, 115, 125, 135, 145]
			                ]
			            });
			        }, 1500);
			
			        setTimeout(function () {
			            chart2.unload({
			                ids: "data1"
			            });
			        }, 2000);
			    </script>
			   <div> ${babyStatusList.babyList} </div>
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