<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>건강일기</title>
    <script src="/res/js/agiMain.js"></script>
    <script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.css">
    <link rel="stylesheet" href="/res/css/agiMain.css">
    <link rel="stylesheet" href="/res/css/healthDiary.css">

    <!-- 카카오 스크립트 -->
    <script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
    <!-- 네이버 스크립트 -->
    <script src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
    <script>
        Kakao.init('2afdabad57ed92e1cc9de5bd4baed321');
        //초기화
        let writeForm = "";
        function getInfo() {
            let accessArea = document.getElementById("accessArea");
            //로그인 한 소셜계정 종류 판단
            if ("${accessInfo.type}" != null && "${accessInfo.type}" != "") {
                accessArea.innerHTML = "";
                accessArea.innerHTML = "<span> ${accessInfo.suNickName}님 </span>";
                if ("${accessInfo.type}" == "kakao") {
                    accessArea.innerHTML += "<span onclick=\"kakaoLogout();\">로그아웃</span>"
                } else if ("${ accessInfo.type }" == "naver") {
                    accessArea.innerHTML += "<span onclick=\"naverLogout(); return false;\">로그아웃</span>"
                } else;
                accessArea.innerHTML += "<span onclick=\"movePage('MoveCompanyLoginPage')\">기업회원</span>";
            }
            //아이 정보가 있는 경우 전처리(전체보기 옵션 삽입,아이 변경 시 이벤트 추가)
            if (document.getElementsByClassName("saveBtn")[0] == undefined) {
                let babyInfo = document.getElementsByClassName("babyInfo")[0];
                babyInfo.insertBefore(createOption(), babyInfo.firstChild);
                babyInfo.addEventListener("change", selectBaby);
            }
            //일기 작성 실패 메시지
            if("${fail}"!="") {
            	alert("${fail}");
            }
            writeForm = document.getElementsByClassName("modal_content")[0].innerHTML;
        }
        /* ------------------------------------------------------------- */
        //로그아웃
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
            }, 1000);
            logout();
        }
        function kakaoLogout() {
            if (Kakao.Auth.getAccessToken()) {
                Kakao.API.request({
                    url: '/v1/user/unlink',
                    success: function (response) {
                        logout();
                    },
                    fail: function (error) {
                        console.log(error)
                    },
                })
                Kakao.Auth.setAccessToken(undefined)
            }
        }
        /* ------------------------------------------------------------- */
        //아이 변경 시 리스트 바꿔주는 함수 호출
        function selectBaby() {
            let babyInfo = document.getElementsByClassName("babyInfo")[0];
            changeList(babyInfo.options[babyInfo.selectedIndex].value);
        }
        //아이 코드가 일치하는 피드만 보여줌
        function changeList(value) {
            let diaryList = document.getElementById("diaryArea");
            let diary = diaryList.childNodes;
            console.log(value);
            //초기화
            for (let idx = 0; idx < diary.length; idx++) {
                if (diary[idx].tagName != undefined)
                    diary[idx].style.display = "block";
            }
            //특정 아이를 선택한 경우
            if (value != "total") {
                for (let idx = 0; idx < diary.length; idx++) {
                    let str = "" + diary[idx].className;
                    str = str.substr(6, 2);
                    if (!(str == value) && diary[idx].tagName != undefined) {
                        diary[idx].style.display = "none";
                    }
                }
            }
        }
        //일기작성 모달 띄우기
        function writeHealthDiary() {
            let modal = document.getElementsByClassName("modal")[0];
            let mcontent = document.getElementsByClassName("modal_content")[0];
            mcontent.innerHTML = writeForm;
            let mhead = document.getElementsByClassName("modal_head")[0];
            mhead.innerHTML = "${babyInfo}";
            let mfoot = document.getElementsByClassName("modal_foot")[0];
            mfoot.innerHTML="";
            //버튼
            let mBtnX = createInput("button",null,"취소","mBtnX",null);
            let mBtnO = createInput("button",null,"등록","mBtnO",null);
            mBtnX.addEventListener("click",cancel);
            mBtnO.addEventListener("click",insHealthDiary);
            mfoot.appendChild(mBtnX);
            mfoot.appendChild(mBtnO);
            //시간설정 제이쿼리 스크립트 다시 설정해주기
            let s = document.createElement("script");
            s.text = "	$(document).ready(function(){"
            	+	"	$('input.timepicker').timepicker({"
            	+	"		timeFormat: 'HH:mm',"
            	+	"		interval: 10,"
            	+	"		minTime: '0',"
            	+	"		maxTime: '23:00pm',"
            	+	"		defaultTime: '18',"
            	+	"		startTime: '18',"
            	+	"		dynamic: false,"
            	+	"		dropdown: true,"
            	+	"		scrollbar: true"
            	+	"	});"
            	+	"	$('input.timepicker2').timepicker({"
            	+	"		timeFormat: 'HH:mm',"
            	+	"		interval: 10,"
            	+	"		minTime: '0',"
            	+	"		maxTime: '23:00pm',"
            	+	"		defaultTime: '6',"
            	+	"		startTime: '6',"
            	+	"		dynamic: false,"
            	+	"		dropdown: true,"
            	+	"		scrollbar: true"
            	+	"	});"
            	+	"});";
            let sleepInput = document.getElementsByClassName("item")[5];
            sleepInput.appendChild(s);
            if (document.getElementsByClassName("saveBtn")[0] == undefined) {
                modal.style.display = "block";
            } else {
                alert("아이정보가 없습니다!");
                return;
            }
        }
        //모달닫기
        function cancel() {
            let modal = document.getElementsByClassName("modal")[0];
            let m = document.getElementsByClassName("modal_content")[0];
            m.innerHTML = "";
            modal.style.display = "none";
        }
		//리스트에서 전체보기 항목 추가
        function createOption() {
            let option = document.createElement("option");
            option.setAttribute("value", "total");
            option.setAttribute("selected", true);
            option.appendChild(document.createTextNode("전체"));
            return option;
        }
      	//일기 등록하기
        function insHealthDiary() {
      		let form = document.getElementById("serverForm");
      		let data = document.getElementsByClassName("modal_content")[0];
      		let babyInfo = document.getElementsByClassName("babyInfo")[1];
            let bbCode = babyInfo.options[babyInfo.selectedIndex].value;
      		
			form.appendChild(createInput("hidden","sleep",sleepTime(),null,null));
			form.appendChild(createInput("hidden","bbCode",bbCode,null,null));
			form.appendChild(data);
			
			form.action = "InsertHealthDiary";
			form.method = "post";
			
			form.submit();
        }
		//수면시간 계산
        function sleepTime() {
        	let sleep = document.getElementsByClassName("timepicker")[0].value;
        	let wake = document.getElementsByClassName("timepicker2")[0].value;
        	let sHour = Number(sleep.split(":")[0]);
        	let wHour = Number(wake.split(":")[0]);
			if(sHour>wHour)
				wHour = wHour+24;
        	let s = (sHour*60)+Number(sleep.split(":")[1]);
        	let w = (wHour*60)+Number(wake.split(":")[1]);
        	
        	return Math.trunc((w-s)/60) + "h" + Math.round((Math.round((w-s)%60))/10)*10+"m";
        }
        //현재 시간
