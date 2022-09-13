<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<script src="/res/js/agiMain.js"></script>
<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js" charset="utf-8"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/login.css">
<script>
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

function kakaoLoginCtl(response) {
	let form = document.getElementById("serverForm");
	form.action="KakaoLogin";
	form.method="post";
	
	form.appendChild(createInput("hidden","suCode",response.id,null,null));
	form.appendChild(createInput("hidden","suEmail",response.kakao_account.email,null,null));
	form.appendChild(createInput("hidden","suNickName",response.kakao_account.profile.nickname,null,null));
	
	form.submit();
}
</script>
</head>
<body onload="getInfo()">
	<div id="background">
		<div id="top">
			<div id="accessArea">
				<span onclick="movePage('MoveLoginPage')">로그인</span> <span
					onclick="movePage('MoveJoinPage')">회원가입</span> <span
					onclick="movePage('MoveCompanyLoginPage')">기업회원</span>
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
			<div id="rightArea" class="scrollBar">
			<div class="fullLogin">
					<div class="loginTitle">로그인</div>
					<div class="smallLoginTitle">일반회원 로그인</div>
					<!-- 네이버 로그인 버튼 -->
						<!-- 아래와같이 아이디를 꼭 써준다. -->
						<div id="naverIdLogin_loginButton" href="javascript:void(0)">
							<img src="/res/img/naver_login.png" width = 300rem height= 70rem alt="image">
						</div>
					<!-- 카카오 로그인 버튼 -->
						<div onclick="kakaoLogin();" id="kakaoIdLogin_loginButton">
							<a href="javascript:void(0)"> 
								<img src="/res/img/kakao_login.png" width = 300rem height= 70rem alt="image">
							</a>
						</div>
						<div class="otherFunctions">
							<div class="joinbutton" onClick="movePage('MoveJoinPage')">회원가입</div>
							<div class="findInfo" onClick="movePage('MoveFindInfo')">아이디 찾기/비밀번호 찾기</div>
						</div>

				</div>
			${message}
				

				<!-- 네이버 스크립트 -->
				<script
					src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js"
					charset="utf-8"></script>

				<script>
					var naverLogin = new naver.LoginWithNaverId({
						clientId : "GgJuJh_y9P_xFKb5fSQj", //내 애플리케이션 정보에 cliendId를 입력해줍니다.
						callbackUrl : "http://localhost:80/callback.jsp", // 내 애플리케이션 API설정의 Callback URL 을 입력해줍니다.
						isPopup : false,
						callbackHandle : true
					});

					naverLogin.init();

					window.addEventListener('load', function() {
						naverLogin.getLoginStatus(function(status) {
							if (status) {
								var email = naverLogin.user.getEmail(); // 필수로 설정할것을 받아와 아래처럼 조건문을 줍니다.
								if (email == undefined || email == null) {
									alert("이메일은 필수정보입니다. 정보제공을 동의해주세요.");
									naverLogin.reprompt();
									return;
								}
							} else {
								console.log("callback 처리에 실패하였습니다.");
							}
						});
					});

					var testPopUp;
					function openPopUp() {
						testPopUp = window
								.open("https://nid.naver.com/nidlogin.logout",
										"_blank",
										"toolbar=yes,scrollbars=yes,resizable=yes,width=1,height=1");
					}
					function closePopUp() {
						testPopUp.close();
					}

				</script>

				<!-- 카카오 스크립트 -->
				<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
				<script>
					Kakao.init('2afdabad57ed92e1cc9de5bd4baed321'); //발급받은 키 중 javascript키를 사용해준다.
					//카카오로그인
					function kakaoLogin() {
						Kakao.Auth.login({
							success : function(response) {
								Kakao.API.request({
									url : '/v2/user/me',
									success : function(response) {
										console.log(response);
										kakaoLoginCtl(response);
									},
									fail : function(error) {
										console.log(error)
									},
								})
							},
							fail : function(error) {
								console.log(error)
							},
						})
					}
				</script>
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