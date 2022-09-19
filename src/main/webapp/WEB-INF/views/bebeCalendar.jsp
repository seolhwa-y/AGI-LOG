<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>캘린더</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link href='/res/css/calender/main.css' rel='stylesheet' />
<script src='/res/js/calender/main.js'></script>

<!-- 카카오 스크립트 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<!-- 네이버 스크립트 -->
<script	src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
<style>
#calendar {
	margin: 0 auto;
	max-width: 1250px;
	max-height: 690px;
}
#rightArea {
	padding: 0.2% 0;
	width: 1400px;
	background-color: whitesmoke;
	
}

.modal {
	color: dimgray;
}

.modal_foot {
	position: relative;
	display: flex;
	margin: 3% auto;
	justify-content: space-between;
	width: 70%;
}

.modal_head {
	font-size: 3rem;
	height: 8%;
}

.modal_content {
	height: 75%;
	width: 80%;
	margin: 9% auto;
}

.modal_foot {
	
}

.mBtnO {
	height: 2.5rem;
	width: 13rem;
	box-shadow: 0px 5px 0px 0px rgb(239, 157, 171);
	background-color: rgb(255, 194, 204);
	font-size: 1.1rem;
	color: dimgray;
}

.mBtnX {
	color: dimgray;
	background: gainsboro;
	height: 2.3rem;
	width: 13rem;
	box-shadow: 0px 5px 0px 0px rgb(182, 182, 182);
	font-size: 1.1rem;
}

#modalTop {
	border-bottom: solid 2px gray;
	height: 30%;
	width: 90%;
	margin: 0 auto;
}

#top_title {
	height: 15%;
	margin-top: 6%;
	text-align: left;
	font-size: 170%;
}

#top_content {
	height: 70%;
	margin-top: 6%;
	font-size: 150%;
}

#isCheckDiary {
	height: 22%;
	width: 83%;
	margin: 4.5% auto;
}

#reservationInfo {
	height: 22%;
	width: 83%;
	margin: 6% auto;
	text-align: left;
}

#bottom_title {
	height: 12%;
	margin-top: 3%;
	text-align: left;
	font-size: 170%;
}

#bottom_content {
	height: 80%;
	width: 90%;
	margin: 0 auto;
	text-align: left;
	font-size: 150%;
}

.schList {
	margin-top: 4%;
	margin-bottom: 7%;
}

#modalBottom {
	height: 62%;
	width: 90%;
	margin: 0 auto;
}

.fc-event-title {
	display: flex;
	align-items: center;
}

/* .fc-day-today {
    background: #FFF !important;
    border: none !important;
	}  */
input[type="checkbox"] {
	display: none;
}

input[type="checkbox"]+label {
	display: inline-block;
	width: 20px;
	height: 20px;
	border: 3px solid #707070;
	position: relative;
	top: 6px;
}

input[type="checkbox"]:checked+label {
	width: 20px;
	height: 20px;
	position: relative;
	top: 8.5px;
}

input[type="checkbox"]:checked+label::after {
	content: '✔';
	font-size: 25px;
	position: relative;
	color: green;
	top: -8px;
}

.schBtn {
	height: 30px;
	margin-top: 3px;
	position: absolute;
}

.schInput {
	height: 30px;
}

.dateImg {
	margin-right: 6px;
}