/*      let today = new Date();   
        let hours = ('0' + today.getHours()).slice(-2); 
        let minutes = ('0' + today.getMinutes()).slice(-2); */
        //건강일기 디테일 확인
        function showHealthDiary(hdCode) {
        	let clientData = "hdCode="+hdCode;
        	
        	postAjaxJson("ShowHealthDiary",clientData,"cShowHealthDiary");
        }
        //디테일정보 모달 만들기
        function cShowHealthDiary(ajaxData) {
        	let data = JSON.parse(ajaxData);
        	let modal = document.getElementsByClassName("modal")[0];
        	let mhead = document.getElementsByClassName("modal_head")[0];
        	mhead.innerHTML = "<span>"+data[0].bbName+"</span><i class='fa-solid fa-xmark closeBtn editBtn' onclick='cancel()'></i><br />";
        	let mcontent = document.getElementsByClassName("modal_content")[0];
            mcontent.innerHTML = writeForm;
            let mfoot = document.getElementsByClassName("modal_foot")[0];
            mfoot.innerHTML="";
        	
            //항목 요소 가져오기
            let item = document.getElementsByClassName("item");
        	let hdMemo = document.getElementById("hdMemo");
        	modal.style.display = "block";
        	
        	//키
        	if(data[0].bbHeight!=null) {
				item[0].innerHTML = "<span>키</span><span>"+data[0].bbHeight+"</span> cm";
			} else item[0].innerHTML = "<span>키</span><span>정보없음</span> cm";
			//몸무게
			if(data[0].bbWeight!=null) {
				item[1].innerHTML = "<span>몸무게</span><span>"+data[0].bbWeight+"</span> kg";
			} else item[1].innerHTML = "<span>몸무게</span><span>정보없음</span> kg";
			//발
			if(data[0].foot!=null) {
				item[2].innerHTML = "<span>발사이즈</span><span>"+data[0].foot+"</span> cm";
			} else item[2].innerHTML = "<span>발사이즈</span><span>정보없음</span> cm";
			//머리
			if(data[0].head!=null) {
				item[3].innerHTML = "<span>머리둘레</span><span>"+data[0].head+"</span> cm";
			} else item[3].innerHTML = "<span>머리둘레</span><span>정보없음</span> cm";
			//체온
			if(data[0].temperature!=null) {
				item[4].innerHTML = "<span>체온</span><span>"+data[0].temperature+"</span> C";
			} else item[4].innerHTML = "<span>체온</span><span>정보없음</span> C";
			//수면시간
			if(data[0].sleep!=null) {
				item[5].innerHTML = "<span>수면시간</span><span>"+data[0].sleep+"</span>";
			} else item[5].innerHTML = "<span>수면시간</span><span>정보없음</span>";
			//배변량
			if(data[0].defecation!=null) {
				item[6].innerHTML = "<span>배변량</span><span>"+data[0].defecation+"</span>";
			} else item[6].innerHTML = "<span>배변량</span><span>정보없음</span>";
			//배변상태
			if(data[0].defstatus!=null) {
				item[7].innerHTML = "<span>배변상태</span><span>"+data[0].defstatus+"</span>";
			} else item[7].innerHTML = "<span>배변상태</span><span>정보없음</span>";
			//식사량
			if(data[0].meal!=null) {
				item[8].innerHTML = "<span>식사량</span><span>"+data[0].meal+"</span>";
			} else item[8].innerHTML = "<span>식사량</span><span>정보없음</span>";
			//메모
			if(data[0].memo!=null) {
				hdMemo.innerHTML = "<div style='margin: 10px 0;'>메모</div><textarea name='memo' class='mMemoInput' readonly>"+data[0].memo+"</textarea>";
			} else hdMemo.innerHTML = "<div style='margin: 10px 0;'>메모</div><textarea name='memo' class='mMemoInput' readonly>정보없음</textarea>";
        }
        
        function deleteHealthDiary(hdCode) {
        	let form = document.getElementById("serverForm");
        	let result = confirm("삭제하시겠습니까?");
        	if(result) {
        		form.action = "DeleteHealthDiary";
        		form.method = "post";
        		
        		form.appendChild(createInput("hidden","hdCode",hdCode,null,null));
        		form.submit();
        	}
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
                <div id="leftArea">
                    <!-- navigator -->
                    <!-- ------------------------------------------------------------------------ -->
                    <nav class="side-menu">
                        <ul class="menus">
                            <li class="nav-text"><span>감성일기</span></li>
                            <li class="nav-sText" onclick="movePage('MoveDailyDiaryPage')"><span>전체피드</span></li>
                            <li class="nav-sText" onclick="movePage('MoveMyDailyDiaryPage')"><span>내 피드</span></li>
                            <li class="nav-text"><span>건강일기</span></li>
                            <li class="nav-sText" onclick="movePage('MoveHealthDiaryPage')"><span>내 피드</span></li>
                            <li class="nav-sText" onclick="movePagePost('MoveHealthStatusPage')"><span>통계기록</span></li>
                            <li class="nav-sText" onclick="movePage('MoveDoctorComment')"><span>진료기록</span></li>
                        </ul>
                    </nav>
                    <!-- ------------------------------------------------------------------------ -->
                </div>
                <div id="rightArea" class="scrollBar">
                    <div id="buttonArea">
                        ${babyInfo}
                        <button class="writeBtn btn" onclick="writeHealthDiary()">글쓰기</button>
                    </div>
                    <div id="diaryArea">
                        ${diaryList}
                    </div>
                    <div class="up"><a href="#buttonArea">위로</a></div>
                </div>
            </div>
        </div>
        <div class="modal">
            <div class="modal_body">
                <div class="modal_head">
                    ${babyInfo}
                    <i class="fa-solid fa-xmark closeBtn editBtn" onclick="cancel()"></i><br />
                </div>
                <div class="modal_content">
                    <div class="row">
                        <div class="item"><span>키</span><input type="number" class="mMiniInput hi" name="bbHeight" /> cm</div>
                        <div class="item"><span>몸무게</span><input type="number" class="mMiniInput hi" name="bbWeight" /> kg</div>
                    </div>
                    <div class="row">
                        <div class="item"><span>발사이즈</span><input type="number" class="mMiniInput hi" name="foot" /> cm</div>
                        <div class="item"><span>머리둘레</span><input type="number" class="mMiniInput hi" name="head" /> cm</div>
                    </div>
                    <div class="item"><span>체온</span><input type="number" class="mMiniInput hi" name="temperature" /> C</div>
                    <div class="item">
                        <span>수면시간</span>
                        <input type="text" class="timepicker mMiniSelect" />~
                        <input type="text" class="timepicker2 mMiniSelect" />
                        <script>
                        $(document).ready(function(){
                            $('input.timepicker').timepicker({timeFormat: 'HH:mm',
								interval: 10,
    						    minTime: '0',
    						    maxTime: '23:00pm',
    						    defaultTime: '18',
    						    startTime: '18',
    						    dynamic: false,
    						    dropdown: true,
    						    scrollbar: true    });
                            $('input.timepicker2').timepicker({timeFormat: 'HH:mm',
								interval: 10,
    						    minTime: '0',
    						    maxTime: '23:00pm',
    						    defaultTime: '6',
    						    startTime: '6',
    						    dynamic: false,
    						    dropdown: true,
    						    scrollbar: true    });
                        });
                        </script>
                    </div>
                    <div class="row">
                        <div class="item">
                        	<span>배변량</span>
                            <select name="defectation" class="mMiniSelect">
                                <option value="">선택안함</option>
                                <option value="적음">적음</option>
                                <option value="보통">보통</option>
                                <option value="많음">많음</option>
                            </select>
                        </div>
                        <div class="item">
                        	<span>배변상태</span>
                            <select name="defstatus" class="mMiniSelect">
                                <option value="">선택안함</option>
                                <option value="정상">정상</option>
                                <option value="묽음">묽음</option>
                            </select>
                        </div>
                    </div>
                    <div class="item">
                    		<span>식사량</span>
                        	<select name="meal" class="mMiniSelect">
                                <option value="">선택안함</option>
                                <option value="적음">적음</option>
                                <option value="보통">보통</option>
                                <option value="많음">많음</option>
                            </select>
                    </div>
                    <div id="hdMemo">
                    	<div style="margin: 10px 0;">메모</div>
                    	<textarea name="memo" class="mMemoInput"></textarea>
                    </div>
                </div>
                <div class="modal_foot">
                </div>
            </div>
        </div>

        <form id="serverForm" style="display: none"></form>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.js"></script>
    </body>
</html>