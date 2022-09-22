//모달닫기
function modalClose() {
	let modal = document.querySelector(".modal");
	let modalHead = document.querySelector(".modal_head");
	let modalContent = document.querySelector(".modal_content");
	let modalFoot = document.querySelector(".modal_foot");

	modal.style.display = "none";
	/*         		modalHead.innerHTML = "";
					modalContent.innerHTML = "";
					modalFoot.innerHTML = ""; */
}

/************** 댓글 전체 기능 **************/

// 댓글 수정 버튼
function updateInput(coCode, mcCode, mcDate, mcSuCode) {
	let mcContent = document.getElementsByClassName(mcDate)[0];

	mcContent.innerHTML = "";
	mcContent.innerHTML += "<input class ='updMcComment mEditInput'>";
	mcContent.innerHTML += "<button class='submitBtn btn' onClick='updateMapComment(" + coCode + "," + mcCode + "," + mcDate + "," + mcSuCode + ")'>확인</button>";
}

// 댓글 등록
function insertMapComment(coCode) {
	const mcContent = document.getElementsByClassName("mcContent")[0].value;
	const clientData = "coCode=" + coCode + "&mcContent=" + mcContent;

	postAjaxJson("InsertCompanyComment", clientData, "mapComment");

	document.getElementsByClassName("mEditInput")[0].value = "";

}

// 댓글 수정
function updateMapComment(coCode, mcCode, mcDate, mcSuCode) {
	const updMcComment = document.getElementsByClassName("updMcComment")[0].value;
	const clientData = "coCode=" + coCode + "&mcCode=" + mcCode + "&mcDate=" + mcDate + "&mcSuCode=" + mcSuCode + "&mcContent=" + updMcComment;

	postAjaxJson("UpdateCompanyComment", clientData, "mapComment");
}

// 댓글 삭제
function deleteMapComment(coCode, mcCode, mcDate, mcSuCode) {
	const clientData = "coCode=" + coCode + "&mcCode=" + mcCode + "&mcDate=" + mcDate + "&mcSuCode=" + mcSuCode;

	// 경고
	swal({
		title: "진짜로 삭제하십니까?",
		text: "삭제하면 되돌릴 수 없습니다.",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				postAjaxJson("DeleteCompanyComment", clientData, "mapComment");
			} else {
				swal("삭제를 취소하셨습니다.");
			}
		});
}

// 댓글 기능 :: CALLBACK
function mapComment(ajaxData) {
	const mcCommentList = JSON.parse(ajaxData);
	const suCode = mcCommentList.suCode;
	const mcComment = mcCommentList.mcComment;

	let commentList = document.getElementById("commentList");
	commentList.innerHTML = "";

	let comment = "";

	for (i = 0; i < mcComment.length; i++) {
		comment += "<div class = 'comment " + i + "'>";
		// 프로필 사진이 없을 경우 기본 이미지
		if (mcComment[i].suPhoto != null) {
			comment += "<img class='profileImage' src=" + mcComment[i].suPhoto + ">";
		} else {
			comment += "<img class='profileImage' src='/res/img/profile_default.png'>";
		}

		// 닉네임
		comment += "<div class = 'suNickname'>" + mcComment[i].suNickname + "</div>";

		// 댓글 내용
		comment += "<div class='dcContent " + mcComment[i].mcDate + "'>" + mcComment[i].mcContent + "</div>";

		// 수정 삭제 버튼
		if (suCode === mcComment[i].mcSuCode) {
			comment += "<i class='fa-solid fa-pen updBtn editBtn' onClick='updateInput(" + mcComment[i].coCode + "," + mcComment[i].mcCode + "," + mcComment[i].mcDate + "," + mcComment[i].mcSuCode + ")'></i>";
			comment += "<i class='fa-solid fa-trash-can delBtn editBtn' onClick='deleteMapComment(" + mcComment[i].coCode + "," + mcComment[i].mcCode + "," + mcComment[i].mcDate + "," + mcComment[i].mcSuCode + ")'></i>";
		}
		comment += "</div>";
	}
	comment += "</div>";

	commentList.innerHTML += comment;

	swal("요청", "요청하신 작업을 완료하였습니다!", "success", { button: "완료" });
}
/************** 예약 전체 기능 **************/

