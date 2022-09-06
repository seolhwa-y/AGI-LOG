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
    max-width: 1100px;
    margin: 0 auto;
  }

	.modal{
		color: dimgray;
	}

	.modal_foot {
    position: relative;
    display: flex;
    margin: 3% auto;
    justify-content: space-between;
    width: 70%;
}
	.modal_head{
		font-size: 3rem;
		height: 8%;
	}
	.modal_content{
		height: 75%;
		width: 80%;
		margin: auto;
		
	}
	.modal_foot{
	}
	.mBtnO {
    height: 2.5rem;
    width: 13rem;
    box-shadow: 0px 5px 0px 0px rgb(239 157 171);
    background-color: rgb(255, 194, 204);
    font-size: 1.1rem;
    color: dimgray;
	}
	.mBtnX {
    color: dimgray;
    background: gainsboro;
    height: 2.3rem;
    width: 13rem;
    box-shadow: 0px 5px 0px 0px rgb(182 182 182);
    font-size: 1.1rem;
	}
	#modalTop{
	border-bottom: solid 2px gray;
	height: 30%;
	width: 90%;
	margin: 0 auto;
	}
	#top_title{
		
		height: 15%;
		margin-top: 6%;
		text-align: left;
		font-size: 170%;
	}
	#top_content{
		
		height: 70%;
		margin-top: 6%;
		font-size: 150%;
	}
	#isCheckDiary{
		
		height: 22%;
		width: 83%;
		margin: 4.5% auto;
	}
	#reservationInfo{
		
		height: 22%;
		width: 83%;
		margin: 6% auto;
		text-align: left;
	}
	#bottom_title{
		height: 12%;
		margin-top: 3%;
		text-align: left;
		font-size: 170%;
	}
	#bottom_content{
		height: 80%;
		width: 90%;
		margin: 0 auto;
		text-align: left;
		font-size: 150%;
	
	}
	li{
		margin-top: 4%;
		margin-bottom: 7%;
		
	}
	#modalBottom{
	
	height: 62%;
	width: 90%;
	margin: 0 auto;
	}

</style>
<script>
Kakao.init('2afdabad57ed92e1cc9de5bd4baed321');
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


/*********************캘린더************************/
 
 	// div 태그_캘린더에 이벤트 추가
   document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    // div 태그_캘린더에 라이브러리 적용.
    var calendar = new FullCalendar.Calendar(calendarEl, {
    	// 헤더 왼쪽 : 전월, 당월, 오늘 이동
    	// 헤더 중간 : 현재 캘린더 년 
    	// 헤어 오른쪽 : 월, 주, 일로 구분하여 보기.
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay'
      },
      // 초기 날짜 :: 설정 안하면 투데이로 보인다.
      initialDate: '2022-09-03', 
      // 일간 주간 캘린더 이동
      navLinks: true, 
      //달력 일정 드래그 해서 이동 가능.
      selectable: true,
      selectMirror: true,
      // 캘린더에서 드래그 해서 일정 생성
      select: function(arg) {
        var title = prompt('Event Title:');
        if (title) {
          calendar.addEvent({
            title: title,
            start: arg.start,
            end: arg.end,
            allDay: arg.allDay
          })
        }
        calendar.unselect()
      },
      // 일정을 클릭 했을 때 발생
      eventClick: function(arg) {
        if (confirm('일정확인???')) {
          /* arg.event.remove() */
        }
      },
      // 수정 가능 여부
      editable: true,
      // 일정 갯수 제한
      dayMaxEvents: true, // allow "more" link when too many events
      // 일정
      events: [
       
      ],
      // 특정일자 선택했을때의 펑션
      dateClick: function(info) {
    	    alert('Clicked on: ' + info.dateStr);
    	    //dateStr = 2022-09-02
    	    //alert('Coordinates: ' + info.jsEvent.pageX + ',' + info.jsEvent.pageY);
    	    //alert('Current view: ' + info.view.type);
    	    // change the day's background color just for fun
    	    //info.dayEl.style.backgroundColor = 'red';
    	    
    	    let clientData = "date="+info.dateStr;
    	    alert(clientData);
    	    postAjaxJson("DateDetail",clientData,"callBackDateDetail");
    	  }
    });

    calendar.render();
  });
function callBackDateDetail(ajaxData){
	let dateInfo = JSON.parse(ajaxData);
	alert(dateInfo);
	
	let isCheckDiary = document.getElementById("isCheckDiary");
	let resInfo = document.getElementById("reservationInfo");
	let schList = document.gteElementById("scheduleList");
	
}
</script>
</head>
<body onload="getInfo()">
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
			<div id="rightArea" class="scrollBar">
				<!-- 캘린더 영역 -->
				<div id='calendar'></div>
			</div>
		</div>
		<div class="modal" style="display: block;">
            <div class="modal_body">
				<div class="modal_head">날짜~~
					<i class="fa-solid fa-xmark closeBtn editBtn" style="float: right;"></i><br />
				</div>
				<div class="modal_content">
					<div id="modalTop">
						<div id="top_title">기록확인</div>
						<div id="top_content">
							<div id="isCheckDiary">데다 헬다</div>
							<div id="reservationInfo">예약 : 2022.09.27 인천병원</div>
						</div>
					</div>
					<div id="modalBottom">
						<div id="bottom_title">기타일정</div>
						<div id="bottom_content">
							<ul id="scheduleList">
								<li>일정1</li>
								<li>일정1</li>
								<li>일정1</li>
								<li>일정1</li>
								<li>일정1</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="modal_foot" id="themeModalFoot"><button class='mBtnX btn' >취소</button><button class='mBtnO btn' >변경</button></div>
            </div>
        </div>
	</div>
	<form id="serverForm"></form>
</body>

</html>