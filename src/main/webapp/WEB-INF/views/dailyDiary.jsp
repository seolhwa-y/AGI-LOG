<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>감성일기</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/dailyDiary.css">

<!-- 알림창 꾸미기 -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<!-- 카카오 스크립트 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<!-- 네이버 스크립트 -->
<script	src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
<style>
  .scrollBar::-webkit-scrollbar-track {
      background:none;
  }
#commentList {
	width: 95%;
	height: 8rem;
	overflow: auto;
	margin-top: 1%;
}
.viewFeedHead > img{
	height: 230px;
    width: 300px;
}
	#writeFeed{
		height: 90%;
		width: 100%;
		font-size: 140%;
	}
.comment {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-left: 6%;
    margin-top: 2%;
    margin-right: 3%;
}

.profileImage {
	width: 2rem;
	height: 2rem;
}
.suNickname{
	width: 20%;
    text-align: center;
}

.dcContent{
	width: 64%;
    text-align: left;
}

.ueBtn{
    width: 13%;
    float: right;
}
.feedDate {
    margin-bottom: 3%;
    border-bottom: 1px solid dimgray;
}
	#rightArea{
		width: 120%;
		height: 100%;
		overflow-x:hidden;
		padding: 0.4% 0 0 0;
		margin-left : 4.5%;
	}
	#rightArea_top{
		width: 100%;
    	height: 10%;
    	padding: 1% 5%;
    	margin-left: 3px;
	}
		#rightArea_middle{
		
		width: 100%;
		height: 88%;
		overflow-x:hidden;
		overflow-y: scroll;
	}
		#writeFeedArea{

		float: right;
		height: 95%;
		width: 10%;
		margin-right: 7%;
	}
	.submitBtn{
	float: right;
    margin-right: -2rem;
    right: 13px;
    height: 33px;
    margin-top: 2px;
    width:70px;
	}
	.udicon{
	margin-top: -23.5rem;
    margin-left: -23rem;
	}
