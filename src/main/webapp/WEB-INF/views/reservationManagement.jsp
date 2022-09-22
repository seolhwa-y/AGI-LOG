<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약관리</title><script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/company.css">
<!-- 알림창 꾸미기 -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<link href='/res/css/calender/main.css' rel='stylesheet' />
<script src='/res/js/calender/main.js'></script>
<style>
#calendar {
	margin: 0 auto;
	max-width: 1200px;
	max-height: 690px;
}
#rightArea {
	padding: 0.3% 0;
	width: 1400px;
}
#time{
	width: 40%;
	height: 95%;
}
#border{
	height: 90%;
	border : 1px solid black;
}
#listBox{
	width: 60%;
	height: 95%;
}
.modal_body {
    width: 1200px;
    box-sizing: border-box;
}
.modal_head{
	position: relative;
	width: 100%;
	height: 4rem;
	margin-bottom: 1rem;
}
.modal_content {
	display: flex;
	width: 100%;
	height: 90%;
}
.closeBtn {
	position: absolute;
	top: 5px;
	right: 5px;
}
#date {
	float: left;
	font-size: 3.5rem;
	margin-left: 2rem;
}
.resH{
	letter-spacing: 3px;
    font-size: 1.3rem;
    width: 7rem;
    padding: 10px;
    vertical-align: top;
    background-color: rgb(255, 194, 204);
}
.resD{
	width: 155px;
    font-size: 1.2rem;
    padding: 10px;
    vertical-align: top;
    color: rgb(97, 97, 97);
    border-bottom: 1px solid #ccc;
    background: #fafafa;
}
.resD select {
	font-size: 1.2rem;
}
#doctor select {
	font-size: 1.2rem;
}
.saveBtn {
	box-shadow: 0px 5px 0px 0px rgb(239 157 171);
    background-color: rgb(255, 194, 204);
}
.timeTitle{
    width: 100%;
    margin-top: 20px;
    margin-bottom: 20px;
    font-size: 199%;
}
#timeContent{
    width: 100%;
    height: 80%;
}
#list{
    width: 100%;
    height: 80%;
    overflow-x: hidden;
    overflow-y: scroll;
    margin-left: 1rem;
}
#morning{
    margin: 1rem;
    height: 40%;
}
#line{
    border: 1px solid dimgray;
    margin: 22px 42px;
}
#afternoon{
    margin: 1rem;
    height: 40%;
}
.mBtnO {
    height: 2.5rem;
    width: 10rem;
    box-shadow: 0px 5px 0px 0px rgb(149 149 149);
    background-color: gainsboro;
    font-size: 1.5rem;
    color: dimgray;
    margin: 1.4rem;
}
.disable{
    height: 2.5rem;
    width: 10rem;
    box-shadow: 0px 5px 0px 0px rgb(255, 194, 204);
    background: rgb(255, 194, 204);
    margin: 1.4rem;
}
.disable:hover {
    background: rgb(255, 194, 204);
}
.scrollBar::-webkit-scrollbar-track {
	background:none;
}
#doctorContent {
	display: flex;
    flex-direction: row-reverse;
    justify-content: space-around;
}

#profile{
	border: 1px solid dimgray;
    display: block;
    float: left;
    width: 8%;
    height: 130%;
    border-radius: 50%;
}
.profileImg{
	max-width: 100%;
    max-height: 100%;
    height: 100%;
    width: 100%;
    border-radius: 50%;
}
</style>
<script>
function init() {
	if ("${companyAccessInfo.coName}" != "") {
		let accessArea = document.getElementById("accessArea");
		accessArea.innerHTML = "";

		accessArea.innerHTML = "<span> ${companyAccessInfo.coName}님 </span>";
		accessArea.innerHTML += "<span onclick=\"movePage(\'CompanyLogout\')\">로그아웃</span>";
		
	}
}
/*********************캘린더************************/

	// div 태그_캘린더에 이벤트 추가