.fc .fc-writeHDBtn-button {
var(--fc-button-bg-color, #ffffff);
}
</style>
<script>
Kakao.init('2afdabad57ed92e1cc9de5bd4baed321');
function getInfo() {
	/* 테마 이미지 or 단순색상 */
	let body = document.getElementById("body");
	
	let suTheme = "${accessInfo.suTheme}";
	
	if(suTheme != ""){
		if(suTheme.indexOf("#") == 0){
			//단순색상
			/* 테마 사용자값으로 설정 */
			//배경css제거
			body.className = "";
			
			let color = suTheme.split(":");

			body.style.background = color[0];

		}else{
			//배경이미지
			body.style.background = "none";
			body.className = "bgImage"+suTheme;
		}
	}
	
	let accessArea = document.getElementById("accessArea");
	if ("${accessInfo.type}"!= null&&"${accessInfo.type}"!="") {
		accessArea.innerHTML = "";
		accessArea.innerHTML = "<span onclick=\"movePage('MoveMyPage')\"> ${accessInfo.suNickName}님 </span>";
		if ("${accessInfo.type}"== "kakao") {
			accessArea.innerHTML +="<span onclick=\"kakaoLogout();\">로그아웃</span>"
		} else if ("${ accessInfo.type }"== "naver") {
			accessArea.innerHTML +="<span onclick=\"naverLogout(); return false;\">로그아웃</span>"
		} else ;
		accessArea.innerHTML += "<span onclick=\"movePage('MoveCompanyLoginPage')\">기업회원</span>";
	}
	document.getElementsByClassName("fc-writeDDBtn-button")[0].style.backgroundColor="lightpink";
	document.getElementsByClassName("fc-writeDDBtn-button")[0].style.border="solid 2px dimgray";
	document.getElementsByClassName("fc-writeHDBtn-button")[0].style.backgroundColor="lightblue";
	document.getElementsByClassName("fc-writeHDBtn-button")[0].style.border="solid 2px dimgray";
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
    setTimeout(function () {
        closePopUp();
        logout();
    }, 1000);
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


/*********************캘린더************************/
 
 	// div 태그_캘린더에 이벤트 추가
   document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    // div 태그_캘린더에 라이브러리 적용.
    var calendar = new FullCalendar.Calendar(calendarEl, {
    	aspectRatio: 2,
    	customButtons: {
    	    writeDDBtn: {
    	      text: '일기쓰기',
    	      click: function() {
    	        let form = document.getElementById("serverForm");
    	        form.appendChild(createInput("hidden","moveWrite","1",null,null));
    	        form.appendChild(createInput("hidden","returnAction","MoveCalendarPage",null,null));
    	        form.action = "MoveDailyDiaryPage";
    	        form.method = "post";
    	        form.submit();
    	      }
    	    },
    	    writeHDBtn: {
      	      text: '기록쓰기',
      	      click: function() {
      	    	let form = document.getElementById("serverForm");
    	        form.appendChild(createInput("hidden","moveWrite","1",null,null));
    	        form.action = "MoveHealthDiaryPage";
    	        form.method = "post";
    	        form.submit();
      	      }
      	    }
    	  },
    	// 헤더 왼쪽 : 전월, 당월, 오늘 이동
    	// 헤더 중간 : 현재 캘린더 년 
    	// 헤더 오른쪽 : 월, 주, 일로 구분하여 보기.
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'writeDDBtn writeHDBtn dayGridMonth'
      },
      // 초기 날짜 :: 설정 안하면 투데이로 보인다.
      initialDate: '2022-09-03', 
      // 일간 주간 캘린더 이동
      navLinks: false, 
      //달력 일정 드래그 해서 이동 가능.
      selectable: false,
      selectMirror: false,
      eventOrder: '-title',
      // 일정을 클릭 했을 때 발생
      eventClick: function(info) {
		if(info.el.classList.item(8) == "healthDiary") {
			alert("dhlsdksgej");
		}
		else {
			let modalTitle = document.getElementById("modalTitle");
			let clientData = "date="+info.event.startStr;
	  	    
			modalTitle.innerText = info.event.startStr;
	  	    
			postAjaxJson("DateDetail",clientData,"showDateDetail");
		}
      },
      // 수정 가능 여부
      editable: true,
      // 일정 갯수 제한
      dayMaxEvents: true, // allow "more" link when too many events
      // 일정
      events: [
          ${birthList}
    	  ${ddList}
    	  ${hdList}
    	  ${reList}
    	  ${scList}
      ],
        eventDidMount: function(info) {
    	    const bimg = document.createElement("img");
    		bimg.setAttribute("src","/res/img/birthday.png");
    		bimg.setAttribute("width", "16");
    		bimg.setAttribute("height", "16");
    	    bimg.classList.add("dateImg");
    	    if(info.el.classList.item(8) == "birthDay"||info.el.classList.item(7) == "birthDay")
    	    	info.el.querySelector(".fc-event-title").insertBefore(bimg,info.el.querySelector(".fc-event-title").firstChild);
    	    
    	    const ddimg = document.createElement("img");
    	    ddimg.setAttribute("src","/res/img/dailydiary.png");
    	    ddimg.setAttribute("width", "16");
    	    ddimg.setAttribute("height", "16");
    	    ddimg.classList.add("dateImg");
    	    if(info.el.classList.item(8) == "dailyDiary"||info.el.classList.item(7) == "dailyDiary")
    	    	info.el.querySelector(".fc-event-title").appendChild(ddimg);
    	    
    	    const hdimg = document.createElement("img");
    	    hdimg.setAttribute("src","/res/img/healthdiary.png");
    	    hdimg.setAttribute("width", "16");
    	    hdimg.setAttribute("height", "16");
    	    hdimg.classList.add("dateImg");
    	    if(info.el.classList.item(8) == "healthDiary"||info.el.classList.item(7) == "healthDiary")
    	    	info.el.querySelector(".fc-event-title").appendChild(hdimg);
    	    
    	    const reimg = document.createElement("img");
    	    reimg.setAttribute("src","/res/img/reservation.png");
    	    reimg.setAttribute("width", "16");
    	    reimg.setAttribute("height", "16");
    	    reimg.classList.add("dateImg");
    	    if(info.el.classList.item(8) == "reservation"||info.el.classList.item(7) == "reservation")
    	    	info.el.querySelector(".fc-event-title").appendChild(reimg);
    	    
    	    const scimg = document.createElement("img");
    	    scimg.setAttribute("src","/res/img/schedule.png");
    	    scimg.setAttribute("width", "16");
    	    scimg.setAttribute("height", "16");
    	    scimg.classList.add("dateImg");
    	    if(info.el.classList.item(8) == "schedule"||info.el.classList.item(7) == "schedule")
    	    	info.el.querySelector(".fc-event-title").appendChild(scimg);
    	  },
      // 특정일자 선택했을때의 펑션
      dateClick: function(info) {
    	    //alert('Clicked on: ' + info.dateStr);
    	    //dateStr = 2022-09-02
    	    //alert('Coordinates: ' + info.jsEvent.pageX + ',' + info.jsEvent.pageY);
    	    //alert('Current view: ' + info.view.type);
    	    // change the day's background color just for fun
    	    //info.dayEl.style.backgroundColor = 'red';
    	    let modalTitle = document.getElementById("modalTitle");
    	    let clientData = "date="+info.dateStr;
    	    
    	    modalTitle.innerText = info.dateStr;
    	    
    	    postAjaxJson("DateDetail",clientData,"showDateDetail");
    	  }
    });

    calendar.render();
  });