</style>
<script>
Kakao.init('2afdabad57ed92e1cc9de5bd4baed321');
function getInfo() {
	/* 테마 이미지 or 단순색상 */
	let body = document.getElementById("body");
	
	let suTheme = "${accessInfo.suTheme}";
	let sideMenu = document.querySelector(".side-menu");
	if(suTheme != ""){
		if(suTheme.indexOf("#") == 0){
			//단순색상
			/* 테마 사용자값으로 설정 */
			//배경css제거
			body.className = "";
			
			let color = suTheme.split(":");

			body.style.background = color[0];
			sideMenu.style.background= color[1];

		}else{
			//배경이미지
			body.style.background = "none";
			body.className = "bgImage"+suTheme;
			sideMenu.style.background= "whitesmoke";

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
		} else
			accessArea.innerHTML += "<span onclick=\"movePage('MoveCompanyLoginPage')\">기업회원</span>";
	}

	let feedList = document.getElementById("feedList");
	feedList.innerHTML = "${allDailyDiaryList}";
	
	if("${isWrite}") {
		moveWriteFeed();
	}
	
	if("${ddCode}"!=""){
		getFeed("${ddCode}");             
		
	}
	
	if("${title}"=="경고"){
		swal("${title}", "${message}", "error", { button: "확인"});
	}else if("${title}"!=""){
		swal("${title}", "${message}", "success", { button: "확인"});
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


//글쓰기 :: 모달창 구성
function moveWriteFeed(){
	let modal = document.querySelector(".modal");
	let modalHead = document.querySelector(".modal_head");
	let modalContent = document.querySelector(".modal_content");
	let modalFoot = document.querySelector(".modal_foot");

	modal.style.display = "block";
	
	modalHead.innerHTML = "<div class='insBabyTitle'>일기 쓰기</div>";
	modalContent.innerHTML = "<table class='insWriteTable'><tr><td class='tdName'><span class='name'>내  용 :</span></td><td class='tdValue'><textarea class='mMiniInput name' name='ddContent' placeholder='내용을 입력하세요' required></textarea></td></tr>"
							+"<tr><td></td><td><input style=\"display: block;width:100%\" type=\"file\" name=\"files\" id=\"input-image\"/></td></tr>"
							+"<tr><td class='tdName'><span class='name'>공개 여부 :</span></td><td class='tdValue'><input class='statusCheck' type='radio' name='ddStatus' value='1' checked>공개<input class='statusCheck' type='radio' name='ddStatus' value='0'>비공개</td></tr></table>";
	modalFoot.innerHTML = "<button class='mBtnX' onclick=\"modalClose('${returnAction}')\">취소</button><button class='mBtnO' onclick=\"insertDailyDiary('${returnAction}')\">등록</button>";
}

//일기 등록 :: 일기 등록버튼 클릭
function insertDailyDiary(returnAction){
	let form = document.getElementById("serverForm");
	let ddContent = document.getElementsByName("ddContent")[0].value;
	let ddStatus = document.getElementsByName("ddStatus");
	ddContent = ddContent.replaceAll("\n", "<br>");
	
	for(idx = 0; idx < 2; idx++) {
		if(ddStatus[idx].checked) {
			form.appendChild(createInput("hidden","ddStatus",ddStatus[idx].value,null,null));
			break;
		}
	}
	
	form.appendChild(createInput("hidden","ddContent",ddContent,null,null));
	form.appendChild(document.getElementById("input-image"));
			
	form.action = "InsertDailyDiary";
	form.method = "post";
	form.enctype = "multipart/form-data";
	
	let file = document.getElementById("input-image");
	file.style.display = "none";
	
	form.submit();
	modalClose(returnAction);
}
//모달닫기
function modalClose(returnAction){
	let modal = document.querySelector(".modal");
	let modalHead = document.querySelector(".modal_head");
	let modalContent = document.querySelector(".modal_content");
	let modalFoot = document.querySelector(".modal_foot");
	
	modal.style.display="none";
	modalHead.innerHTML = "";
	modalContent.innerHTML = "";
	modalFoot.innerHTML = "";

	if (returnAction != '') {
		movePage(returnAction);
	}
}
/* 감성일기 댓글 작업 */
// 댓글 수정 버튼
function updateInput(ddCode, ddSuCode, dcCode, dcDate, ddDate){
	let dcContent = document.getElementsByClassName(dcDate)[0];
	document.getElementsByClassName("ueBtn")[0].style.display = "none";
	dcContent.innerHTML = "";
	dcContent.innerHTML += "<input class ='updDbComment mEditInput'>";
	dcContent.innerHTML += "<button class='submitBtn btn' onClick='updateDailyDiaryComment(" + ddCode + "," + ddSuCode + "," + dcCode + "," + dcDate + "," + ddDate +")'>확인</button>";

}

// 감성일기 댓글 전부 AJAX
// 1. 댓글 등록
function insertDailyDiaryComment(ddCode, ddSuCode, ddDate) {
	const ddComment = document.getElementsByClassName("ddComment")[0].value;
	const clientData = "dcDdCode=" + ddCode + "&dcDdSuCode=" + ddSuCode + "&dcDdDate=" + ddDate + "&dcContent=" + ddComment;
	postAjaxJson("InsertDailyDiaryComment", clientData, "dailyDiaryComment");
	swal("댓글등록", "댓글 등록을 성공하였습니다!", "success", { button: "확인"});
}

// 2. 댓글 수정
function updateDailyDiaryComment(ddCode, ddSuCode, dcCode, dcDate, ddDate) {
	const updDbComment = document.getElementsByClassName("updDbComment")[0].value;
	const clientData = "dcDdCode=" + ddCode + "&dcCode=" + dcCode + "&dcDdSuCode=" + ddSuCode + "&dcDate=" + dcDate + "&dcContent=" + updDbComment + "&dcDdDate=" + ddDate;

	postAjaxJson("UpdateDailyDiaryComment", clientData, "dailyDiaryComment");
	swal("댓글수정", "댓글 수정을 성공하였습니다!", "success", { button: "확인"});
}

// 3. 댓글 삭제
function deleteDailyDiaryComment(ddCode, ddSuCode, dcCode, dcDate, ddDate) {
	const clientData = "dcDdCode=" + ddCode + "&dcCode=" + dcCode + "&dcDdSuCode=" + ddSuCode + "&dcDate=" + dcDate + "&dcDdDate=" + ddDate;
	
    // 경고
    swal({
        title: "댓글 삭제",
        text: "댓글을 삭제하시겠습니까?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {
        		postAjaxJson("DeleteDailyDiaryComment", clientData, "dailyDiaryComment");
        		swal("댓글 삭제", "댓글 삭제를 성공하였습니다!", "success", { button: "확인"});
            } else {
                swal("삭제를 취소하셨습니다.");
            }
        });
}

//4. 댓글 콜백
function dailyDiaryComment(ajaxData){
	console.log(ajaxData);
	const ajax = JSON.parse(ajaxData);
	const suCode = ajax.suCode;
	const ddComment = ajax.ddComment;

	document.getElementsByClassName("ddComment")[0].value = "";
	let commentList = document.getElementById("commentList");
	commentList.innerHTML = "";
	//댓글
	let comment = "", input = "";
	
	
	
	for(i = 0; i < ddComment.length; i++) {
		comment += "<div class = 'comment " + i + "'>";
		// 프로필 사진이 없을 경우 기본 이미지
		if(ddComment[i].suPhoto != null) {
			comment += "<img class='profileImage' src=" + ddComment[i].suPhoto + ">";
		} else {
			comment += "<img class='profileImage' src='/res/img/profile_default.png'>";
		}
	
		// 닉네임
		comment += "<div class = 'suNickname'>" +  ddComment[i].suNickname + "</div>";
	
		// 댓글 내용
		comment += "<div class='dcContent " + ddComment[i].dcDate + "'>" + ddComment[i].dcContent + "</div>";
		
		// 수정 삭제 버튼
		if(suCode === ddComment[i].dcSuCode) {
			comment += "<div class = 'ueBtn '" + ddComment[i].dcDate+ ">"
			comment += "<i class='fa-solid fa-pen mUpdBtn editBtn' onClick='updateInput(" + ddComment[i].dcDdCode + "," + ddComment[i].dcDdSuCode + "," + ddComment[i].dcCode + "," + ddComment[i].dcDate + "," + ddComment[i].dcDdDate +")'></i>";
			comment += "<i class='fa-solid fa-trash-can mDelBtn editBtn' style ='margin-left: 12%;' onClick='deleteDailyDiaryComment(" + ddComment[i].dcDdCode + "," + ddComment[i].dcDdSuCode + "," + ddComment[i].dcCode + "," + ddComment[i].dcDate + "," + ddComment[i].dcDdDate +")'></i>";
			comment += "</div>"
		} 
		comment += "</div>";
	}
	
	
	commentList.innerHTML += comment;
}

//피드 클릭시 확대
function getFeed(ddCode){
	const clientData = "ddCode=" + ddCode;

	postAjaxJson("GetDailyDiaryFeed", clientData, "viewFeed");
}

//피드 콜백
function viewFeed(ajaxData) {
	const ajax = JSON.parse(ajaxData);
	const suCode = ajax.suCode;
	const ddComment = ajax.ddComment;
	const ddFeed = ajax.ddFeed;
	
	let modal = document.querySelector(".modal");
	let modalHead = document.querySelector(".modal_head");
	let modalContent = document.querySelector(".modal_content");
	let modalFoot = document.querySelector(".modal_foot");
	modal.style.display = "block";

	modalHead.innerHTML = "<div class='viewFeedHead'>"
	+"<img style=\"filter: drop-shadow(10px 6px 6px #5D5D5D)\" src=\'" + ddFeed.dpLink + "'>"
	+"<i class=\"fa-solid fa-xmark closeBtn \" onclick =\"modalClose('')\"style=\"float: right;position: absolute; right: 26px;\"></i>"
	+"<div class='viewLike' onClick='dailyDiaryLike(" + ddFeed.ddCode + "," + ddFeed.ddDate + "," + ddFeed.suCode + ")'>❤ " + ddFeed.likes + "</div>"
	+"</div>";
	modalContent.innerHTML = "<div id='d_content'>" 
	+"<div id='viewFeedDate'>" 
	+ "<div id='feedInfo'><span id='writer'>" + ddFeed.ddSuName+"</span>"
	+ "<span id='date'>" + ddFeed.ddDate.substring(0, 4) + "년 " + ddFeed.ddDate.substring(4, 6) + "월 " + ddFeed.ddDate.substring(6, 8) + "일 " + ddFeed.ddDate.substring(8, 10) + ":" + ddFeed.ddDate.substring(10, 12) + ":" + ddFeed.ddDate.substring(12, 14)
	+ "</span></div>"
	+"</div>"
	+"<div id='viewFeedContent' class='scrollBar'>" + ddFeed.ddContent + "</div></div>";
	if("${accessInfo.suCode}" == ddFeed.suCode) {
		let area = document.querySelector("#viewFeedDate");
		area.innerHTML += "<div id='UDIcon'><i class='fa-solid fa-pen updBtn editBtn' onClick='feedUpdateInput(" + ddFeed.ddCode + ")' style='margin-right:20%'></i><i class='fa-solid fa-trash-can delBtn editBtn' onClick='deleteDailyDiaryFeed(" + ddFeed.ddCode + ")'></i></div>";
	}
	
	let viewLike = document.querySelector(".viewLike");
	if(ddFeed.like) {
		viewLike.className = "viewLike myViewLike";
	} else {
		viewLike.className = "viewLike";
	}
		
	//댓글
	let comment = "", input = "";
	comment += "<div id='commentList' class='scrollBar'>";
	
	for(i = 0; i < ddComment.length; i++) {
		comment += "<div class = 'comment " + i + "'>";
		// 프로필 사진이 없을 경우 기본 이미지
		if(ddComment[i].suPhoto != null) {
			comment += "<img class='profileImage' src=" + ddComment[i].suPhoto + ">";
		} else {
			comment += "<img class='profileImage' src='/res/img/profile_default.png'>";
		}
	
		// 닉네임
		comment += "<div class = 'suNickname'>" +  ddComment[i].suNickname + "</div>";
	
		// 댓글 내용
		comment += "<div class='dcContent " + ddComment[i].dcDate + "'>" + ddComment[i].dcContent + "</div>";
		
		// 수정 삭제 버튼
		if(suCode === ddComment[i].dcSuCode) {
			comment += "<div class = 'ueBtn'>"
			comment += "<i class='fa-solid fa-pen mUpdBtn editBtn' onClick='updateInput(" + ddComment[i].dcDdCode + "," + ddComment[i].dcDdSuCode + "," + ddComment[i].dcCode + "," + ddComment[i].dcDate + "," + ddComment[i].dcDdDate +")'></i>";
			comment += "<i class='fa-solid fa-trash-can mDelBtn editBtn' style ='margin-left: 12%;' onClick='deleteDailyDiaryComment(" + ddComment[i].dcDdCode + "," + ddComment[i].dcDdSuCode + "," + ddComment[i].dcCode + "," + ddComment[i].dcDate + "," + ddComment[i].dcDdDate +")'></i>";
			comment += "</div>"
		} 
		comment += "</div>";
	}
	comment += "</div>";
	
	modalFoot.innerHTML = input;
	
	if(suCode != null){
		input += "<div style = 'margin-top: 9%;'>";
		input += "<input class=\"ddComment mEditInput\" />";
		input += "<button class=\"mMiniBtn btn ddbtn\" onClick=\"insertDailyDiaryComment("+ ddFeed.ddCode + "," + ddFeed.suCode + "," + ddFeed.ddDate + ")\">확인</button>";
		input += "</div>";
	}
	let d_content = document.querySelector("#d_content");
	d_content.innerHTML += comment;
	modalFoot.innerHTML += input;
}

function feedUpdateInput(ddCode) {
	let viewFeedContent = document.getElementById("viewFeedContent");
	let UDIcon = document.getElementById("UDIcon");
	let content = document.getElementById("viewFeedContent").textContent;

	viewFeedContent.innerHTML = "";
	viewFeedContent.innerHTML = "<textarea id='updContent'>" + content + "</textarea>";
	
	UDIcon.className="";
	UDIcon.innerHTML = "";
	UDIcon.innerHTML = "<button class='submitBtn btn' onClick='updateDailyDiaryFeed(" + ddCode + ")'>수정</button>";
}
//피드 내용 수정
function updateDailyDiaryFeed(ddCode) {
	
	let updContent = document.getElementById("updContent").value;
	
	const clientData = "ddCode=" + ddCode + "&ddContent=" + updContent;
	
	postAjaxJson("UpdateDailyDiaryFeed", clientData, "viewFeed");
	swal("피드 내용 수정", "피드 내용 수정을 성공하였습니다!", "success", { button: "확인"});
}


function deleteDailyDiaryFeed(ddCode) {
	
	swal({
        title: "피드 삭제",
        text: "피드를 삭제하시겠습니까?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {
            	let form = document.getElementById("serverForm");

            	form.appendChild(createInput("hidden","ddCode",ddCode,null,null));
            	
            	form.action = "DeleteDailyDiaryFeed";
            	form.method = "post";
            	
            	form.submit();
            	modalClose('');
            } else {
                swal("삭제를 취소하셨습니다.");
            }
        });
}

