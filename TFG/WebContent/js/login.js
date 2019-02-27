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
	request.open("post", "../jsp/login.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if(request.readyState === 4){
			var respuesta = JSON.parse(request.responseText);
			if(respuesta.result ==="OK"){
				location.href="../html/main.html";
				console.log("Se ha loggeado con exito");
				
			}else{
				alert(respuesta.mensaje);
			}
		}
		return false;
	};
	request.send("login="+JSON.stringify(login));
	console.log(JSON.stringify(login));
}

