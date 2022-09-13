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
	
	// 추세 그래프 불러오기	
	lineChart("${babyStatusList}", 1); 
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

/*****************차트******************/

function lineChart(data, check){
	// 만나이(7개) :: 0, 1, 2, 3, 4, 5, 6, 7
	// 평균 데이터 삽입 :: 키, 몸무게, 머리둘레
	// 표현 데이터 삽입 :: 키, 몸무게, 머리둘레
	/* 표준 데이터
		columns: [["평균 머리둘레", 33.9, 44.9, 47.2, 48.8, 49.6, 50.2, 50,9],
				  ["평균 키", 49.1, 74, 85.7, 95.4, 101.9, 108.4, 114.7, 120.8],
				  ["평균 몸무게", 3.2, 8.9, 11.5, 14.2, 16.3, 18.4, 20.7]]
	
	 * 회원 데이터
		columns: [["머리둘레", 0, 1, 2, 3, 4, 5, 6],
				  ["키", 0, 1, 2, 3, 4, 5, 6],
				  ["몸무게", 0, 1, 2, 3, 4, 5, 6]]
	 */
	 let height = "", weight = "", head = "";
	 console.log("check DATA :: " + data);
		
	 // 방식에 따른 조건문 :: page : ajax = 1 : 0
	 if(check == 1) {
		 /* 페이지 */
		 height = "${babyStatusList.heightList}".replace(/ /g, "").split("[")[1].split("]")[0].split(",");
		 weight = "${babyStatusList.weightList}".replace(/ /g, "").split("[")[1].split("]")[0].split(",");
		 head = "${babyStatusList.headList}".replace(/ /g, "").split("[")[1].split("]")[0].split(",");
	 } else if (check == 0) {
		 /* API 방식 */
		 height = data.heightList.replace(/ /g, "").split("[")[1].split("]")[0].split(",");
		 weight = data.weightList.replace(/ /g, "").split("[")[1].split("]")[0].split(",");
		 head = data.headList.replace(/ /g, "").split("[")[1].split("]")[0].split(",");
	 }

	// 나이별 건강 데이터 분리 체크
	console.log("lineChart_height :: " + height);
	console.log("lineChart_weight :: " + weight);
	console.log("lineChart_head :: " + head);

	// 아이 나이 평균 데이
	var chart = bb
			.generate({
				data : {
					columns : [["평균 머리둘레", 34.1, 45.5, 48.1, 49.2, 49.9, 50.2, 50.5 ],
								["평균 키", 49.6, 77, 88.2, 95.6, 102.7, 109.4, 115.5, 122.4 ],
								["평균 몸무게", 3.2, 9.8, 12.5, 14.3, 16.3, 18.4, 20.7, 23.9 ]],
					type : "line", // for ESM specify as: line()
				},
				bindto : "#lineChart"
			});

	// 머리둘레 표현
	setTimeout(function() {
		chart.load({
			columns : [[ "머리둘레", head[0], head[1], head[2], head[3], head[4], head[5], head[6], head[7] ]]
		});
	}, 1000);
	
	// 몸무게 표현
	setTimeout(function() {
		chart.load({
			columns : [[ "몸무게", weight[0], weight[1], weight[2], weight[3], weight[4], weight[5], weight[6], weight[7] ]]
		});
	}, 1500);
	
	// 키 표현
	setTimeout(function() {
		chart.load({
			columns : [[ "키", height[0], height[1], height[2], height[3], height[4], height[5], height[6], height[7] ]]
		});
	}, 2000);

/* 	// 평균 머리둘레 제거
	setTimeout(function() {
		chart.unload({
			ids : "평균 머리둘레"
		});
	}, 2001);

	// 평균 몸무게 제거
	setTimeout(function() {
		chart.unload({
			ids : "평균 몸무게"
		});
	}, 2002);

	// 평균 키 제거
	setTimeout(function() {
		chart.unload({
			ids : "평균 키",
		});
	}, 2003); */
}

//셀렉트 박스 선택시 아이 추세 변경
function cngHealthStatus() {
	// 선택된 아기의 코드
	var babyCode = document.getElementById("babyName").value;
	var clientData = "bbCode=" + babyCode;
	
	console.log("현재 선택된 아이 코드 : " + babyCode);
	
	postAjaxJson("SelectBaby", clientData, "cngLineChert");
}

//콜백 함수 
function cngLineChert(ajax) {
	let babyStatusList = JSON.parse(ajax);
	
	console.log("cngLineChert_babyStatusList :: " + babyStatusList);
	
	document.getElementById("lineChart").innerHTML = "";
	
	lineChart(babyStatusList, 0);
}

</script>
<style>
    #lineChart {
    	margin : 0;
        width: 100%;
        height:90%;
    }
</style>
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
			<div id="logo" onclick="movePage('MoveMainPage')"><span id="txt">아기-로그</span>
				<img src="/res/img/logo.png" alt="images">
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
			    <div id="lineChart" >
			    	<!-- 차트 영역 -->
			    </div>
				<div>
					<!-- 아이 선택 버튼 -->
			   		${babyStatusList.babyList} 
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