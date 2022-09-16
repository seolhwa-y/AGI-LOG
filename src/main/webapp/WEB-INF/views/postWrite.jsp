<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">

<!-- 카카오 스크립트 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<!-- 네이버 스크립트 -->
<script	src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
<!-- 글쓰기 스크립트 -->
<script type="text/javascript" src="/res/se2/js/service/HuskyEZCreator.js" charset="utf-8"></script>
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

var submitObject = new Object();

function submitContents() {
	let form = document.getElementById("upload");
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);	// 에디터의 내용이 textarea에 적용됩니다.
	const files = submitObject.files;
	
	let fbTitle = document.getElementById("fbTitle").value;
	let fbContent = document.getElementById("ir1").value;

	// 에디터의 내용에 대한 값 검증
	if(fbTitle == "") {
		alert("제목을 입력해 주세요");
		return;
	}
	
	if(fbContent == "") {
		alert("내용을 입력해 주세요");
		return;
	}

	//폼에 값 추가
	form.appendChild(createInput("hidden","fbTitle",fbTitle,null,null));
	form.appendChild(createInput("hidden","fbContent",fbContent,null,null));
	
	//for (idx = 0; idx < files.length; idx++) {
    //	alert(files[idx].name);
    //	form.appendChild(createInput("hidden","files["+idx+"].fileName",files[idx].name, null, null));
	//}
	
	console.log(form);
	
	form.action = "InsertPost";
	form.method = "post";
	//form.enctype= "multipart/form-data";
	form.submit();
}

function uploadFile(){
	alert("aaaa");
	const form = document.getElementById("upload");
	form.method = "post";
	console.log(form);
	form.submit();
}

function readMultipleImage(input) {
    const multipleContainer = document.getElementById("multiple-container")
    
    submitObject = input;
    
    // 인풋 태그에 파일들이 있는 경우
    if(input.files) {
        
        console.log(input.files)
        // 유사배열을 배열로 변환 (forEach문으로 처리하기 위해)
        const fileArr = Array.from(input.files)
        multipleContainer.innerHTML = "";
        const $colDiv1 = document.createElement("div")
        const $colDiv2 = document.createElement("div")
        $colDiv1.classList.add("column")
        $colDiv2.classList.add("column")
        fileArr.forEach((file, index) => {
            const reader = new FileReader()
            const $imgDiv = document.createElement("div")   
            const $img = document.createElement("img")
            $img.classList.add("image")
            const $label = document.createElement("label")
            $label.classList.add("image-label")
            $label.textContent = file.name
            $imgDiv.appendChild($img)
            $imgDiv.appendChild($label)
            reader.onload = e => {
                $img.src = e.target.result
                
                $imgDiv.style.width = "280px"
            }
            
            console.log(file.name)
            if(index % 2 == 0) {
                $colDiv1.appendChild($imgDiv)
            } else {
                $colDiv2.appendChild($imgDiv)
            }
            
            reader.readAsDataURL(file)
        })
        multipleContainer.appendChild($colDiv1)
        multipleContainer.appendChild($colDiv2)
    }
}

/* 첨부파일 검증 
function validation(obj){
    const fileTypes = ['application/pdf', 'image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tif', 'application/haansofthwp', 'application/x-hwp'];
    if (obj.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    } else if (obj.size > (100 * 1024 * 1024)) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}*/
</script>

<style>

.insert {
    padding: 20px 30px;
    display: block;
    width: 600px;
    margin: 5vh auto;
    height: 15vh;
    border: 1px solid #dbdbdb;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
}
.insert .file-list {
    height: 50px;
    overflow: auto;
    border: 1px solid #989898;
    padding: 10px;
}
.insert .file-list .filebox p {
    font-size: 14px;
    margin-top: 10px;
    display: inline-block;
}
.insert .file-list .filebox .delete i{
    color: #ff5353;
    margin-left: 5px;
}

#multiple-container {
	margin-top:5%;
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    height: 550px;
    overflow-y: auto;
    overflow-x: hidden;
}
#multiple-container:hover{
    cursor: pointer;
}
.image {
    display: block;
    width: 100%;
}
.image-label {
    position: relative;
    bottom: 22px;
    left: 5px;
    color: white;
    text-shadow: 2px 2px 2px black;
}

#USdiv {
    width: 44%;
    height: 570px;
    float: right;
    position: relative;
    margin-top: -48%;
}

#input-multiple-image{
    display: block;
    position: absolute;
    width: 100%;
    height: 570px;
}
</style>

</head>
<body onload="getInfo()">
</head>
<body>
	<div id="background" id="body">
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
				<div style="width:54%">제목 : <input type="text" id="fbTitle" style="width:92%" placeholder="제목을 입력해 주세요"/></div>
				<textarea name="ir1" id="ir1" rows="40" cols="90"></textarea>
				<script type="text/javascript">
					var oEditors = [];
					nhn.husky.EZCreator.createInIFrame({
					 oAppRef: oEditors,
					 elPlaceHolder: "ir1",
					 sSkinURI: "/res/se2/SmartEditor2Skin.html",
					 fCreator: "createSEditor2"
					});
				</script>
				<form id="upload" enctype="multipart/form-data">
				<div id="USdiv">
					<input style="display: block;" type="file" name="files" id="input-multiple-image" multiple />
					<div id="multiple-container"></div>
					<input type="button" value="취소" style=" font-size:1.5rem;position: absolute;right: 12%;bottom: -8%;">
					<input type="button" onclick="submitContents();" value="작성" style="font-size:1.5rem;position: absolute;right: -1%;bottom: -8%;">
				</div>
				</form>
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
	<!-- <form id="upload" action="Upload" enctype="multipart/form-data">
		<input type="file" name="files" multiple />
	</form>
	<input type="button" value="파일전송" onClick="uploadFile()"/> -->
	<form id="serverForm"></form>
</body>
<script type="text/javascript">
const inputMultipleImage = document.getElementById("input-multiple-image")
inputMultipleImage.addEventListener("change", e => {
    readMultipleImage(e.target)
})

</script>
</html>