// 특정일 클릭시 모달창 띄워주고 안에 해당일 정보들 전부 보여줌
function showDateDetail(ajaxData){
	let dateInfo = JSON.parse(ajaxData);

	let modal = document.querySelector(".modal");
	modal.style.display="block";
	
	let modalTitle = document.getElementById("modalTitle");
	let resInfo = document.getElementById("reservationInfo");
	let schList = document.getElementById("scheduleList");
	let checkDd = document.getElementById("checkDd");
	let checkHd = document.getElementById("checkHd");
	
	//해당 날짜
	let date = dateInfo.date;
	// 공유,헬스 작성여부에따라 체크표시
	if(dateInfo.dailyDiary){
		checkDd.setAttribute("checked","checked");
		
		let c = document.getElementsByClassName("checkName")[0];
		
		c.addEventListener("click",function() {
			showDd(date);
		});
	}
	if(dateInfo.healthDiary){
		checkHd.setAttribute("checked","checked");
		
		let c = document.getElementsByClassName("checkName")[1];
		
		c.addEventListener("click",function() {
			showHd(date);
		});
	}
	
	
	// 예약여부에따라 내용 표시
	let resInfoCheck = [];
	resInfoCheck = dateInfo.reservationInfo;
	if(resInfoCheck == null){
		//예약내역 없을 떄
		resInfo.innerHTML = "예약 : ";
	}else{
		//예약내역 있을 때
		resInfo.innerHTML = "예약 : "+dateInfo.reservationInfo[0].resCoName+"&nbsp&nbsp<i class=\"fa-solid fa-trash-can delBtn\" onclick=\"deleteReservation(\'"+dateInfo.reservationInfo[0].resCode+","+dateInfo.reservationInfo[0].resDate+","+dateInfo.reservationInfo[0].resCoName+"\')\"></i>";
	}
	// 개인일정여부에 따라 내용 표시
	schList.innerHTML="";
	let schInfoCheck = [];
	schInfoCheck = dateInfo.scheduleList;
	if(schInfoCheck == null){
		//개인일정 없을 때 +버튼만 안에넣어줌
		schList.innerHTML += "<li><i class=\"fa-solid fa-square-plus updBtn\" onclick=\"insSch(\'"+dateInfo.date+"\')\"></i></li>";
	}else{
		//개인일정 있을 떄
		for(idx=0;dateInfo.scheduleList.length>idx;idx++){
			schList.innerHTML += "<li name=\"scheduleList\" class=\"schList\">"+dateInfo.scheduleList[idx].scheduleName+"&nbsp&nbsp<i class=\"fa-solid fa-pen updBtn\" onclick=\"updSch(\'"+dateInfo.scheduleList[idx].scheduleCode+","+dateInfo.scheduleList[idx].scheduleDate+","+idx+","+dateInfo.scheduleList[idx].scheduleName+"\')\"></i>&nbsp<i class=\"fa-solid fa-trash-can delBtn\" onclick=\"deleteSchedule(\'"+dateInfo.scheduleList[idx].scheduleCode+","+dateInfo.scheduleList[idx].scheduleDate+","+dateInfo.scheduleList[idx].scheduleName+"\')\"></i></li>";
			//마지막에 +버튼(개인일정추가) 추가
			if(dateInfo.scheduleList.length-1 == idx){
				if(idx!=4){
					//개인일정 최댓값이 5이므로 4까지만 +버튼 추가해줌
					schList.innerHTML += "<li><i class=\"fa-solid fa-square-plus updBtn\" onclick=\"insSch(\'"+dateInfo.scheduleList[idx].scheduleDate+"\')\"></i></li>";
				}
			}
		}
	}
	
}
// 모달창 닫기
function modalClose(){
	let modal = document.querySelector(".modal");
	
	let resInfo = document.getElementById("reservationInfo");
	let schList = document.getElementById("scheduleList");
	let checkDd = document.getElementById("checkDd");
	let checkHd = document.getElementById("checkHd");
	
	checkDd.removeAttribute("checked");
	checkHd.removeAttribute("checked");
	
	resInfo.innerHTML = "";
	schList.innerHTML = "";
	
	modal.style.display="none";
}
// 에약일정 삭제
function deleteReservation(resInfo){
	//code,date,coName
	let info = [];
	info = resInfo.split(",");
	
	let delConfirm = confirm(info[2]+" 예약을 취소하시겠습니까?");
	if(delConfirm){
		//확인 누름
		let clientData = "resCode="+info[0]+"&resDate="+info[1];
		postAjaxJson("DeleteReservation",clientData,"showDateDetail");
	}else{
		//취소 누름
		alert("예약삭제가 취소되었습니다");
	}
}
// 개인일정 삭제
function deleteSchedule(schInfo){
	//code,date,name
	let info = [];
	info = schInfo.split(",");
	
	let delConfirm = confirm("기타일정 '"+info[2]+"' 항목을 삭제하시겠습니까?");
	if(delConfirm){
		//확인 누름
		let clientData = "scheduleCode="+info[0]+"&scheduleDate="+info[1];

		postAjaxJson("DeleteSchedule",clientData,"showDateDetail");
	}else{
		//취소 누름
		alert("삭제가 취소되었습니다");
	}
	
}

