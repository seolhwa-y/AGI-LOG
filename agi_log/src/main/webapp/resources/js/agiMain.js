function movePage(action) {
	let form = document.getElementById("serverForm");
	
	form.action = action;
	form.method = "get";
	form.submit();
}