document.addEventListener('DOMContentLoaded', function() {
var calendarEl = document.getElementById('calendar');

// div 태그_캘린더에 라이브러리 적용.
var calendar = new FullCalendar.Calendar(calendarEl, {
  headerToolbar: {
    left: 'prev,next today',
    center: 'title',
    right: 'dayGridMonth'
  },
  // 초기 날짜 :: 설정 안하면 투데이로 보인다.
  initialDate: '2022-09-03', 
  navLinks: false, 
  selectable: false,
  selectMirror: false,
  eventOrder: '-title',
  // 일정을 클릭 했을 때 발생
  eventClick: function(info) {
	  
  },
  // 수정 가능 여부
  editable: true,
  // 일정 갯수 제한
  dayMaxEvents: true, // allow "more" link when too many events
  // 일정
  events: [
      ${resCount}
  ],
    eventDidMount: function(info) {
	},
  // 특정일자 선택했을때의 펑션
  dateClick: function(info) {
	    let modal = document.querySelector(".modal");
		modal.style.display="block";
	    let modalHead = document.getElementsByClassName("modal_head")[0];
	    
	    if("${companyAccessInfo.coPhoto}"!=""){
	    	alert("사진있음");
	    	modalHead.innerHTML = "<div id='profile'><img class='profileImg' src='"+"${companyAccessInfo.coPhoto}"+"'></div><span id='date'>"+info.dateStr+"<span><i class='fa-solid fa-xmark closeBtn editBtn' onclick='modalClose()'></i>";
	    }else{
	    	alert("사진없음");
	    	modalHead.innerHTML = "<div id='profile'><img class='profileImg' src='/res/img/profile_default.png'></div><span id='date'>"+info.dateStr+"<span><i class='fa-solid fa-xmark closeBtn editBtn' onclick='modalClose()'></i>";
	    }
	    
	    let clientData = "resDate="+info.dateStr;
	    postAjaxJson("CoResDetail",clientData,"resManageMent");
	  }
});
calendar.render();
});
//시간, 예약정보 콜백
function resManageMent(ajaxData){
	let info = JSON.parse(ajaxData);
	
	let modal = document.querySelector(".modal");
	modal.style.display="block";
	
	let doctor = document.getElementById("doctor");
	doctor.innerHTML = info.doctorList;
	let timeContent = document.getElementById("timeContent");
	timeContent.innerHTML = info.time;
	
	let list = document.getElementById("list");
	list.innerHTML = "";
	if(info.resInfo!=""&&info.resInfo!=null)
		list.innerHTML = info.resInfo;
}
//시간 조정
function setResTime(obj){
	let info = (obj.value).split(":");
	let doctor = document.getElementsByName("docTime")[0];
	
	let clientData;
	
	if(obj.className == "mBtnO btn"){
		//예약가능하게 바꿈
		obj.className = "mBtnO btn disable";
		clientData = "resDate="+info[0]+"&resTime="+info[1]+"&resDoCode="+doctor.options[doctor.selectedIndex].value;
	}
	
	postAjaxJson("SetResTime",clientData,"callbackTime");
	
}
//수정 콜백
function callbackTime(ajaxData){
	let info = JSON.parse(ajaxData);
	
	let timeContent = document.getElementById("timeContent");
	timeContent.innerHTML = info.time;
}
//의사 변경
function chageDoctor() {
	let doctor = document.getElementsByName("docTime")[0];
	let date = document.getElementById("date").innerText;
	let clientData;
	clientData = "resDate="+date+"&resDoCode="+doctor.options[doctor.selectedIndex].value;
	
	postAjaxJson("GetDoctorResTime",clientData,"callbackDocTime");
}
//해당의사의 예약시간 콜백
function callbackDocTime(ajaxData) {
let info = JSON.parse(ajaxData);
	let timeContent = document.getElementById("timeContent");
	timeContent.innerHTML = info.time;
}
//예약상태 변경
function updateReservation(resCode, idx, idx2) {
	let form = document.getElementById("serverForm");
	//상태코드
	let rcCode = document.getElementsByName("selectResState")[idx];
	let rcCode2 = rcCode.options[rcCode.selectedIndex].value;
	//날짜, 시간
	let date = document.getElementById("date").innerText;
	let time = document.getElementsByClassName("resTime")[idx].innerText;
	//의사 코드
	let doCode = document.getElementsByName("resDoCode")[idx].value;
	
	let clientData = "rcCode="+rcCode2+"&resCode="+resCode+"&resDate="+date+"&resTime="+time.split(":")[0]+"&resDoCode="+doCode;

//	if (idx2 != "") {
//		let doCode = document.getElementsByName("selectDoctor")[idx2];
//		let doCode2 = doCode.options[doCode.selectedIndex].value;
//		clientData += "&resDoCode="+doCode2;
//	}

	postAjaxJson("UpdateReservation",clientData,"resManageMent");
	swal("요청", "요청하신 작업을 완료하였습니다!", "success", { button: "확인"});
}
// 모달창 닫기
function modalClose(){
	let modal = document.querySelector(".modal");
	let modalHead = document.getElementsByClassName("modal_head")[0];
	modalHead.innerHTML = "";
	
	modal.style.display="none";
}
</script>
</head>
<body onload="init()">
	<div id="background">
		<div id="top">
			<div id="accessArea">
				<span onclick="movePage('MoveCompanyLoginPage')">로그인</span>
                <span onclick="movePage('MoveCompanyJoinPage')">회원가입</span>
                <span onclick="movePage('MoveMainPage')">일반회원</span>
			</div>
			<div id="logo" onclick="movePage('MoveReservationManagement')"><span id="txt">아기-로그</span>
				<img src="/res/img/logo.png" alt="images">
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
				<div id='calendar'></div>
				
			</div>
		</div>
	</div>
	<div class="modal">
            <div class="modal_body">
				<div class="modal_head">
					<i class="fa-solid fa-xmark closeBtn editBtn" onclick="modalClose()"></i>
				</div>
				<div class="modal_content">
					<div id="time">
					<div class="timeTitle">* 예약시간 관리 *</div>
						<div id="doctorContent">
							<div id="tip" style="display: flex">
								<div style="background-color: rgb(255, 194, 204); width: 1rem; height: 1rem; margin-right: 2px;"></div>예약열림
								<div style="background-color: gainsboro; width: 1rem; height: 1rem; margin-right: 2px; margin-left: 5px;"></div>예약닫힘
							</div>
							<div id="doctor">
							</div>
						</div>
						<div id="timeContent">
						</div>
					</div>
					<div id="border">
					</div>
					<div id="listBox">
					<div class="timeTitle">* 예약정보 관리 *</div>
						<div id="list" class="scrollBar">
						</div>
					</div>
				</div>
				<div class="modal_foot"></div>
            </div>
    </div>
	<form id="serverForm"></form>
</body>

</html>