const month = { "01" : "January",  "02" : "February", "03" : "March", "04" : "April", "05" : "May", "06" : "June", "07" : "July", "08" : "August", "09" : "September", "10" : "October", "11" : "November", "12" : "December"};

// 예약하기 (정보 가지고 DB 접근) 모달 띄우기
function showReservation(resCoCode) {
	let clientData = "resCoCode=" + resCoCode;

	postAjaxJson("ShowReservation", clientData, "callReservation");
}

// DB에서 가져온 정보로 모달 꾸미기 (아이선택, 의사선택, 캘린더(예약 가능, 불가능), 시간선택, 예약완료)
function callReservation(ajaxData) {
	const ajax = JSON.parse(ajaxData);
	let babyList = ajax.babyList;
	let doctorList = ajax.doctorList;

	console.log(babyList);

	let modal = document.getElementsByClassName("modal")[0];
	modal.style.display = "block";


	let bbList = "", doList = "";

	// 아이선택 셀렉트
	bbList += "<select id='babyName' class='select'>";
	bbList += "<option disabled selected>아이를 선택하세요</option>";

	for (i = 0; i < babyList.length; i++) {
		bbList += "<option value=" + babyList[i].suCode + ":" + babyList[i].bbCode + ">" + babyList[i].bbName + "</option>";
	}

	bbList += "</select>";

	// 의사선택 셀렉트
	doList += "<select id='doctorName' class='select' onChange = 'getDoctorRes()'>";
	doList += "<option disabled selected>의사를 선택하세요</option>";

	for (i = 0; i < doctorList.length; i++) {
		doList += "<option value=" + doctorList[i].coCode + ":" + doctorList[i].doCode + ">" + doctorList[i].doName + "</option>";
	}

	doList += "</select>";
	document.getElementById("selectBaby").innerHTML += bbList;
	document.getElementById("selectDoctor").innerHTML += doList;
}

// 의사 선택시 날짜에 이벤트 추가
function getDoctorRes() {	
	let day = document.getElementsByClassName("rd-day-body");
	let babyName = document.getElementById("babyName");
	
	if(babyName.value == null) return swal("알림", "아이를 선택하세요!.", "waring", { button: "확인"});;
	
	for(i = 0; i < day.length; i++){
		day[i].addEventListener("dblclick", getResTime);
	}
}	

// 날짜 더블클릭 후 날짜 추출해서 시간 가져오기
function getResTime() {
	let result = document.getElementById("result").value;
	let result2 =  result.replace(/,/g, "");
	
	let date = result2.split(" ");
	let key = getKeyByValue(month, date[0]);
	let resDate = date[2] + key + date[1] + "000000";
	let doctor = document.getElementById("doctorName").value;
	let code = doctor.split(":");
	
	const clientData = " " + code[0] + "&resDoCode=" + code[1] + "&resDate=" + resDate;
	postAjaxJson("GetResTime", clientData, "callResTime");
}

