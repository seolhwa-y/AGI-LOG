<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/joinForm.css">
<script>
	/* 주소 검색 API */
	function checkAddress() {
	        //카카오 지도 발생
	        new daum.Postcode({
	            oncomplete: function(data) { //선택시 입력값 세팅
	                console.log(data.address);
	                document.getElementsByName("suAddress")[0].value = data.address + data.bname; // 주소 넣기
	            }
	        }).open();
	} 

	let check = false;
	function checkOverlap() {
		//0:? 1:닉네임
		let nickName = document.getElementsByName("suNickName")[1];
		let clientData = "code=1&suNickName="+nickName.value;
		postAjaxJson("CheckPersonalOverlap",clientData,"callBackOverlap");
	}
	
	function callBackOverlap(ajaxData) {
		if(ajaxData=="ok"){
			alert("사용가능");
			check = true;
		}
		else {
			alert(ajaxData);
			check = false;
		}
	}
	function change() {
		check = false;
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
		let address = document.getElementsByName("suAddress")[0].value;
		
		if(name.length > 11 && name.length == 0){
			alert("이름은 10자 이내로 입력해주세요");
			return;
		}
		if(phone.length > 12 && phone.length == 0){
			alert("전화번호를 확인해주세요");
			return;
		}
		if(nick.length > 9 && nick.length == 0){
			alert("닉네임은 8자리까지 입력 가능합니다");
			return;
		}
		if(!check) {
			alert("닉네임 중복확인을 해주세요");
			return;
		}
		if(address == null){
			alert("주소를 입력해주세요.");
			return;
		}
		form.appendChild(createInput("hidden","suEmail",email,null,null));
		form.appendChild(createInput("hidden","suCode",code,null,null));
		form.appendChild(createInput("hidden","suName",name,null,null));
		form.appendChild(createInput("hidden","suPhone",phone,null,null));
		form.appendChild(createInput("hidden","suNickName",nick,null,null));
		form.appendChild(createInput("hidden","suAddress",address,null,null));
		
		alert(nick);
		
		form.submit();
	}
</script>
</head>
<body onload="init()">
	<div id="background">
		<div id="top">
			<div id="accessArea">
				<span onclick="movePage('MoveLoginPage')">로그인</span> <span
					onclick="movePage('MoveJoinPage')">회원가입</span> <span
					onclick="movePage('MoveCompanyLoginPage')">기업회원</span>
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
						<div class = "accessInput-input-group2">
							<div class="necessaryInfo">
								<span class="necessaryPoint">*</span>주소
							</div>
							<input type = "text" name = "suAddress" class="basicInput" placeholder="주소"/>
					    	<input type = "button" value="주소검색" id = "checkSuAddress" class="checkBtn btn" onclick = "checkAddress()">
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