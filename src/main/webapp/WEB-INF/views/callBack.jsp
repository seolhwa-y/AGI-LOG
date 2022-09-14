<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>네이버 로그인</title>
<script src="/res/js/agiMain.js"></script>
</head>
<body>
<!-- 1. LoginWithNaverId Javscript SDK -->
  <script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
  <script src="https://code.jquery.com/jquery-1.12.1.min.js"></script>
	<script type="text/javascript">
	 var naverLogin = new naver.LoginWithNaverId(
		      {
		        clientId: "GgJuJh_y9P_xFKb5fSQj",
		        callbackUrl: "http://localhost/MoveLoginPage?",
		        isPopup: false,
		        callbackHandle: true
		      }
		    );
		    /* 3. 로그인 정보 초기화 */
		    naverLogin.init();

		    /* 4. 현재 로그인 상태를 확인 */
		    window.addEventListener('load', function () {
		      naverLogin.getLoginStatus(function (status) {
		        if (status) {
		          /* 4-1 로그인 상태인 경우 로그인 실행 함수 호출. */
			          loginCtl();
		        }
		      });
		    });
		    /* 5. 로그인 실행 함수 호출. */
		    function loginCtl() {
		    	let form = document.getElementById("serverForm");
				form.action="NaverLogin";
				form.method="post";
				
				form.appendChild(createInput("hidden","suCode",naverLogin.user.id,null,null));
				form.appendChild(createInput("hidden","suEmail",naverLogin.user.email,null,null));
				form.appendChild(createInput("hidden","suNickName",naverLogin.user.nickname,null,null));
				
				form.submit();
		    }
	</script>
	<form id="serverForm"></form>
</body>
</html>