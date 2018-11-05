"use strict";
$(document).ready(function(){
	$("#div_login").load("login.html");
});
$(function() {
	$('#login-form-link').click(function(e) {
		$("#login-form").delay(100).fadeIn(100);
		$("#register-form").fadeOut(100);
		$('#register-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
		
	});
	$('#register-form-link').click(function(e) {
		$("#register-form").delay(100).fadeIn(100);
		$("#login-form").fadeOut(100);
		$('#login-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});
});
function validate(){
	location.href="../main.html";
	var login = {
			id : document.getElementById("id").value,
			pwd : document.getElementById("password").value
	}
	var request = new XMLHttpRequest();
	request.open("post", "jsp/login.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if(request.readyState === 4){
			var respuesta = JSON.parse(request.responseText);
			if(respuesta.result ==="OK"){
				location.href="../main.html";
				remove("#div_login");
				console.log("Se ha loggeado con exito");
			}else{
				//TODO Reflejar el mensaje en rojo debajo del email
				alert(respuesta.mensaje);
			}
		}
	};
	request.send("login="+JSON.stringify(login));
	console.log(JSON.stringify(login));
}

function remove(id){
	var elem = document.getElementById(id);
	return elem.parentNode.removeChild(elem);
}
