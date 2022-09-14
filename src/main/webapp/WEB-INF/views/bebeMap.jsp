<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>지도</title>
<script src="/res/js/agiMain.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/bebeMap.css">
<!-- 알림창 꾸미기 -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<!-- 카카오 지도 스크립트 -->
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=2afdabad57ed92e1cc9de5bd4baed321&libraries=services,clusterer,drawing"></script>
<!-- 카카오 스크립트 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<!-- 네이버 스크립트 -->
<script	src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
<script>
	Kakao.init('2afdabad57ed92e1cc9de5bd4baed321');
	function getInfo() {
		let accessArea = document.getElementById("accessArea");
		if ("${accessInfo.type}" != null && "${accessInfo.type}" != "") {
			accessArea.innerHTML = "";
			accessArea.innerHTML = "<span> ${accessInfo.suNickName}님 </span>";
			if ("${accessInfo.type}" == "kakao") {
				accessArea.innerHTML += "<span onclick=\"kakaoLogout();\">로그아웃</span>"
			} else if ("${ accessInfo.type }" == "naver") {
				accessArea.innerHTML += "<span onclick=\"naverLogout(); return false;\">로그아웃</span>"
			} else
				;
			accessArea.innerHTML += "<span onclick=\"movePage('MoveCompanyLoginPage')\">기업회원</span>";
		}
	}

	function openPopUp() {
		testPopUp = window.open("https://nid.naver.com/nidlogin.logout",
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
				url : '/v1/user/unlink',
				success : function(response) {
					logout();
				},
				fail : function(error) {
					console.log(error)
				},
			})
			Kakao.Auth.setAccessToken(undefined)
		}
	}