// 시간 작업
function callResTime(ajaxData) {
	let res = JSON.parse(ajaxData);
	console.log(res);

	let mon = document.getElementsByClassName("mon")[0];
	let aft = document.getElementsByClassName("aft")[0];
	mon.innerHTML = "";
	aft.innerHTML = "";
	 
	// 해당 날짜가 열려있는 경우
	if (res != "") {
		for (i = 0; i < res.length; i++) {
			// 현재 예약자 인원수 
			if (res[i].resCount <= 5) {
				// 열린 예약시간
				if (res[i].resTime == "09" || res[i].resTime == "10" || res[i].resTime == "11" || res[i].resTime == "12") {
					mon.innerHTML += "<div class =\"time " + res[i].resTime + '\"onClick=\'cngTimeColer(' + res[i].resTime + ")'\>" + res[i].resTime + ": 00" + "</div>";
				} else if (res[i].resTime == "14" || res[i].resTime == "15" || res[i].resTime == "16" || res[i].resTime == "17") {
					aft.innerHTML += "<div class =\"time " + res[i].resTime + '\"onClick=\'cngTimeColer(' + res[i].resTime + ")'\>" + res[i].resTime + ": 00" + "</div>";
				}
			} else {
				if (res[i].resTime == "09" || res[i].resTime == "10" || res[i].resTime == "11" || res[i].resTime == "12") {
					mon.innerHTML += "<div class ='time " + res[i].resTime + '\"style = "background-color:#6c757d">' + res[i].resTime + ": 00" + "</div>";
				} else if (res[i].resTime == "14" || res[i].resTime == "15" || res[i].resTime == "16" || res[i].resTime == "17") {
					aft.innerHTML += "<div class ='time " + res[i].resTime + '\"style = "background-color:#6c757d">' + res[i].resTime + ": 00" + "</div>";
				}
			}
		}
	} else {
		//alert("해당 일자는 예약 불가능합니다.");
		swal("알림", "해당 일자는 예약 불가능합니다.", "waring", { button: "확인"});
	}

}

// 선택한 시간 변경
function cngTimeColer(time) {
	let color = document.getElementsByClassName(time)[0];
	let all = document.getElementsByClassName("time");
	
	for(i = 0; i < all.length; i++){
		all[i].style.backgroundColor = "rgb(255, 194, 204)";
	}
		//color.style.backgroundColor = "#f67280";	
		
	if (color.style.backgroundColor == "rgb(255, 194, 204)") {
        color.style.backgroundColor = "#f67280";
    } else {
        color.style.backgroundColor = "rgb(255, 194, 204)";
    } 
}

// 키 : 값 => 키값 뽑아내기
function getKeyByValue(object, value) {
  return Object.keys(object).find(key => object[key] === value);
}

// 예약완료
function reservation() {
	// 아이+유저
	let su = document.getElementById("babyName").value;
	let code1 = su.split(":");
	let resSuCode = code1[0];
	let resBbCode = code1[1];
	console.log(code1);
	// 닥터+회사
	let co = document.getElementById("doctorName").value;
	let code2 = co.split(":");
	let resCoCode = code2[0];
	let resDoCode = code2[1];
	console.log(code2);
	// 날짜 
	let result = document.getElementById("result").value;
	let result2 =  result.replace(/,/g, "");
	let date = result2.split(" ");
	let key = getKeyByValue(month, date[0]);
	let resDate = date[2] + key + date[1] + "000000";
	console.log(resDate);
	// 시간
	let time = document.getElementsByClassName("time");
	let resTime = "";
	
	for(i = 0; i < time.length; i++){
		if (time[i].style.backgroundColor != "rgb(255, 194, 204)") {
			let s = time[i].innerText.split(":");
			resTime = s[0];
	    } 
	}
	console.log(resTime);
	// 건강일기 공개 여부
	let toggle = document.getElementById("toggle");
	let resAccess = "";
	
	if(toggle.checked) {
		resAccess = "0";
	} else {
		resAccess = "1";
	}
	console.log(resAccess);
	// ClientDate
	const clientData = "resSuCode=" + resSuCode + "&resBbCode=" + resBbCode + "&resCoCode=" + resCoCode + "&resDoCode=" + resDoCode + "&resDate=" + resDate + "&resTime=" + resTime + "&resAccess=" + resAccess;
	console.log(clientData);
	postAjaxJson("Reservation", clientData, "cpReservation"); 
}

function cpReservation(ajaxData) {
	document.getElementById("cans").style.display = "block";
	document.getElementsByClassName("part1")[0].style.display = "none";
	document.getElementsByClassName("part2")[0].style.display = "none";
	document.getElementsByClassName("modal_foot")[0].style.display = "none";
}

