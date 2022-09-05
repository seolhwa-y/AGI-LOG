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
        {
          title: '이제 그만 할래요.',
          start: '2022-09-02T17:00:00'
        },
        {
          title: '지겹다...API랑 라이브러리..',
          start: '2022-09-03',
          end: '2022-09-05'
        },
        {
          groupId: 999,
          title: '아싸리 김태훈 복귀한다',
          start: '2022-09-05T08:30:00'
        },
        {
          groupId: 999,
          title: '오전부터 모두의 작업 확인',
          start: '2022-09-05T09:00:00'
        },
        {
          title: '마감하자',
          start: '2022-09-16',
          end: '2022-09-21'
        },
        {
          title: '상담갈겨 다들 화이팅',
          start: '2022-09-05',
          end: '2022-09-16'
        },
        {
          title: '!!!!!!회식 가야지 모두!!!!',
          start: '2022-09-23'
        },
        {
          title: '경고 :: 아기로그로 이동해',
          url: 'http://localhost/',
          start: '2022-09-21T08:30:00'
        },
        {
            title: '경고 :: 지도로 이동해',
            url: 'http://localhost/MoveMapPage?',
            start: '2022-09-21T08:30:00'
          },
        {
            title: '경고 :: 캘린더로 이동해',
            url: 'http://localhost/MoveCalendarPage?',
            start: '2022-09-21T08:30:00'
          },
        {
            title: '경고 :: 건강일기로 이동해',
            url: 'http://localhost/MoveDailyDiaryPage?',
            start: '2022-09-21T08:30:00'
          }
      ]
    });

    calendar.render();
  });
 
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