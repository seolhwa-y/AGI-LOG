<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/joinForm.css">
<script>

	function checkOverlap() {
		//0:? 1:닉네임
		let nickName = document.getElementsByName("suNickName")[1];
		let clientData = "code=1&suNickName="+nickName.value;
		alert(clientData);
		postAjaxJson("CheckPersonalOverlap",clientData,"callBackOverlap");
	}
	function callBackOverlap(ajaxData) {
		if(ajaxData=="ok"){
			alert(ajaxData);
		}
		else {
			alert(ajaxData);
		}
	}
	function next(num) {
		switch (num) {
		case "1":
			let second = document.querySelector(".second");
			second.style.display = "block";
			break;
		case "2":
			document.querySelector(".formOne").style.display = 'none';
			document.querySelector(".formTwo").style.display = 'block';
			break;
		}
	}
	function init() {
		let second = document.querySelector(".second");
		second.style.display = 'none';
		
		let nickName = document.getElementsByName("suNickName");
		if("${socialInfo.suNickName}"!="") {
			nickName[0].value = "${socialInfo.suNickName}";
			nickName[1].value = "${socialInfo.suNickName}";
		}
	}

	function moveLeft() {
		if (index === 0)
			return;
		const carousel = document.querySelector('.listBody');
		const lBtn = document.getElementById("leftMoveBtn");
		const rBtn = document.getElementById("rightMoveBtn");
		let num = 0;
		if (index == 1) {
			lBtn.style.display = 'none';

			lBtn.style.cursor = 'default';
		}
		if (index <= maxSize) {
			rBtn.style.cursor = 'pointer';
			rBtn.style.display = 'block';
		}
		index -= 1;
		num = w * index;
		carousel.style.transform = 'translateX(-' + num + 'rem)';
	}

	function moveRight() {
		const carousel = document.querySelector('.listBody');
		const lBtn = document.getElementById("leftMoveBtn");
		const rBtn = document.getElementById("rightMoveBtn");

		index += 1;
		let num = 0;

		if (index === maxSize) {
			return;
		}
		if (index >= maxSize) {
			index -= 1;
		}
		if (index == maxSize - 1) {
			rBtn.style.display = 'none';
			rBtn.style.cursor = 'default';
		}
		lBtn.style.cursor = 'pointer';
		lBtn.style.display = 'block';
		num = w * index;
		carousel.style.transform = 'translateX(-' + num + 'rem)';

	}
	
	function joinCtl() {
		let form = document.getElementById("serverForm");
		form.action="SocialJoin";
		form.method="post";
		
		let email = document.getElementsByName("suEmail")[0].value;
		let code = document.getElementsByName("suCode")[0].value;
		let name = document.getElementsByName("suName")[0].value; 
		let phone = document.getElementsByName("suPhone")[0].value;
		let nick = document.getElementsByName("suNickName")[1].value;
		
		if(name.length > 11 && name.length == 0){
			alert("이름을 확인해주세요");
			return;
		}
		if(phone.length > 12 && phone.length == 0){
			alert("전화번호를 확인해주세요");
			return;
		}
		if(nick.length > 9 && nick.length == 0){
			alert("닉네임은 8자리까지 가능합니다");
			return;
		}
		form.appendChild(createInput("hidden","suEmail",email,null,null));
		form.appendChild(createInput("hidden","suCode",code,null,null));
		form.appendChild(createInput("hidden","suName",name,null,null));
		form.appendChild(createInput("hidden","suPhone",phone,null,null));
		form.appendChild(createInput("hidden","suNickName",nick,null,null));
		alert(nick);
		form.submit();
	}
</script>


<!-- 	<script type="text/javascript"
		src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js"
		charset="utf-8"></script>
	<script type="text/javascript"
		src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
	<script type="text/javascript">
	var naver_id_login = new naver_id_login("GgJuJh_y9P_xFKb5fSQj",
	"http://localhost/MoveJoinFormPage");
	// 네이버 사용자 프로필 조회
	naver_id_login.get_naver_userprofile("naverSignInCallback()");
	// 네이버 사용자 프로필 조회 이후 프로필 정보를 처리할 callback function
	function naverSignInCallback() { 
	// 접근 토큰 값 출력
	alert(naver_id_login.oauthParams.access_token);
		alert(naver_id_login.getProfileData('email'));
	alert(naver_id_login.getProfileData('nickname'));
	
	let form = document.getElementById("serverForm");
	form.action="MoveJoinFormPage";
	form.method="post";
	
	form.appendChild(createInput("hidden","suCode",naver_id_login.oauthParams.access_token,null,null));
	form.appendChild(createInput("hidden","suEmail",naver_id_login.getProfileData('email'),null,null));
	form.appendChild(createInput("hidden","suNickName",naver_id_login.getProfileData('nickname'),null,null));
	
	 form.submit(); 
	} 
	</script> -->


</head>
<body onload="init()">
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

					<div class="formOne">
						<div class="accessInput-input-group">
							<input class="basicInput" name="suEmail" placeholder="E-mail" value="${socialInfo.suEmail}" readOnly/>
							<button class="checkBtn btn" onClick="next('1')">Continue</button>
						</div>
						<div class="accessInput-input-group second">
							<input class="basicInput" name="suNickName" placeholder="NickName"  value="${socialInfo.suNickName}" readOnly/>
							<button class="checkBtn btn" onClick="next('2')">Next</button>
						</div>
						<input type="hidden" name="suCode" value="${socialInfo.suCode}" readOnly/>
					</div>
				
					<div class="formTwo" style="display:none">
						<div class="accessInput-input-group2">
							<div class="necessaryInfo">
								<span class="necessaryPoint">*</span>이름
							</div>
							<input class="basicInput" name="suName" placeholder="Name" required />
						</div>
						<div class="accessInput-input-group2">
							<div class="necessaryInfo">
								<span class="necessaryPoint">*</span>연락처
							</div>
							<input class="basicInput" name="suPhone" placeholder="Phone Number" required />
						</div>
						<div class="accessInput-input-group2">
							<div class="necessaryInfo">
								<span class="necessaryPoint">*</span>닉네임
							</div>
							<input type="text" class="basicInput" name="suNickName" placeholder="Nickname" onchange="change()"/>
							<button class="checkBtn btn" onclick="checkOverlap()">중복확인</button>
						</div>
						<button class="checkBtn btn joinconfirm" onclick="joinCtl()">가입완료</button>
					</div>
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
	<form id="serverForm"></form>
</body>

</html>