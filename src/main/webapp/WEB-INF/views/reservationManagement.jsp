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
</style>
<script>
function init() {
	if ("${companyAccessInfo.coName}" != "") {
		let accessArea = document.getElementById("accessArea");
		accessArea.innerHTML = "";

		accessArea.innerHTML = "<span> ${companyAccessInfo.coName}님 </span>";
		accessArea.innerHTML += "<span onclick=\"movePage(\'CompanyLogout\')\">로그아웃</span>";
		accessArea.innerHTML += "<span onclick=\"movePage(\'MoveMainPage\')\">일반회원</span>";
	}
}
/*********************캘린더************************/

	// div 태그_캘린더에 이벤트 추가
document.addEventListener('DOMContentLoaded', function() {
var calendarEl = document.getElementById('calendar');

// div 태그_캘린더에 라이브러리 적용.
var calendar = new FullCalendar.Calendar(calendarEl, {
	
	customButtons: {
	    writeDDBtn: {
	      text: '일기쓰기',
	      click: function() {
	        let form = document.getElementById("serverForm");
	        form.appendChild(createInput("hidden","moveWrite","1",null,null));
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
	/*if(info.el.classList.item(8) == "healthDiary") {
		alert("dhlsdksgej");
	}
	else {
		let modalTitle = document.getElementById("modalTitle");
		let clientData = "date="+info.event.startStr;
  	    
		modalTitle.innerText = info.event.startStr;
  	    
		postAjaxJson("DateDetail",clientData,"showDateDetail");
	}*/
  },
  // 수정 가능 여부
  editable: true,
  // 일정 갯수 제한
  dayMaxEvents: true, // allow "more" link when too many events
  // 일정
  events: [
      
  ],
    eventDidMount: function(info) {

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

	function updateReservation(resCode, idx, idx2) {
		let form = document.getElementById("serverForm");

		alert(resCode + "," + idx + "," + idx2);
		let rcCode = document.getElementsByName("selectResState")[idx];
		let rcCode2 = rcCode.options[rcCode.selectedIndex].value;
		
		form.appendChild(createInput("hidden","resCode",resCode,null,null));

		let hidden1 = document.createElement("input");
		hidden1.type = "hidden";
		hidden1.name = "rcCode";
		hidden1.value = rcCode2;
		 
		form.appendChild(hidden1);
		
		if (idx2 != "") {
			alert("실행체크");
			let doCode = document.getElementsByName("selectDoctor")[idx2];
			let doCode2 = doCode.options[doCode.selectedIndex].value;
			
			let hidden2 = document.createElement("input");
			hidden2.type = "hidden";
			hidden2.name = "doCode";
			hidden2.value = doCode2;
			
			form.appendChild(hidden2);
		}

		form.action = "UpdateReservation";
		form.method = "post";
		form.submit();
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
				${resInfo}
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