function dailyDiaryLike(ddCode, ddDate, suCode) {
	
	const clientData = "ddCode=" + ddCode + "&ddDate=" + ddDate + "&ddSuCode=" + suCode;
	postAjaxJson("DailyDiaryLike", clientData, "updLike");
}

function updLike(ajaxData) {
	const ajax = JSON.parse(ajaxData);
	
	let viewLike = document.querySelector(".viewLike");
	if(ajax.ddLike.like) {
		viewLike.className = "viewLike myViewLike";
	} else {
		viewLike.className = "viewLike";
	}
	
	viewLike.innerText = "❤ " + ajax.ddLike.likes;
}
function changeSort(){
	let form = document.getElementById("serverForm");
	let postSelect = document.getElementById("dailySelect");
	//option값을 저장
	let ddSort = postSelect.options[postSelect.selectedIndex].value;
	let optionText = postSelect.options[postSelect.selectedIndex].text;
	
	form.appendChild(createInput("hidden","ddSort",ddSort,null,null));
	
	form.action = "ChangeDailyDiary";
	form.method = "get";
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
				<div id="rightArea_top">
					<div id="sortArea">
						<select id="dailySelect" onChange="changeSort()">
							<option value = "none" selected disabled>정렬순서</option>
							<option value = "newList">최신순</option>
							<option value = "oldList">오래된순</option>
							<option value = "likeList">좋아요순</option>
						</select>
					</div>
					<div id="writeFeedArea">
						<input id="writeFeed"type="button" class="writeBtn btn" style="cursor:pointer;" onClick="moveWriteFeed()" value="글쓰기" >
					</div>
				</div>
				<div id="rightArea_middle" class="scrollBar">
					<ul id="feedList"></ul>
				</div>
			</div>
		</div>
		<div class="modal">
            <div class="ddModal_body">
				<div class="modal_head">
				</div>
				<div class="modal_content"></div>
				<div class="modal_foot"></div>
            </div>
        </div>
	</div>
	<form id="serverForm"></form>
</body>

</html>