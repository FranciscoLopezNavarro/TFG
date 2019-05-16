"use strict";
$(document).ready(function(){
    $("#div_login").load("login.html");
});


function validate(){
	var login = {
		id : document.getElementById("id").value,
		pwd : document.getElementById("password").value
	}
	var request = new XMLHttpRequest();
	request.open("post", "../jsp/login.jsp",false);
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
	    if(request.readyState == 4 && request.status == 200){
		var respuesta = JSON.parse(request.responseText);
		if(respuesta.result ==="OK"){
		    $("#div_login").remove();
		    location.href = "../html/main.html";	

		}else{
		    alert(respuesta.mensaje);
		}
	    }
	};
	request.send("login="+JSON.stringify(login));
}

