<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="/res/js/agiMain.js"></script>
<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js" charset="utf-8"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/socialJoin.css">
<script>
	function kakaoJoinCtl(response) {
		let form = document.getElementById("serverForm");
		form.action="MoveJoinFormPage";
		form.method="post";
		
		form.appendChild(createInput("hidden","suCode",response.id,null,null));
		form.appendChild(createInput("hidden","suEmail",response.kakao_account.email,null,null));
		form.appendChild(createInput("hidden","suNickName",response.kakao_account.profile.nickname,null,null));
		
		form.submit();
	}
</script>
</head>
<body>
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
				<div class="fullJoin">
					<div class="joinTitle">회원가입</div>
					<div class="smallJoinTitle">일반 회원가입</div>
					<!-- 네이버 로그인 버튼 -->
						<!-- 아래와같이 아이디를 꼭 써준다. -->
						<div id="naverIdLogin_loginButton" href="javascript:void(0)">
							<img src="/res/img/naver_join.png" width = 300rem height= 70rem alt="image">
						</div>
					<!-- 카카오 로그인 버튼 -->
					<div onclick="kakaoLogin();" id="kakaoIdJoin_joinButton">
						<a href="javascript:void(0)"> <img
							src="/res/img/kakao_join.png" width=300rem height=70rem
							alt="image">
						</a>
					</div>
					<div class="otherFunctions">
						<div class="moveLoginTitle">이미 회원이신가요?</div>
						<div class="moveLoginPage" onClick="movePage('MoveLoginPage')">로그인</div>
					</div>
 
				<!-- 네이버 스크립트 -->
				<script
					src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js"
					charset="utf-8"></script>

				<script>
					var naverLogin = new naver.LoginWithNaverId({
						clientId : "GgJuJh_y9P_xFKb5fSQj", //내 애플리케이션 정보에 cliendId를 입력해줍니다.
						/* callbackUrl : "http://localhost:80/joincallback.jsp", // 내 애플리케이션 API설정의 Callback URL 을 입력해줍니다. */
						
						callbackUrl : "http://localhost/MoveJoinPage?", // 내 애플리케이션 API설정의 Callback URL 을 입력해줍니다.
						
						isPopup : false,
						callbackHandle : true
					});

					naverLogin.init();

				</script>

<!-- 	네이버는 금지!!!!! -->
					<script type="text/javascript"
						src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js"
						charset="utf-8"></script>
					<script type="text/javascript"
						src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
					<script type="text/javascript">
						var naver_id_login = new naver_id_login(
								"GgJuJh_y9P_xFKb5fSQj",
								"http://localhost/MoveJoinPage?");
						// 네이버 사용자 프로필 조회
						naver_id_login
								.get_naver_userprofile("naverSignInCallback()");
						// 네이버 사용자 프로필 조회 이후 프로필 정보를 처리할 callback function
						function naverSignInCallback() {
							// 접근 토큰 값 출력
							alert(naver_id_login.oauthParams.access_token);
							alert(naver_id_login.getProfileData('email'));
							alert(naver_id_login.getProfileData('nickname'));
							alert(naver_id_login.getProfileData('age'));

							let form = document.getElementById("serverForm");
							form.action = "MoveJoinFormPage";
							form.method = "post";

							form.appendChild(createInput("hidden", "suCode",
									naver_id_login.oauthParams.access_token,
									null, null));
							form.appendChild(createInput("hidden", "suEmail",
									naver_id_login.getProfileData('email'),
									null, null));
							form.appendChild(createInput("hidden",
									"suNickName", naver_id_login
											.getProfileData('nickname'), null,
									null));

							form.submit();
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
										kakaoJoinCtl(response);
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