</script>
</head>
<body onload="getInfo()">
<div id="background">
    <div id="top">
        <div id="accessArea">
            <span onclick="movePage('MoveLoginPage')">로그인</span> <span
                onclick="movePage('MoveJoinPage')">회원가입</span> <span
                onclick="movePage('MoveCompanyLoginPage')">기업회원</span>
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
            <div class="map_wrap" style = "margin: 0 auto; width: 100%; height: 100%;">
                <div id="menu_wrap" class="bg_white" style = "width: 18rem; height: 40rem; float: left; ">
                    <div class="option">
                        <div>
                            <form onsubmit="searchPlaces(); return false;">
                                키워드 : <input type="text" value="인천일보아카데미" id="keyword" size="15"> 
                                <button type="submit">검색하기</button> 
                            </form>
                        </div>
                    </div>
                    <hr>
                    <ul id="placesList"></ul>
                    <div id="pagination"></div>
                </div>
                <div id="map" style="width: 60rem; height: 40rem; float: right; "></div>
            </div>
            <script>
        	/* 지도 모든 작업 */
        	
        	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
        	mapOption = {
        		center : new kakao.maps.LatLng(37.56996, 126.97839), // 지도의 중심좌표
        		level : 3, // 지도의 확대 레벨
        		mapTypeId : kakao.maps.MapTypeId.ROADMAP	// 지도종류
        	};

        	// 지도를 생성합니다    
        	var map = new kakao.maps.Map(mapContainer, mapOption);

        	// 지도 타입 변경 컨트롤을 생성한다
        	var mapTypeControl = new kakao.maps.MapTypeControl();

        	// 지도의 상단 우측에 지도 타입 변경 컨트롤을 추가한다
        	map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

        	// 지도에 확대 축소 컨트롤을 생성한다
        	var zoomControl = new kakao.maps.ZoomControl();

        	// 지도의 우측에 확대 축소 컨트롤을 추가한다
        	map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

        	// 주소-좌표 변환 객체를 생성합니다
        	var geocoder = new kakao.maps.services.Geocoder();

        	// 주소로 좌표를 검색합니다*********
        	geocoder.addressSearch('인천 미추홀구 매소홀로488번길 6-32', function(result, status) {

        		// 정상적으로 검색이 완료됐으면 
        		if (status === kakao.maps.services.Status.OK) {

        			var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

        			// // 결과값으로 받은 위치를 마커로 표시합니다
        			// var marker = new kakao.maps.Marker({
        			//     map: map,
        			//     position: coords
        			// });

        			// // 인포윈도우로 장소에 대한 설명을 표시합니다*********
        			// var infowindow = new kakao.maps.InfoWindow({
        			//     content: '<div style="width:10rem; height: 10rem;text-align:center; padding:6px 0;">인천일보아카데미</div>'
        			// });

        			// 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        			map.setCenter(coords);

        			selectedMarker = null; // 클릭한 마커를 담을 변수

        			// // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
        			// kakao.maps.event.addListener(marker, 'click', function () {
        			//     alert('마커를 클릭했습니다!');
        			//     infowindow.open(map, marker);
        			// });
        		}
        	});

        	// 마커를 담을 배열입니다
        	var markers = [];

        	// 장소 검색 객체를 생성합니다
        	var ps = new kakao.maps.services.Places();

        	// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
        	var infowindow = new kakao.maps.InfoWindow({
        		zIndex : 1
        	});

        	// 키워드로 장소를 검색합니다
        	searchPlaces();

        	// 키워드 검색을 요청하는 함수입니다
        	function searchPlaces() {

        		var keyword = document.getElementById('keyword').value;

        		if (!keyword.replace(/^\s+|\s+$/g, '')) {
        			alert('키워드를 입력해주세요!');
        			return false;
        		}

        		// 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
        		ps.keywordSearch(keyword, placesSearchCB);
        	}

        	// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
        	function placesSearchCB(data, status, pagination) {
        		if (status === kakao.maps.services.Status.OK) {

        			// 정상적으로 검색이 완료됐으면
        			// 검색 목록과 마커를 표출합니다
        			displayPlaces(data);

        			// 페이지 번호를 표출합니다
        			displayPagination(pagination);

        		} else if (status === kakao.maps.services.Status.ZERO_RESULT) {

        			alert('검색 결과가 존재하지 않습니다.');
        			return;

        		} else if (status === kakao.maps.services.Status.ERROR) {

        			alert('검색 결과 중 오류가 발생했습니다.');
        			return;

        		}
        	}

        	// 검색 결과 목록과 마커를 표출하는 함수입니다
        	function displayPlaces(places) {

        		var listEl = document.getElementById('placesList'), menuEl = document
        				.getElementById('menu_wrap'), fragment = document
        				.createDocumentFragment(), bounds = new kakao.maps.LatLngBounds(), listStr = '';

        		// 검색 결과 목록에 추가된 항목들을 제거합니다
        		removeAllChildNods(listEl);

        		// 지도에 표시되고 있는 마커를 제거합니다
        		removeMarker();

        		for (var i = 0; i < places.length; i++) {

        			// 마커를 생성하고 지도에 표시합니다
        			var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x), marker = addMarker(
        					placePosition, i), itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        			// 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        			// LatLngBounds 객체에 좌표를 추가합니다
        			bounds.extend(placePosition);

        			// 마커와 검색결과 항목에 click 했을 때
        			// 해당 장소에 인포윈도우에 장소명을 표시합니다
        			// mouseover 했을 때는 인포윈도우를 닫습니다
        			(function(marker, title, address, phone) {
        				kakao.maps.event.addListener(marker, 'click', function() {

        					displayInfowindow(marker, title, address, phone);
        				});

        				/* kakao.maps.event.addListener(marker, 'mouseover', function() {
        					infowindow.close();
        				}); */

        				itemEl.onmouseover = function() {
        					displayInfowindow(marker, title, address, phone);
        				};

        				itemEl.onmouseout = function() {
        					infowindow.close();
        				};
        			})(marker, places[i].place_name, places[i].address_name,
        					places[i].phone);

        			fragment.appendChild(itemEl);
        		}

        		// 검색결과 항목들을 검색결과 목록 Element에 추가합니다
        		listEl.appendChild(fragment);
        		menuEl.scrollTop = 0;

        		// 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
        		map.setBounds(bounds);
        	}

        	// 검색결과 항목을 Element로 반환하는 함수입니다
        	function getListItem(index, places) {

        		var el = document.createElement('li'), itemStr = '<span class="markerbg marker_'
        				+ (index + 1)
        				+ '"></span>'
        				+ '<div class="info">'
        				+ '   <h5>'
        				+ places.place_name + '</h5>';

        		if (places.road_address_name) {
        			itemStr += '    <span>' + places.road_address_name + '</span>'
        					+ '   <span class="jibun gray">' + places.address_name
        					+ '</span>';
        		} else {
        			itemStr += '    <span>' + places.address_name + '</span>';
        		}

        		itemStr += '  <span class="tel">' + places.phone + '</span>' + '</div>';

        		el.innerHTML = itemStr;
        		el.className = 'item';

        		return el;
        	}

        	// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
        	function addMarker(position, idx, title) {
        		var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        		imageSize = new kakao.maps.Size(36, 37), // 마커 이미지의 크기
        		imgOptions = {
        			spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
        			spriteOrigin : new kakao.maps.Point(0, (idx * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
        			offset : new kakao.maps.Point(13, 37)
        		// 마커 좌표에 일치시킬 이미지 내에서의 좌표
        		}, markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize,
        				imgOptions), marker = new kakao.maps.Marker({
        			position : position, // 마커의 위치
        			image : markerImage
        		});

        		marker.setMap(map); // 지도 위에 마커를 표출합니다
        		markers.push(marker); // 배열에 생성된 마커를 추가합니다

        		return marker;
        	}

        	// 지도 위에 표시되고 있는 마커를 모두 제거합니다
        	function removeMarker() {
        		for (var i = 0; i < markers.length; i++) {
        			markers[i].setMap(null);
        		}
        		markers = [];
        	}

        	// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
        	function displayPagination(pagination) {
        		var paginationEl = document.getElementById('pagination'), fragment = document
        				.createDocumentFragment(), i;

        		// 기존에 추가된 페이지번호를 삭제합니다
        		while (paginationEl.hasChildNodes()) {
        			paginationEl.removeChild(paginationEl.lastChild);
        		}

        		for (i = 1; i <= pagination.last; i++) {
        			var el = document.createElement('a');
        			el.href = "#";
        			el.innerHTML = i;

        			if (i === pagination.current) {
        				el.className = 'on';
        			} else {
        				el.onclick = (function(i) {
        					return function() {
        						pagination.gotoPage(i);
        					}
        				})(i);
        			}

        			fragment.appendChild(el);
        		}
        		paginationEl.appendChild(fragment);
        	}

        	// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
        	// 인포윈도우에 장소명을 표시합니다
        	function displayInfowindow(marker, title, address, phone) {
        		var content = '<div style="width:20rem; height: 20rem;text-align:center; padding:6px 0;">'
        				+ '<div>'+ title + '</div>'
        				+ '<div>'+ address + '</div>'
        				+ '<div>'+ phone + '</div>'
        				+ "<input type = 'button' value = '예약'  onClick = \"showReservation(\'" + title + ',' + address + ',' + phone + "\')\">"
        				+ '<div>댓글리스트 불러와라</div>' 
        				+ '</div>';

        		infowindow.setContent(content);
        		infowindow.open(map, marker);
        	}

        	// 검색결과 목록의 자식 Element를 제거하는 함수입니다
        	function removeAllChildNods(el) {
        		while (el.hasChildNodes()) {
        			el.removeChild(el.lastChild);
        		}
        	}

        	// 예약하기 (정보 가지고 DB 접근) 모달 띄우기
        	function showReservation(title, address, phone) {
        		document.getElementsByClassName("modal")[0].style.display = "block";
        		let modalBody = document.getElementsByClassName("modal_content")[0];
        		modalBody.innerText = title, address, phone;
        	}

        	// DB에서 가져온 정보로 모달 꾸미기 (아이선택, 의사선택, 캘린더(예약 가능, 불가능), 시간선택, 예약완료)
        	function callReservation(ajaxData) {
        		alert(ajaxData);
        		const ajax = JSON.parse(ajaxData);
        		alert(ajax);

        	}

        	// 예약완료
        	function reservation() {

        	}

        	// 댓글 등록
        	function insertMapComment() {

        	}

        	// 댓글 수정
        	function updateMapComment() {

        	}

        	// 댓글 삭제
        	function deleteMapComment() {

        	}

        	// CALLBACK
        	function mapComment(ajaxData) {
        		alert(ajaxData);
        		const ajax = JSON.parse(ajaxData);
        		alert(ajax);

        		swal("요청", "요청하신 작업을 완료하였습니다!", "success", {
        			button : "완료"
        		});
        	}
            </script>
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