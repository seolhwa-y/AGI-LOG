<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>감성일기 내피드</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">

<!-- 카카오 스크립트 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<!-- 네이버 스크립트 -->
<script	src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
<style>
	ul {
    list-style: none;
	}
	.feed {
		float: left;
		margin-left: 2%;
		margin-right: 6%;
		margin-bottom: 2%;
	}

	#rightArea{
		width: 85%;
		height: 100%;
		overflow-x:hidden;
		padding: 0.4% 0 0 0;
	}
	#rightArea_top{
		width: 100%;
    	height: 5%;
    	padding: 1% 5%;
    	margin-left: 3px;
	}
	#rightArea_middle{
		margin-top: 1%;
		width: 100%;
		height: 89%;
		overflow-x:hidden;
		overflow-y: scroll;
	}
	.like{
	    color: red;
	    font-size: 130%;
	    float: right;
	    margin-right: 4%;
	}
	.feed{
		width: 25%;
		height: 389px;
		border-radius: 5px;
		background-color: white;
		box-shadow: 4px 4px 10px;
	}
	.feed_top{
		width: 100%;
		height: 70%;
		border-bottom: 1px solid #616161;
	}
	.feed_bottom{

		width: 90%;
		height: 23%;
		margin: 3% auto;
		font-size: 140%;
	}
	#sortArea{ 

		float: left;
		height: 95%;
		width: 10%;
	}
	#hashTagArea{

		float: left;
		height: 95%;
		width: 19%;
		margin-left: 4%;
	}
	#writeFeedArea{

		float: right;
		height: 95%;
		width: 10%;
		margin-right: 7%;
	}
	#sort{
		height: 100%;
		width: 100%;
		font-size: 140%;
	}
	#hashTag{
		height: 90%;
		width: 100%;
		font-size: 140%;
	}
	#writeFeed{
		height: 90%;
		width: 100%;
		font-size: 140%;
	}
	#feed_top ,img{
		width: 100%;
    	height: 100%;
		border-radius: 5px 5px 0 0;
	}
	#feedDate{
		margin-bottom: 3%;
		border-bottom: 1px solid dimgray;
	}
	#searchHashTag{
		border: solid 1px black;
		width: 5%;
		height: 100%;
		margin-left: 2%;
		font-size: 110%;
	}
	.modal_body {
    position: relative;
    top: 50%;
    left: 50%;
    width: 700px;
    height: 700px;
    padding: 20px;
    text-align: center;
    /*background-color: rgb(255, 255, 255);*/
    background-image: url("/res/img/newModal.png");
    background-size : cover;
    border-radius: 10px;
    box-shadow: 0 2px 3px 0 rgba(34, 36, 38, 0.15);
    transform: translateX(-50%) translateY(-50%);
    
}

</style>
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
	
	let feedList = document.getElementById("feedList");
	feedList.innerHTML = "${myDailyDiaryFeed}";
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
				<div id="rightArea_top">
					<div id="sortArea">
						<select id="sort">
							<option value = "newList">최신순</option>
							<option value = "oldList">오래된순</option>
							<option value = "likeList">좋아요순</option>
							<option value = "viewList">조회순</option>
						</select>
					</div>
					<div id="writeFeedArea">
						<input id="writeFeed"type="button" class="writeBtn btn" value="글쓰기" >
					</div>
				</div>
				<div id="rightArea_middle" class="scrollBar">
					<ul id="feedList"></ul>
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