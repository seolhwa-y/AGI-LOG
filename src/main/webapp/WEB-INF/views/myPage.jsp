<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<style>
	.mBtnP {
    height: 2.3rem;
    width: 8rem;
    box-shadow: 0px 5px 0px 0px rgb(239 157 171);
    background-color: rgb(255, 194, 204);
    font-size: 1.1rem;
    color: dimgray;
	}

	.mBtnP:hover {
		background-color: rgb(255, 234, 238);
	}

	.mBtnG {
		height: 2.3rem;
		width: 8rem;
		box-shadow: 0px 5px 0px 0px rgb(182, 182, 182);
		font-size: 1.1rem;
		color: dimgray;
		background: gainsboro;
	}

	.mBtnG:hover {
		background-color: rgb(255, 234, 238);
	}
	.mBtnX{    
		color: dimgray;
   		background: gainsboro;
   		height: 2.3rem;
   	 	width: 13rem;
   	 	box-shadow: 0px 5px 0px 0px rgb(182 182 182);
    	font-size: 1.1rem;
   		
	}
	#middle{
		position: relative;
	}
	#line{    
		position: relative;
		float: left;
		border-left: 4px solid #C0C0C0;
		height: 60%;
		left: 50%;
		margin-top: 8rem;
	}
	#left{
		position: absolute;
		float: left;
		height: 75%;
		width: 40%;
		margin: 5rem;
	}
	#right{
	    position: relative;
	    float: right;
	    height: 75%;
	    width: 40%;
	    margin: 5rem;
		
	}
	.west{
		height: 100%;
		width: 40%;
		float: left;
	}
	.sideWest{
		position: absolute;
    	margin-top: -11%;
    	margin-left: 4%;
	}
	.east{
		height: 100%;
		width: 57.5%;
		float: right;
	}
	.profile{
		border:  1px solid #C0C0C0;
		width: 90%;
		height: 36%;
		border-radius: 100px;
		margin: 3% 3%;
		margin-bottom: 17%;
	}
	.subBtn{
		margin-top: 15%;
		margin-left: 19%;
	}
	.tableDiv{
		width: 100%;
		height: 60%;
		font-size: 1.7rem;
		margin-top: 1.3rem;
		
	}
	table{
		border-spacing: 18px;
		width: 100%;
 	 	border-collapse: separate;
 	 	color:#616161;
	}
	table td {
		width: 80px;
	}
	.moveLeft{
    float: left;
    margin-top: 40%;
    left: -3rem;
    font-size: 3rem;
    position: absolute;
    cursor: pointer;
    color : #616161;
    display:none;
	}
	.moveRight{
	float: right;
    margin-top: -57%;
    font-size: 3rem;
    cursor: pointer;
    color : #616161;
    display:none;
	}
	.cover1{
	background-color: #fff1f1;
    width: 4rem;
    height: 4rem;
    position: absolute;
    top: 40%;
    left: -8%;
    z-index: 2;
	}
	.cover2{    
	
	background-color: #fff1f1;
    width: 4rem;
    height: 4rem;
    position: absolute;
    top: 40%;
    right: -5%;
	}
	.insBabyTable{
		margin-left: 11%;
		margin-top: 3rem;
		font-size: 1.5rem;
		
	}
	.tdValue{
		text-align: left;
		padding-top: 4%;
	}
	.tdName{
		padding-top: 4%;
		text-align: right;
	}
	.modal{
		color: dimgray;
	}
	.name{
		font-size: 1.4rem;
		margin: 1%;
		color: dimgray;
	}

	.modal_foot{
 	position: relative;
    display: flex;
    margin: 13% auto;
    justify-content: space-between;
    width: 80%;
	}
	.modal_head{
		font-size: 3rem;
		height: 8%;
	}
	.modal_content{
		height: 70%;
	}
	.close{
		float:right;
	}
	.dateInput{
		width: 70%;
	}
	.editImage{
		width: 25%;
		height: 10%;
		position: relative;
		bottom: 17%;
		left: 68%;
		background-color:#fff1f1;
		border-radius: 400px;
	}
	.editImage:active{
		transform: translate(0px, 1px);
    -webkit-transform: translate(0px, 3px);
	}
	.profileImage{
	max-width: 100%;
    max-height: 100%;
    width: 100%;
    border-radius: 100px;}
    
    svg:not(:host).svg-inline--fa, svg:not(:root).svg-inline--fa {
    overflow: visible;
    box-sizing: content-box;
    background: none;
	}
	.theme{
	text-align: center;
	border-radius: 100px;
    border: 3px solid lightgray;
    width: 12%;
    height: 60%;
    display: inline-block;
	margin-left: 3%;
	}
	#themeList{
	order: 1px solid #000000;
    width: 100%;
    height: 30%;
    margin-left: 8%;
    display: flex;
    margin-top: 30%;
	}
	.checked::after{
	content: "✔";
    font-size: 275%;
    line-height: 166%;
	}
	#themeModalFoot{
	position: relative;
    display: flex;
    margin: 13% auto;
    justify-content: space-between;
    width: 80%;
    top: -30%;
    }