// 개인일정 업데이트
function updSch(schInfo){
	//code, date, idx, name
	let info = [];
	info = schInfo.split(",");
	
	//수정할 개인일정 특정함.
	let schedule = document.getElementsByName("scheduleList")[info[2]];
	
	schedule.innerHTML ="<input id=\"updSchedule\"class=\"mScheduleInput schInput\" value=\""+info[3]+"\"schInput\" type=\"text\">&nbsp<button class=\"mMiniBtn btn schBtn\" onclick=\"updateSchedule(\'"+info[0]+","+info[1]+"\')\">수정</button>";

}
// 개인일정 업데이트 수정버튼 클릭
function updateSchedule(schInfo){
	//code, date
	let info = [];
	info = schInfo.split(",");
	
	let updSchedule = document.getElementById("updSchedule");
	let clientData = "scheduleCode="+info[0]+"&scheduleDate="+info[1]+"&scheduleName="+updSchedule.value;

	postAjaxJson("UpdateSchedule",clientData,"showDateDetail");
}
// 개인일정 추가
function insSch(schDate){

	let clientData = "date="+schDate;
	
	document.getElementById("scheduleList").lastChild.innerHTML = "<input id=\"insSchedule\"class=\"mScheduleInput schInput\" type=\"text\">&nbsp<button class=\"mMiniBtn btn schBtn\" onclick=\"insertSchedule(\'"+schDate+"\')\">등록</button>";
}
// 개인일정 추가 등록버튼 클릭
function insertSchedule(schDate){
	let insSchedule = document.getElementById("insSchedule");
	let clientData = "scheduleName="+insSchedule.value+"&scheduleDate="+schDate;
	
	postAjaxJson("InsertSchedule",clientData,"showDateDetail");
}

