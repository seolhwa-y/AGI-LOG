<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>지도</title>
<script src="/res/js/agiMain.js"></script>
<script src="/res/js/bebeMap.js"></script>
<script src="https://use.fontawesome.com/releases/v6.1.2/js/all.js"></script>
<link rel="stylesheet" href="/res/css/bebeMap/icomoon.css">
<link rel="stylesheet" href="/res/css/bebeMap/rome.css">
<link rel="stylesheet" href="/res/css/bebeMap/bootstrap.min.css">
<link rel="stylesheet" href="/res/css/bebeMap/style.css">
<link rel="stylesheet" href="/res/css/agiMain.css">
<link rel="stylesheet" href="/res/css/bebeMap.css">
<!-- 알림창 꾸미기 -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<!-- 카카오 지도 스크립트 -->
<script type="text/javascript" src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2afdabad57ed92e1cc9de5bd4baed321&libraries=services,clusterer,drawing"></script>
<!-- 카카오 스크립트 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<!-- 네이버 스크립트 -->
<script	src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
<style>
.keyword{
    margin-left: -0.4rem;
}
.writeBtn{
	border: 1px solid dimgray;
}
.mResBtn{
    margin: 0 auto;
    width: 200px;
}
.placeinfo{
text-align: center;
}
.mcContent{
    width: 280px;
}
#commentList{

    border: 1px solid dimgray;
    margin: 0 auto;
    border-radius: 10px;
}
.editBtn{
	margin-right: 6px;
}
.time{
	cursor: pointer;
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
			} else {
				accessArea.innerHTML += "<span onclick=\"movePage('MoveCompanyLoginPage')\">기업회원</span>";
			}
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
<script type="importmap">
        {
          "imports": {
            "three": "https://unpkg.com/three@0.141.0/build/three.module.js",
            "GLTFLoader" : "https://unpkg.com/three@0.141.0/examples/jsm/loaders/GLTFLoader.js"
          }
        }
        </script>
        <script type="module">
            import {GLTFLoader} from 'GLTFLoader';
            import * as THREE from 'three';
            
            // 3D 보여주기
            let scene = new THREE.Scene();
            let renderer = new THREE.WebGLRenderer({
                canvas : document.querySelector("#canvas"),
                antialias : true
            });
            
            renderer.outputEncoding = THREE.sRGBEncoding;
    
            // 3D 카메라
            let camera = new THREE.PerspectiveCamera(25, 1);
            camera.position.set(0, 0, 10);
    
            // 3D 배경
            scene.background = new THREE.Color('white');
    
            // 3D 조명
            let light = new THREE.DirectionalLight(0xffffff, 1);
            light.position.set(0, 20, 100);
            scene.add(light);
    
            let loader = new GLTFLoader();
            loader.load("/res/img/dr.long_leaf/scene.gltf", function(gltf){
                //alert("병원 예약 완료");
                scene.add(gltf.scene);
    
                function animate(){
                    requestAnimationFrame(animate);
                    gltf.scene.rotation.y += 0.03;
                    renderer.render(scene, camera);
                }
                animate();
            });
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
			<div class="wrapper">
				<div class="typing">
					<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-geo-alt-fill" viewBox="0 0 16 16">
						<path d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10zm0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6z"/>
					</svg>
				   	회원가입 시 등록한 주소 기반으로 검색됩니다.
				</div>
	        </div>
            <div class="map_wrap" style = "margin: 0 auto; width: 100%; height: 90%;">
                <div id="menu_wrap" class="bg_white scrollBar" style = "width: 23%; height: 100%; float: left;">
                    <div class="option">
                        <div>
                            <form onsubmit="searchPlaces(); return false;">
                                <span class="keyword">키워드 :</span> <input type="text" value="" id="keyword" size="15"> 
                                <button type="submit" class="writeBtn btn">검색</button> 
                            </form>
                        </div>
                    </div>
                    <hr>
                    <ul id="placesList"></ul>
                    <div id="pagination"></div>
                </div>
                <div id="map" style="float: right; margin: 10px 0 30px 10px; width: 70%; height: 100%;"></div>
            </div>
            <script>
         	// 지도 소아과 전문의 병원 호출...
    		let bmList = JSON.parse('${bmList}');
    		let suInfo = JSON.parse('${suInfo}');
    		const suAddress = suInfo.suAddress;
    		//callBabyListInfo(bmList);
            
            
            
            /* 지도 모든 작업 */
                        	/************** 기본 기능 **************/
                var mapContainer = document.getElementById('map'),  
            	mapOption = {
            			// 기본 주소에서 받아온 위도 경도 넣기
            		center : new kakao.maps.LatLng(37.403419311975, 126.72003443712), // 지도의 중심좌표
            		level : 3, // 지도의 확대 레벨
            		mapTypeId : kakao.maps.MapTypeId.ROADMAP	// 지도종류
            	};
             
            	// 지도를 생성합니다    
            	var map = new kakao.maps.Map(mapContainer, mapOption);

            	mapContainer.style.width = '70%';
            	mapContainer.style.height = '100%';

            	map.relayout();

            	// 지도 타입 변경 컨트롤을 생성한다
            	var mapTypeControl = new kakao.maps.MapTypeControl();

            	// 지도의 상단 우측에 지도 타입 변경 컨트롤을 추가한다
            	map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

            	// 지도에 확대 축소 컨트롤을 생성한다
            	var zoomControl = new kakao.maps.ZoomControl();

            	// 지도의 우측에 확대 축소 컨트롤을 추가한다
            	map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
            
            
            /************** 페이지 로딩시 작동 기능 **************/	
            
            // 페이지 로드했을때 서버에 가서 소아전문병원만 가져옴.
            function callBabyListInfo(json) {
             	// 지도 표시
             	displayPlaces(json, 1);
        	}
            

            	
            	// 마커를 담을 배열입니다
            	var markers = [];

            	// 장소 검색 객체를 생성합니다
            	var ps = new kakao.maps.services.Places();

            	// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
            	var infowindow = new kakao.maps.InfoWindow({
            		zIndex : 1
            	});
            	
            	// 주소-좌표 변환 객체를 생성합니다 :: 위도 경도로 변환
            	var geocoder = new kakao.maps.services.Geocoder();
            	
            	// 주소로 좌표를 검색합니다*********
            	geocoder.addressSearch(suAddress, function(result, status) {
     			console.log(result);
            		// 정상적으로 검색이 완료됐으면 
            		if (status === kakao.maps.services.Status.OK) {

            			var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            			//  결과값으로 받은 위치를 마커로 표시합니다
            			 marker = new kakao.maps.Marker({
            			     map: map,
            			     position: coords
            			 });

            			// 인포윈도우로 장소에 대한 설명을 표시합니다
            		 	 var infowindow = new kakao.maps.InfoWindow({
            			     content: '<div style="width:10rem; height: 10rem;text-align:center; padding:6px 0;">여기에 데이터 표기</div>'
            			 }); 

            			// 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            			map.setCenter(coords);

            			selectedMarker = null; // 클릭한 마커를 담을 변수

            			// 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
            			 kakao.maps.event.addListener(marker, 'click', function () {
            				 alert("회원가입 시 주소지 입니다.");
            			 });
            		}
            	}); 
            	
                // 검색 결과 목록과 마커를 표출하는 함수입니다
            	function displayPlaces(places, code) {
            	    var listEl = document.getElementById("placesList"),
            	      menuEl = document.getElementById("menu_wrap"),
            	      fragment = document.createDocumentFragment(),
            	      bounds = new kakao.maps.LatLngBounds(),
            	      listStr = "";

            	    // 검색 결과 목록에 추가된 항목들을 제거합니다
            	    removeAllChildNods(listEl);

            	    // 지도에 표시되고 있는 마커를 제거합니다
            	    removeMarker();
					
            	    let name = [], address = [], tell = [];
            	    
            	    for (var i = 0; i < places.length; i++) {
              	      if(code == 0){
               	      	  name.push(places[i].place_name); address.push(places[i].road_address_name); tell.push(places[i].phone); // 0
               	      } else if(code == 1){
						  name.push(places[i].name); address.push(places[i].address); tell.push(places[i].tell); // 1
               	      }
            	      
            	      // 마커를 생성하고 지도에 표시합니다
            	      var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            	        marker = addMarker(placePosition, i)
            	        itemEl = getListItem(i, places[i], code); // 검색 결과 항목 Element를 생성합니다

            	      // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
            	      // LatLngBounds 객체에 좌표를 추가합니다
            	      bounds.extend(placePosition);

            	      // 마커와 검색결과 항목에 mouseover 했을때
            	      // 해당 장소에 인포윈도우에 장소명을 표시합니다
            	      // mouseout 했을 때는 인포윈도우를 닫습니다
            	      (function (marker, name, address, tell) {
            	        kakao.maps.event.addListener(marker, "click", function () {
            	        	displayInfowindow(marker, name, address, tell);
            	        });

                        itemEl.onmouseover =  function () {
                            displayInfowindow(marker, name, address, tell);
                        };

        				itemEl.onmouseout = function() {
        					infowindow.close();
        				};
            	      })(marker, name[i], address[i], tell[i]); // 0

            	      fragment.appendChild(itemEl);
            	    }

            	    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다

            	    listEl.appendChild(fragment);
            	    menuEl.scrollTop = 0;
					
 
            	    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
            	    map.setBounds(bounds);
            	  }
                	
            	// 검색결과 항목을 Element로 반환하는 함수입니다
            	function getListItem(index, places, code) {
            		let name = "", address = "", tell = "";
            		
              	    if(code == 0){
               	      	name = places.place_name, address = places.road_address_name, tell = places.phone; // 0
               	    } else if(code == 1){
						name = places.name, address = places.address, tell = places.tell; // 1
               	    }
          	      	
            		var el = document.createElement('li'), itemStr = '<span class="markerbg marker_'
            				+ (index + 1)
            				+ '"></span>'
            				+ '<div class="info">'
            				+ '   <h5>'
            				+ name + '</h5>';

            		if (name) {
            			itemStr += '    <span>' + address + '</span>'
            					+ '   <span class="jibun gray">' + address
            					+ '</span>';
            		} else {
            			itemStr += '    <span>' + address + '</span>';
            		}

            		itemStr += '  <span class="tel">' + tell + '</span>' + '</div>';

            		el.innerHTML = itemStr;
            		el.className = 'item';

            		return el;
            	}
            	
            	// 검색결과 목록의 자식 Element를 제거하는 함수입니다
            	function removeAllChildNods(el) {
            		while (el.hasChildNodes()) {
            			el.removeChild(el.lastChild);
            		}
            	}
            	
            	// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
            	function addMarker(position, idx) {
            		
            		var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
            		imageSize = new kakao.maps.Size(36, 37), // 마커 이미지의 크기
            		imgOptions = {
            			spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            			spriteOrigin : new kakao.maps.Point(0, (idx * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            			offset : new kakao.maps.Point(13, 37)
            		// 마커 좌표에 일치시킬 이미지 내에서의 좌표
            		},  markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions), 
            			marker = new kakao.maps.Marker({
	            			map: map, // 마커를 표시할 지도
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
            		var paginationEl = document.getElementById('pagination'),
            		fragment = document.createDocumentFragment(),
            		i;

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
             	function displayInfowindow(marker, name, address, tell) {
            		console.log("마커 클릭 :: " + name);
            		let clientData = "coName=" + name + "&coAddress=" + address + "&coPhone=" + tell;
            		
            		markers = marker;
            		//postAjaxJson("ViewCompanyInfo", clientData, "callDisplayInfo");
            	
            		let content = '<div class = "placeinfo" >';
        			content	+= '<div>'+ name + '</div>';
        			content	+= '<div>'+ address + '</div>';
        			content	+= '<div>'+ tell + '</div>';
    		    	content += '</div>'; 
    		    	 
    		    	postAjaxJson("ViewCompanyInfo", clientData, "callDisplayInfo");
    		    	
            		infowindow.setContent(content);
            		infowindow.open(map, marker);
            	} 
            	
             // 마크 눌렀을 때 CALLBACK
             	function callDisplayInfo(ajaxData) {
            		console.log("여기야₩~~!~!~");
            		console.log(ajaxData);
            		let infoWindow = document.getElementsByClassName("placeinfo")[0];
          		
            		if(ajaxData == "") {
            			infoWindow.innerHTML += "<div id='commentList'><h6>아기로그에 등록된 업체가 아닙니다.<br> 예약을 하기 위해서는 해당 업체에 연락해주세요.</h6></div>";
            			return;
            		} else {
                		const mcCommentList = JSON.parse(ajaxData);
                		const suCode = mcCommentList.suCode;
                		const mcComment = mcCommentList.mcComment;
                		const coInfo = mcCommentList.coInfo;
						console.log(coInfo);
    	    			let mcList = "";
    	    		
    	 	        	mcList += "<input type = 'button' value = '예약' class='mResBtn btn' onClick = \"showReservation(\'" + coInfo.coCode + "\')\">";
    	    			
    	    			mcList += "<div id='commentList' class='scrollBar'>";
    	    			
    	    			if(mcComment != ""){
	    	    			for(i = 0; i < mcComment.length; i++) {
	    	    				mcList += "<div class = 'comment " + i + "'>";
	    	    				// 프로필 사진이 없을 경우 기본 이미지
	    	    				if(mcComment[i].suPhoto != null) {
	    	    					mcList += "<img class='profileImage' src=" + mcComment[i].suPhoto + ">";
	    	    				} else {
	    	    					mcList += "<img class='profileImage' src='/res/img/profile_default.png'>";
	    	    				}
	    	    			
	    	    				// 닉네임
	    	    				mcList += "<div class = 'suNickname'>" +  mcComment[i].suNickname + "</div>";
	    	    			
	    	    				// 댓글 내용
	    	    				mcList += "<div class='dcContent " + mcComment[i].mcDate + "'>" + mcComment[i].mcContent + "</div>";
	    	    				
	    	    				// 수정 삭제 버튼
	    	    				if(suCode === mcComment[i].mcSuCode) {
	    	    					mcList += "<i class='fa-solid fa-pen updBtn editBtn' onClick='updateInput(" + mcComment[i].coCode + "," + mcComment[i].mcCode + "," + mcComment[i].mcDate + "," + mcComment[i].mcSuCode + ")'></i>";
	    	    					mcList += "<i class='fa-solid fa-trash-can delBtn editBtn' onClick='deleteMapComment(" + mcComment[i].coCode + "," + mcComment[i].mcCode + "," + mcComment[i].mcDate + "," + mcComment[i].mcSuCode + ")'></i>";
	    	    				} 
	    	    				mcList += "</div>";
	    	    			}
    	    			}
    	    			mcList += "</div>";
    	    		
    	    			mcList += "<div style='display: flex; align-items: center; justify-content: space-evenly;'>";
    	    			mcList += "<input class=\"mcContent mEditInput\" maxlength='30'/>";
    	    			mcList += "<button class=\"mMiniBtn btn\" onClick=\"insertMapComment("+ coInfo.coCode + ")\">확인</button>";
    	    			mcList += "</div>";
    	    			
    	    			infoWindow.innerHTML += mcList;
    	    			console.log(mcList);
    	        		infowindow.setContent(infoWindow);
            		}
        			//swal("요청", "요청하신 작업을 완료하였습니다!", "success", { button: "완료"});
            	}
            	

            

        	/************** 키워드 검색 기능 **************/
        	function searchPlaces() {
        		let keyword = document.getElementById('keyword').value;

        		if (!keyword.replace(/^\s+|\s+$/g, '')) {
        			alert('키워드를 입력해주세요!');
        			return false;
        		}
        		callSearchPlaces(keyword);
        		if(keyword.includes("동") || keyword.includes("구")){
        			console.log("1");
        			callSearchPlaces(keyword);
        		}  else {
        			console.log("2");
        			callSearchPlaces(keyword);
/*             		postAjaxJson("GetBabyListInfo", "", "callSearchPlaces"); */
        		} 

        	}
        	
        	// 키워드 검색 후 콜백 함수 :: 특정 위치 지정
        	function callSearchPlaces(ajaxData) {
        		let keyword = document.getElementById('keyword').value;
        		
        		if(keyword.includes("동") || keyword.includes("구")){
        			console.log("1");
            		// 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
            		ps.keywordSearch(keyword, placesSearchCB, {
            			// 특정 위치에서 검색
            		    location: new kakao.maps.LatLng(37.566826, 126.9786567)
            		});

        		}  else {
        			console.log("2");
            		// 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
            		ps.keywordSearch(keyword + suAddress, placesSearchCB, {
            			// 특정 위치에서 검색
            		    location: new kakao.maps.LatLng(37.566826, 126.9786567)
            		});
        		} 
        	}

        	// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
        	function placesSearchCB(data, status, pagination) {
        		if (status === kakao.maps.services.Status.OK) {
        			// 정상적으로 검색이 완료됐으면
        			// 검색 목록과 마커를 표출합니다
        			displayPlaces(data, 0);

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

            </script>
        </div>
    </div>
    <div class="modal">
        <div class="modal_body">
            <div class="modal_head">
                <i class="fa-solid fa-xmark closeBtn editBtn" style = "float: right;" onClick ="modalClose()"></i><br />
            </div>
            <div class="modal_content">
            		<div class = "part1">
						<!-- 아이선택 -->
						<div id="selectBaby">아이선택 : </div>
						<!-- 의사선택 -->
						<div id="selectDoctor">의사선택 : </div>
						<!-- 달력 -->
						<br/>
						<div class="content">
							<div class="container text-left">
								<div class="row justify-content-center">
									<div class="col-md-10 text-center">
										<input type="text" class="form-control w-25 mx-auto mb-3"
											id="result" placeholder="Select date" disabled="" style = "display:none;">
										<form action="#" class="row">
											<div class="col-md-12">
												<div id="inline_cal">
													<!-- 캘린더 영역 -->
												</div>
											</div>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class = "part2" >
						<!-- 시간선택 -->
						<div class="resTime">
							<div class="mon"></div>
							<div class="aft"></div>
						</div>
						<!-- 토글스위치 -->
						<div class="checkHealth">
							건강기록 공개 : <input type="checkbox" id="toggle" hidden>
							<label for="toggle" class="toggleSwitch"> <span
								class="toggleButton"></span>
							</label>
						</div>
					</div>
					<div id = "cans">
	    				<canvas id = "canvas"></canvas>
	    				<h1>예약신청 되었습니다.</h1>
	    				<h3 style="color:#b0adad">예약이 완료가 되면 문자가 도착합니다.</h3>
    				</div>
				</div>
            <div class="modal_foot">
            	<input type="button" class="mBtnO btn" value = "예약완료" onclick="reservation()">
            </div>
        </div>
    </div>
</div>
<form id="serverForm"></form>
<script src="/res/js/bebeMap/jquery-3.3.1.min.js"></script>
<script src="/res/js/bebeMap/popper.min.js"></script>
<script src="/res/js/bebeMap/bootstrap.min.js"></script>
<script src="/res/js/bebeMap/rome.js"></script>
<script src="/res/js/bebeMap/main.js"></script>
</body>
</html>