</style>
<!-- 카카오 스크립트 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<!-- 네이버 스크립트 -->
<script	src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
<script>
//길이 체크
function isCharLengthCheck(text, minimum, maximum) {
	let result = false;
	if (maximum != null) {
		if (text.length >= minimum && text.length <= maximum) result = true;
	}
	else {
		if (text.length >= minimum) result = true;
	}
	return result;
}
Kakao.init('2afdabad57ed92e1cc9de5bd4baed321');
function getInfo() {

	if("${message}"!= ""){
		alert("${message}");
	}
	
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
	

	
	//처음화면에서 필요없는 버튼숨기기
	let editConfirmBtn1 = document.getElementById("editConfirmBtn1");
	let editConfirmBtn2 = document.getElementById("editConfirmBtn2");
	
	editConfirmBtn1.style.display = "none";
	editConfirmBtn2.style.display = "none";
	
	//back에서 가져온 정보로 화면에 뿌려줌 => 부모정보, 아이정보
	let pImage = document.getElementById("pImage");
	let parentName = document.getElementById("parentName");
	let parentNickName = document.getElementById("parentNickName");
	
	pImage.innerHTML = "<img class='profileImage' src=\'"+"${mypageInfo.suPhoto}"+"\'>";
	parentName.innerText="${mypageInfo.suName}";
	parentNickName.innerText="${mypageInfo.suNickName}";
	
	let bImage = document.getElementById("bImage");
	let babyName = document.getElementById("babyName");
	let babyBirthDay = document.getElementById("babyBirthday");
	let babyHeight = document.getElementById("babyHeight");
	let babyWeight = document.getElementById("babyWeight");

	//아이사진이 없으면 안띄워줌
	if(!"${mypageInfo.babyList[0].babyPhoto}" == ""){
		bImage.innerHTML = "<img class='profileImage' src='"+"${mypageInfo.babyList[0].babyPhoto}"+"'>";
	}else{
		bImage.innerHTML ="";
	}
	babyName.innerText="${mypageInfo.babyList[0].babyName}";
	babyBirthDay.innerText="${mypageInfo.babyList[0].babyBirthday}";
	babyHeight.innerText="${mypageInfo.babyList[0].babyHeight}"+"cm";
	babyWeight.innerText="${mypageInfo.babyList[0].babyWeight}"+"kg";
	
	//내 밑으로 두아이 이상이라면 '>' 버튼 보여줌
	if("${mypageInfo.babyList.size()}">1){
		let getBabyInfo = document.getElementById("getBabyInfo");
		getBabyInfo.style.display="block";
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
//마이페이지 다른아이 프로필로 변경 :: '>' 가장 처음 클릭
function getBabyInfo(){
	// 맨처음 아이에서 다음아이 프로필 넘기는 버튼 눌렀을 때.한번만 사용함 이때 서버에가서 아이정보 json으로 다 가져옴
	let getBabyInfo = document.getElementById("getBabyInfo");
	getBabyInfo.remove();
	
	postAjaxJson("GetBabyInfo","","callbackGetBabyInfo");
}
//마이페이지 다른아이 프로필로 변경 :: 콜백함수
function callbackGetBabyInfo(jsonData){
	//위 함수에 상응하는 콜백함수
	
	// 첫아이는 <버튼을,마지막 아이는 >버튼을 숨겨줘야 하기 총 아이가 몇명인지 구함 
	// babyNum전역변수로 올린것도 같은이유.
	let babyCount = "${mypageInfo.babyList.size()}";
	
	// 가져온 모든 아이 정보가 담긴 배열을 전역변수에 저장.
	babyInfo = JSON.parse(jsonData);
	
	babyNum++;
	
	let babyImage = document.getElementById("bImage");
	let babyName = document.getElementById("babyName");
	let babyBirthDay = document.getElementById("babyBirthday");
	let babyHeight = document.getElementById("babyHeight");
	let babyWeight = document.getElementById("babyWeight");
	
	//아이프로필사진이 없다면 사진부분에 아무것도 띄워주지 않음
	if(!babyInfo.babyList[babyNum].babyPhoto == ""){
		babyImage.innerHTML = "<img class=\"profileImage\" src=\""+babyInfo.babyList[babyNum].babyPhoto+"\" >";
	}else{
		babyImage.innerHTML = "";
	}
	
	babyName.innerText = babyInfo.babyList[babyNum].babyName;
	babyBirthDay.innerText=babyInfo.babyList[babyNum].babyBirthday;
	babyHeight.innerText=babyInfo.babyList[babyNum].babyHeight;
	babyWeight.innerText=babyInfo.babyList[babyNum].babyWeight;
	
	let num = babyNum;
	num++;

	
	if(babyCount>num){
		// 왼쪽 div 보여지게하고 오른쪽 div는 삭제후 새로 만들어서 넣어주기
		let minus = document.getElementById("minus");
		minus.style.display = "block";
		
		let plus = document.getElementById("plus");
		plus.style.display = "block";
	}else{
		// 오른쪽 div없어지고 왼쪽 div만 보여지게 함
		let minus = document.getElementById("minus");
		minus.style.display = "block";
		let plus = document.getElementById("plus");
		plus.style.display = "none";
	}
}
//마이페이지 현재보이는 아이프로필을 다른아이로 변경 :: >,<버튼 클릭
function changeBaby(number){
	
	let size = "${mypageInfo.babyList.size()}"; 

	if(number=="1"){ //플러스'>' 눌렀을 때
		
		if(babyNum>=0){
			babyNum++;
			
			let babyImage = document.getElementById("bImage");
			let babyName = document.getElementById("babyName");
			let babyBirthDay = document.getElementById("babyBirthday");
			let babyHeight = document.getElementById("babyHeight");
			let babyWeight = document.getElementById("babyWeight");
			
			if(!babyInfo.babyList[babyNum].babyPhoto == ""){
				babyImage.innerHTML = "<img class=\"profileImage\" src=\""+babyInfo.babyList[babyNum].babyPhoto+"\" >";
			}else{
				babyImage.innerHTML = "";
			}

			babyName.innerText = babyInfo.babyList[babyNum].babyName;
			babyBirthDay.innerText=babyInfo.babyList[babyNum].babyBirthday;
			babyHeight.innerText=babyInfo.babyList[babyNum].babyHeight+"cm";
			babyWeight.innerText=babyInfo.babyList[babyNum].babyWeight+"kg";
			
			let num = babyNum;
			num++;
			
			//마지막 아기일때 플러스'>'버튼만 막아주면 됨
			
			if(size == num){
				let plus = document.getElementById("plus");
				plus.style.display="none";
				let minus = document.getElementById("minus");
				minus.style.display="block";
			}else{
				let plus = document.getElementById("plus");
				plus.style.display="block";
				let minus = document.getElementById("minus");
				minus.style.display="block";
			}
		}
		
	}else{ //마이너스'<' 눌렀을떄
		babyNum--;
	
		let babyImage = document.getElementById("bImage");
		let babyName = document.getElementById("babyName");
		let babyBirthDay = document.getElementById("babyBirthday");
		let babyHeight = document.getElementById("babyHeight");
		let babyWeight = document.getElementById("babyWeight");

		if(!babyInfo.babyList[babyNum].babyPhoto == ""){
			babyImage.innerHTML = "<img class=\"profileImage\" src=\""+babyInfo.babyList[babyNum].babyPhoto+"\" >";
		}else{
			babyImage.innerHTML = "";
		}		
		babyName.innerText = babyInfo.babyList[babyNum].babyName;
		babyBirthDay.innerText=babyInfo.babyList[babyNum].babyBirthday;
		babyHeight.innerText=babyInfo.babyList[babyNum].babyHeight+"cm";
		babyWeight.innerText=babyInfo.babyList[babyNum].babyWeight+"kg";
		
		//가장 첫번째 아기일 때 <버튼만 막아주면 됨
		if(babyNum==0){
			let plus = document.getElementById("plus");
			plus.style.display="block";
			let minus = document.getElementById("minus");
			minus.style.display="none";
		}else{
			let plus = document.getElementById("plus");
			plus.style.display="block";
			let minus = document.getElementById("minus");
			minus.style.display="block";
		}
	}

}
let babyInfo;
let babyNum = 0;
//부모프로필변경 :: 부모쪽 프로필변경 클릭 :: 관련없는 다른 버튼들 지워주기
function changeParentProfile(){
	//부모 프로필 수정버튼 눌렀을 때
	let parentNickName = document.getElementById("parentNickName");
	
	//수정가능한 부분을 인풋박스로 바꿔줌 
	parentNickName.innerHTML = "<input type=\"text\" name='suNickName' class=\"profileInput\" placeholder=\"${mypageInfo.suNickName}\">";
	
	// 아이쪽 <,>버튼을 숨겨줌 0:숨김 1:나타남
	cover("0");
	
	// 부모 프로필 수정과 관련되지 않은 버튼들 없애주기
	let editConfirmBtn1 = document.getElementById("editConfirmBtn1");
	let leftWest = document.getElementById("leftWest");
	let rightWest = document.getElementById("rightWest");	

	editConfirmBtn1.style.display = "block";
	leftWest.style.display = "none";
	rightWest.style.display = "none";
}
//아이프로필변경 :: 수정완료버튼 클릭
function updateProfile(){
	let nickName = document.getElementsByName("suNickName")[0];
	let form = document.getElementById("serverForm");
	
	if(nickName.value == ""){
		alert("바꾸실 닉네임을 입력하세요");
		nickName.focus();
	}else if(!isCharLengthCheck(nickName.value,"1","20")){
		alert("닉네임은 1자~20자 사이로 입력해주세요")
		nickName.focus();
	}
	else{
		form.appendChild(nickName);
		form.action = "UpdateProfile";
		form.method = "post";
		
		form.submit();
	}
}
//부모 프로필 변경 :: 취소 :: 수정하지않고 취소 눌렀을 때 텍스트박스 = > 원상복구
function parentEditCancel(){
	let editConfirmBtn1 = document.getElementById("editConfirmBtn1");
	let leftWest = document.getElementById("leftWest");
	let rightWest = document.getElementById("rightWest");
	
	editConfirmBtn1.style.display = "none";
	leftWest.style.display = "block";
	rightWest.style.display = "block";
	// <,> 보여주기
	cover("1");
	
	let parentNickName = document.getElementById("parentNickName");
	
	parentNickName.innerText="${mypageInfo.suNickName}";
}
let babyName = "";
let babyBirthday = "";
//아이프로필변경 :: 아이쪽 프로필변경 클릭 :: 관련없는 다른 버튼들 지워주기
function changeBabyProfile(){

	let editConfirmBtn2 = document.getElementById("editConfirmBtn2");
	let leftWest = document.getElementById("leftWest");
	let rightWest = document.getElementById("rightWest");
	
	editConfirmBtn2.style.display = "block";
	leftWest.style.display = "none";
	rightWest.style.display = "none";
	// <,> 가려주기
	cover("0");
	
	// 현재 저장된 프로필 값 input박스 value로 넣어 사용자에게 보여주기
	//let babyNameTd = document.getElementById("babyName");
	let babyBirthdayTd = document.getElementById("babyBirthday");
	
	//babyName = babyNameTd.innerText;
	babyBirthday = babyBirthdayTd.innerText;
	//babyNameTd.innerHTML = "<input type=\"text\" name='babyName' class=\"profileInput dateInput\" value=\""+babyName+"\">";
	babyBirthdayTd.innerHTML = "<input type=\"date\" name='babyBirthday' class=\"profileInput dateInput\" value=\""+babyBirthday+"\">";

}
//아이프로필변경 :: 수정완료버튼 클릭
function changeBabyInfo(){
	let babyNumber;
	let babyNameInput = document.getElementsByName("babyName")[0];
	let babyBirthdayInput = document.getElementsByName("babyBirthday")[0];
	let form = document.getElementById("serverForm");
	

	if(babyInfo == null){
		babyNumber = "${mypageInfo.babyList[0].babyCode}";
		//babyName = "${mypageInfo.babyList[0].babyName}";
		
	}else{
		babyNumber = babyInfo.babyList[babyNum].babyCode;
		//babyName = babyInfo.babyList[babyNum].babyName;
	}
	
	/*if(babyNameInput.value==""){
		alert("아이 이름을 작성해 주세요");
		return;
	}*/
	
	form.appendChild(createInput("text","babyCode",babyNumber,"",""));
	form.appendChild(babyBirthdayInput);
	//form.appendChild(babyNameInput);
	
	form.action = "ChangeBabyInfo";
	form.method = "post";
	
	form.submit();
}

//아이 프로필 변경 :: 취소 :: 수정하지않고 취소 눌렀을 때 텍스트박스 = > 원상복구
function babyEditCancel(){
	
	let editConfirmBtn2 = document.getElementById("editConfirmBtn2");
	let leftWest = document.getElementById("leftWest");
	let rightWest = document.getElementById("rightWest");
	
	editConfirmBtn2.style.display = "none";
	leftWest.style.display = "block";
	rightWest.style.display = "block";
	// <,> 보여주기
	cover("1");
	
	let babyBirthdayTd = document.getElementById("babyBirthday");
	
	babyBirthdayTd.innerText = babyBirthday;
}
//아이정보 <왼쪽 >오른쪽 버튼 가려주기
function cover(number){
	let cover1 = document.getElementById("cover1");
	let cover2 = document.getElementById("cover2");
	
	if(number==1){
		cover1.style.display = "none";
		cover2.style.display = "none";
	}else{
		cover1.style.display = "block";
		cover2.style.display = "block";
	}
}

//아이추가 :: 모달창 구성
function insertBabyModal(){
	let modal = document.querySelector(".modal");
	let modalHead = document.querySelector(".modal_head");
	let modalContent = document.querySelector(".modal_content");
	let modalFoot = document.querySelector(".modal_foot");


	modal.style.display = "block";
	modalHead.innerHTML = "<div class='insBabyTitle'>아이 추가</div>";
	modalContent.innerHTML = "<table class='insBabyTable'><tr><td class='tdName'><span class='name'>아이 이름 :</span></td><td class='tdValue'><input class='mBasicInput name'type='text' name='babyName' placeholder='이름' required></td></tr>"
							+"<tr><td class='tdName'><span class='name'>출생일 :</span></td><td class='tdValue'><input class='mBasicInput name' type='date' name='babyBirthday' placeholder='출생일' required></td></tr>"
							+"<tr><td class='tdName'><span class='name'>키 :</span></td><td class='tdValue'><input class='mMiniInput name'type='text' name='babyHeight' placeholder='cm' required></td></tr>"
							+"<tr><td class='tdName'><span class='name'>몸무게 :</span></td><td class='tdValue'><input type='text' class='mMiniInput name' name='babyWeight' placeholder='kg' required></td></tr></table>";
	modalFoot.innerHTML = "<button class='mBtnX' onclick='modalClose()'>취소</button><button class='mBtnO' onclick='insertBabyInfo()'>추가</button>";


}
//아이 추가 :: 아이 추가 제출
function insertBabyInfo(){
	let form = document.getElementById("InsertBabyInfo");
	
	let babyName = document.getElementsByName("babyName")[0];
	let babyBirthday = document.getElementsByName("babyBirthday")[0];
	let babyHeight = document.getElementsByName("babyHeight")[0];
	let babyWeight = document.getElementsByName("babyWeight")[0];
	
	//이름 글자 제한
	if(!isCharLengthCheck(babyName.value,"1","20")){
		alert("아이 이름은 1자~20자 사이로 입력해 주세요")
		babyName.focus();
		return;
	}
	
	form.action = "InsertBabyInfo";
	form.method = "post";
	
	form.submit();
	modalClose();
}
//모달닫기
function modalClose(){
	let modal = document.querySelector(".modal");
	let modalHead = document.querySelector(".modal_head");
	let modalContent = document.querySelector(".modal_content");
	let modalFoot = document.querySelector(".modal_foot");
	
	modal.style.display="none";
	modalHead.innerHTML = "";
	modalContent.innerHTML = "";
	modalFoot.innerHTML = "";
}
//프로필사진 변경 :: 카메라모양하고 hidden처리한 input type="file" 연결
function onClickUpload(num) {
	if(num == "1"){
		//부모프사 :: 파일 선택하는 인풋 연결
		let imgInput = document.getElementsByName("file")[0];
		imgInput.click();
	}else{
		//아이프사 :: 파일 선택하는 인풋 연결
		let imgInput = document.getElementsByName("file")[1];
		imgInput.click();
	}
}
//프로필사진 변경 :: 파일 선택하고 해당 파일, 필요한 정보 서버로 전송
function submitPhoto(num){
	if(num == "1"){
		//부모프사
		let imgInput = document.getElementsByName("file")[0];
		let form = document.getElementById("clientData");
		
		//폼에 담아 서버전송
		form.appendChild(imgInput);
		form.appendChild(createInput("hidden","suCode","${mypageInfo.suCode}",null,null));
		form.action = "UploadParentImage";
		form.method = "post";
		form.enctype = "multipart/form-data";
		form.submit();
	}else{
		//아이프사
		let imgInput = document.getElementsByName("file")[1];
		let form = document.getElementById("clientData");
		
		//아이코드
		if(babyInfo == null){
			babyCode = "${mypageInfo.babyList[0].babyCode}";
		}else{
			babyCode = babyInfo.babyList[babyNum].babyCode;
		}
		//폼에 담아 서버전송
		form.appendChild(imgInput);
		form.appendChild(createInput("hidden","babyCode",babyCode,"",""));
		form.action = "UploadBabyImage";
		form.method = "post";
		form.enctype = "multipart/form-data";
		form.submit();
	}
}
/* 테마변경 */
function clickChangeTheme(){
	let themeModal = document.getElementById("changeThemeModal")
	themeModal.style.display = "block";
}
let theme ="";
function selectTheme(num){
	let themeList = document.getElementById("themeList");
	let child = themeList.childNodes;
	let modalFoot = document.getElementById("themeModalFoot");
	
	for(idx=0;idx<10;idx++){
		if(idx == num){
			if(child[num].classList.contains("checked")){
				alert("시발");
				child[num].classList.remove("checked");
				theme = num;
				modalFoot.innerHTML="<button class=\'mBtnX\' onclick=\'themeModalClose()\'>취소</button><button class=\'mBtnO\' onclick=\'changeTheme(\'0\')\'>변경</button>";
			}else{
				child[num].classList.add("checked");
				modalFoot.innerHTML="<button class=\'mBtnX\' onclick=\'themeModalClose()\'>취소</button><button class=\'mBtnO\' onclick=\'changeTheme("+num+")\'>변경</button>";
			}
		}else{
			child[idx].className="theme";
		
		}
	}
}
function themeModalClose(){
	let themeModal = document.getElementById("changeThemeModal")
	themeModal.style.display = "none";
	let modalFoot = document.getElementById("themeModalFoot");
	//현재테마로 설정 변경하고
	
	//버튼도 다시 0으로 설정
	modalFoot.innerHTML="<button class=\'mBtnX\' onclick=\'themeModalClose()\'>취소</button><button class=\'mBtnO\' onclick=\'changeTheme(\'0\')\'>변경</button>";
}
function changeTheme(num){
	let clientData = "";
	switch(num){
	case 1 : //핑크
		clientData = "suTheme=pink";
		break;
	case 3 : //노랑
		clientData = "suTheme=yellow";
		break;
	case 5 : //파랑
		clientData = "suTheme=blue";
		break;
	case 7 : //보라
		clientData = "suTheme=purple";
		break;
	case 9 : //초록
		clientData = "suTheme=green";
		break;
	}
	postAjaxJson("ChangeTheme",clientData,"callBackTheme");
	
}
function callBackTheme(theme){
	alert(theme);
	/*
	switch(theme){
	case pink : //핑크
		clientData = "suTheme=pink";
		break;
	case yellow : //노랑
		clientData = "suTheme=yellow";
		break;
	case blue : //파랑
		clientData = "suTheme=blue";
		break;
	case purple : //보라
		clientData = "suTheme=purple";
		break;
	case green : //초록
		clientData = "suTheme=green";
		break;
	}*/
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
			<div id="left">
				<div  class="west">
					<div class="profile" id="pImage"></div>
					<i class="fa-solid fa-camera editImage mUpdBtn" id="parentImage" onclick="onClickUpload('1')" ></i>					
					<div id="leftWest"  class="sideWest">
						<div class="subBtn">
							<button class="mBtnP btn" onclick="changeParentProfile()">프로필 수정</button>
						</div>
						<div class="subBtn">
							<button class="mBtnG btn" onclick="clickChangeTheme()">테마 변경</button>
						</div>
					</div>
				</div>
				<div id="leftEast" class="east">
					<div class="tableDiv">
						<table >
							<tr>
								<td><span >이름</span></td>
								<td colspan="2" id="parentName"></td>
							</tr>
							<tr>
								<td><span >닉네임</span></td>
								<td colspan="2" id="parentNickName"></td>
							</tr>
						</table>
					</div>
					<diV id="editConfirmBtn1">
					<button class="subBtn mBtnG btn" onclick="parentEditCancel()">취소</button>
					<button class="mBtnP btn" onclick="updateProfile()">수정완료</button>
					</div>
				</div>
			</div>

			<div id="line"></div>
			
			<div id="right">
				<div class="cover1" id="cover1"style="display:none;" ></div>
				<div class="moveLeft" id="minus" onclick="changeBaby('-1')">◁</div>
				<div class="west">
					<div class="profile" id="bImage"></div>
					<i class="fa-solid fa-camera editImage mUpdBtn" id="babyImage" onclick="onClickUpload('2')" ></i>					
					<div id="rightWest" class="sideWest">
						<div class="subBtn">
							<button class="mBtnP btn" onclick="changeBabyProfile()" >프로필 수정</button>
						</div>
						<div class="subBtn">
							<button class="mBtnG btn" onclick="insertBabyModal()">아이 추가</button>
						</div>
					</div>
				</div>
				<div id="rightEast" class="east">
					<div class="tableDiv">
						<table >
							<tr>
								<td><span >이름</span></td>
								<td colspan="2" id="babyName"></td>
							</tr>
							<tr>
								<td><span>생일</span></td>
								<td colspan="2" id="babyBirthday"></td>
		
							</tr>
							<tr>
								<td><span>키</span></td>
								<td colspan="2" id="babyHeight"></td>
							</tr>
							<tr>
								<td><span>몸무게</span></td>
								<td colspan="2" id="babyWeight"></td>
							</tr>
						</table>
					</div>
					<div id="editConfirmBtn2">
						<button class="subBtn mBtnG btn" onclick="babyEditCancel()">취소</button>
						<button class="mBtnP btn" onclick="changeBabyInfo()">수정완료</button>
					</div>
				</div>
				<div class="cover2" id="cover2" style="display:none;"></div>
				<div class="moveRight" id="getBabyInfo" onclick="getBabyInfo()">▷</div>
				<div class="moveRight" id="plus" style="display:none;" onclick="changeBaby('1')">▷</div>
			</div>
		</div>
		<div class="modal">
            <div class="modal_body">
            	<form id="InsertBabyInfo">
				<div class="modal_head">
					<i class="fa-solid fa-xmark closeBtn editBtn"></i><br />
				</div>
				<div class="modal_content"></div>
				<div class="modal_foot"></div>
				</form>
            </div>
        </div>
        <div class="modal" id="changeThemeModal" style="display : none;" >
            <div class="modal_body">
				<div class="modal_head">테마선택
				</div>
				<div class="modal_content">
					<div id ="themeList">
					<div class="theme" style="background-color:#fff1f1;" onclick="selectTheme('1')">&nbsp;</div>
					<div class="theme" style="background-color:#FAF4C0;" onclick="selectTheme('3')">&nbsp;</div>
					<div class="theme" style="background-color:#D4F4FA;" onclick="selectTheme('5')">&nbsp;</div>
					<div class="theme" style="background-color:#E8D9FF;" onclick="selectTheme('7')">&nbsp;</div>
					<div class="theme" style="background-color:#CEFBC9;" onclick="selectTheme('9')">&nbsp;</div>
					</div>
				</div>
				<div class="modal_foot" id="themeModalFoot"><button class='mBtnX' onclick='themeModalClose()'>취소</button><button class='mBtnO' onclick='changeTheme(0)'>변경</button></div>
            </div>
        </div>
	</div>
	<form id="clientData" ></form>
	<input style="visibility: hidden;" type="file" name="file" onchange="submitPhoto('1');" accept="image/*">
	<input style="visibility: hidden;" type="file" name="file" onchange="submitPhoto('2');" accept="image/*">
	<form id="serverForm"></form>
<script>
/*
if ("${mypageInfo.suName}"!=null) {
    document.documentElement.classList.add("dark")
	let toggleTheme = document.getElementById("toggleTheme");
    let middle = document.getElementById("middle");
    toggleTheme.addEventListener("click",function(){
    	   if(document.querySelector('body').classList.contains('dark-mode')){
    	        document.body.classList.remove("dark-mode");
    	        middle.classList.remove("dark-mode");
    	    }else{
    	        document.body.classList.add("dark-mode");
    	        middle.classList.add("dark-mode");
    	    }
    },false);
	}
if ("Blue") {
	

	let middle = document.getElementById("middle");
	
	switch(){
	case PINK :
		middle.classList.add("pink-mode");
		break;
	case LEMON :
		middle.classList.add("lemon-mode");
		break;
	case BLUE :
		middle.classList.add("blue-mode");
		break;
	case PURPLE :
		middle.classList.add("purple-mode");
		break;
	case GREEN :
		middle.classList.add("green-mode");
		break;
	}
}*/
</script>
</body>
</html>