function showDd(date) {
	let form = document.getElementById("serverForm");
	form.appendChild(createInput("hidden","ddDate",date,null,null));
	form.action = "MoveMyDailyDiaryPage";
	form.method = "post";
	
	form.submit();
}

function showHd(date) {
	let form = document.getElementById("serverForm");
	form.appendChild(createInput("hidden","hdDate",date,null,null));
	form.action = "MoveHealthDiaryPage";
	form.method = "post";
	
	form.submit();
}

</script>
</head>
<body onload="getInfo()" id="body">
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
			<div id="rightArea" class="scrollBar">
				<!-- 캘린더 영역 -->
				<div id='calendar'></div>
			</div>
		</div>
		<div class="modal" >
            <div class="modal_body">
				<div class="modal_head">
				    <a id="modalTitle"></a>
					<i class="fa-solid fa-xmark closeBtn " onclick ="modalClose()"style="float: right;"></i><br />
				</div>
				<div class="modal_content">
					<div id="modalTop">
						<div id="top_title">기록확인</div>
						<div id="top_content">
							<div id="isCheckDiary">
								<input id="checkDd" type="checkbox" >
								<label for="checkDd" class="checkName" onclick="return false;"></label> 공유일기
								<a style="margin-left: 165px;"></a>
								<input id="checkHd" type="checkbox" onclick="return false;">
								<label for="checkHd" class="checkName" ></label> 건강일기
							</div>
							<div id="reservationInfo"></div>
						</div>
					</div>
					<div id="modalBottom">
						<div id="bottom_title">기타일정</div>
						<div id="bottom_content">
							<ul id="scheduleList">
							</ul>
						</div>
					</div>
				</div>
            </div>
        </div>
	</div>
	<form id="serverForm"></form>
</body>

</html>