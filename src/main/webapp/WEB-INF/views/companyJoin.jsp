<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>기업 회원가입</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/company.css">
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<!-- 알림창 꾸미기 -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<style>
.accessInput-input-group2 {
 width:32.4rem;
 margin:0 auto;
 position: relative;
}
.passw[type=password]{
	font-family:"Nanum Gothic", sans-serif !important;
	font-weight: bold;
}
</style>
<script>
	/* 주소 검색 API */
	window.onload = function(){
	    document.getElementById("checkCoAddress").addEventListener("click", function(){ //주소입력칸을 클릭하면
	        //카카오 지도 발생
	        new daum.Postcode({
	            oncomplete: function(data) { //선택시 입력값 세팅
	                console.log(data.address);
	                document.getElementsByName("coAddress")[0].value = data.address; // 주소 넣기

	            }
	        }).open();
	    
	    });
	    
		/*let second = document.querySelector(".formTwo");
		second.style.display = 'none';*/
	  }
	
	/* 사업자등록번호 진위확인 API */	
    function checkCoCode(){
        console.log($('[name="coCode"]').val());
        
        var data = {
              "b_no": [$('[name="coCode"]').val()] // 사업자번호 "xxxxxxx" 로 조회 시,
            }; 
          
        $.ajax({
          url: "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=O1knyhqepuJAw4ayaaud9WPTKZtq0t8v8MKi6RidO7aqPOqF1o%2B8NNqgqpPV4%2BG4fqiFdK4RzH0vE%2BV5Viv1%2BQ%3D%3D",  // serviceKey 값을 xxxxxx에 입력
          type: "POST",
          data: JSON.stringify(data), // json 을 string으로 변환하여 전송
          dataType: "JSON",
          contentType: "application/json",
          accept: "application/json",
          success: function(result) {
            console.log(result);
				if(result.data[0].b_stt == "계속사업자"){
	              	console.log(result.data[0].b_stt); // 사업자 유형 "계속사업자"
	              	if(result.data[0].tax_type == "부가가치세 면세사업자"){
	                	console.log(result.data[0].tax_type);  // 사업자 유형 "면세사업자"
	                	document.querySelector(".isCheckCC").innerText = result.data[0].tax_type;
	              	} else { 
	              		document.querySelector(".isCheckCC").innerText = "면세사업자만 가입이 가능합니다.";
	              		console.log("면세 사업자 아님"); 
						$('[name="coCode"]').val('');
	              		return;
	              	}
				} else { 
					console.log("계속 사업자 아님"); 
					document.querySelector(".isCheckCC").innerText = "계속사업자만 가입이 가능합니다.";
					$('[name="coCode"]').val('');
					return;
				}

          },
          error: function(result) {
              console.log(result.responseText); //responseText의 에러메세지 확인
          }
        });
    }
	
	// 문자 체크
	function isChar(text, type) {
		let result;
		
		const largeChar = /[A-Z]/; //대문자 
		const smallChar = /[a-z]/; //소문자
		const num = /[0-9]/; //숫자
		const specialChar = /[!@#$%^&*]/; //특수문자
		
		let typeCount = 0;

		if (largeChar.test(text)) typeCount++; 
		if (smallChar.test(text)) typeCount++;
		if (num.test(text)) typeCount++;
		if (specialChar.test(text)) typeCount++;

		if(type){ //비밀번호 3가지 이상 조합 판단
			result = typeCount >= 3? true : false;
		}else{ //이름 한글로만 이루어졌는지 판단
			result = typeCount == 0? true : false;
		}
		return result;
	}
	
	// 길이 체크
	function isCharLength(text, minimum, maximum) {
		let result = false;
		if (maximum != null) {
			if (text.length >= minimum && text.length <= maximum) result = true;
		}
		else {
			if (text.length >= minimum) result = true;
		}
		return result;
	}
	
	/* 비밀번호 유효성 체크 */
	function checkPassword() {
		let password = document.getElementsByName("coPassword")[0];
		
		// 1. 문자 
		if(isChar(password.value, true)) {
			// 2. 길이 
			if(isCharLength(password.value, 8, 15)) {
				document.querySelector(".CheckPW").innerText = "올바른 비밀번호 입니다.";
			} else {
				document.querySelector(".CheckPW").innerText = "비밀번호는 8자리 이상이어야 합니다.";
	
				return;
			}
		} else {
			document.querySelector(".CheckPW").innerText = "패스워드는 영문 대소문자, 숫자, 특수문자 중 3가지 이상의 문자를 사용하셔야 합니다.";
			
			return;
		}
	}
	
	/* 비밀번호 유효성 체크, 비밀번호 일치 체크 */
	function isPassword() {
		document.getElementsByName("checkCoPassword")[0].addEventListener("keyup", function(){
			let password = document.getElementsByName("coPassword")[0];
			let isPassword = document.getElementsByName("checkCoPassword")[0];
			if(password.value == isPassword.value) {
				document.querySelector(".isCheckPW").innerText = "비밀번호 일치";
			} else {
				document.querySelector(".isCheckPW").innerText = "비밀번호 일치하지 않음";
			}
		})
	}
	
	/* 기업회원 회월가입 완료 */
	function companyJoin(action) {
		
		// 상호 체크 
		let coName = document.getElementsByName("coName")[0];
		if(isCharLength(coName.value, 2)){
			console.log("상호 2자리 이상 확인 완료");
		} else {
			swal("경고", "상호를 다시 입력해주세요!", "warning", { button: "확인"});
			coName.value = "";
			coName.focus();
			return;
		}
		
		// 다시 사업자 체크
		checkCoCode();
		// 다시 비밀번호 체크
		checkPassword();
		
		let isFile = document.getElementsByName("coProfile")[0];
		let file = document.getElementsByName("file")[0];
		let form = document.getElementById("serverForm");
		
		if(isFile.value!=""){
			//파일 올렸을 때
			form.appendChild(file);
			form.enctype = "multipart/form-data";
		}
		
		
		
	 	form.action = action;
		form.method = "post";
		form.submit();
	}
	
	function next(num){
		let formOne = document.getElementById("formOne");
		let formTwo = document.getElementById("formTwo");
		
		if(num == "1"){
			formOne.style.display="none";
			
			formTwo.style.display="block";
			
		}else{
			formOne.style.display="block";
			
			formTwo.style.display="none";
			
		}
	}
	
	//프로필사진 추가 ::  hidden처리한 input type="file" 연결
	function onClickUpload() {
			// 파일 선택하는 인풋 연결
			let imgInput = document.getElementsByName("file")[0];
			imgInput.click();
	}
	function submitPhoto(){
		//올린 파일 이름 찍어주기
		let imgInput = document.getElementsByName("file")[0].value;
		let coProfile = document.getElementsByName("coProfile")[0];
		coProfile.value = imgInput;
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
			<div id="logo" onclick="movePage('MoveReservationManagement')"><span id="txt">아기-로그</span>
				<img src="/res/img/logo.png" alt="imgages">
			</div>
			<div id="mainMenuArea">
				<ul id="mainMenuList">
					<li class="mainMenu" onclick="movePage('MoveDoctorManagement')">의사관리</li>
					<li class="mainMenu" onclick="movePage('MoveReservationManagement')">예약관리</li>
					<li class="mainMenu" onclick="movePage('IntoCheckDoctor')">환자관리</li>
				</ul>
			</div>
		</div>
		<div id="middle">
			<div id="rightArea" class="scrollBar">
				<form id = "serverForm">
					<div class="fullJoin">
						<div class="joinTitle">회원가입</div>
						<div class="smallJoinTitle">기업 회원가입</div>
							<div class="formOne" id="formOne">		
									<div class = "accessInput-input-group">
										<input type = "text" name = "coName" class="basicInput" placeholder="상호명"/>
									</div>
									<div class = "accessInput-input-group">
										<input type = "text" name = "coCode" class="basicInput" placeholder="사업자등록번호">
					  		  			<input type = "button" value="진위확인" onClick = "checkCoCode()" class="checkBtn btn"/>
									</div>
									<div class = "isCheckCC">
										<!-- 비밀번호 동일한지 체크하여 보여줌 -->
									</div>
									<div class = "accessInput-input-group">
										<input type = "text" name = "coAddress" class="basicInput" placeholder="주소" readonly/>
					    				<input type = "button" value="주소검색" id = "checkCoAddress" class="checkBtn btn">
									</div>
									<div class = "accessInput-input-group">
										<input type="text" name = "coManagerCode" class="basicInput" placeholder="관리자 코드">
										<input class="checkBtn btn form" onClick="next('1')" value="Next">
									</div>
							</div>		
									
									
									
									
							<div class="formTwo" id="formTwo" style="display:none;">		
									<div class = "accessInput-input-group2">
										<input type = "password" name = "coPassword" onkeyup = "checkPassword()" class="basicInput passw" placeholder="비밀번호">
									</div>
									<div class = "CheckPW">
										<!-- 비밀번호 유효성 체크하여 보여줌 -->
									</div>
									<div class = "accessInput-input-group2">
										<input type = "password" name = "checkCoPassword" onkeyup = "isPassword()" class="basicInput passw" placeholder="비밀번호 확인">
									</div>
									<div class = "isCheckPW">
										<!-- 비밀번호 동일한지 체크하여 보여줌 -->
									</div>
									<div class = "accessInput-input-group2">
										<input type = "text" name = "coPhone" class="basicInput" placeholder="연락처">
									</div>
									<div class = "accessInput-input-group2">
										<input type = "text" name = "coEmail" class="basicInput" placeholder="이메일">
									</div>
									<div class = "accessInput-input-group2">
										<input type = "text" name = "coProfile" placeholder="프로필사진" class="basicInput" onclick="onClickUpload()">
									</div>
										<input style="visibility: hidden;" type="file" name="file" onchange="submitPhoto()" accept="image/*" readonly>
									<div>
										<input type = "button" class="checkBtn btn form" onClick="next('2')" value="Previous">
										<input type = "button" value = "회 원 가 입" onclick="companyJoin('CompanyJoin')" class="checkBtn btn joinconfirm"> 
									</div>
							</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>