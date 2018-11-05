"use strict";
var connected= false;
var nombre="";
var flag =false;
var ConnectionManager={
	isConnected: function() {
		var request = new XMLHttpRequest();	
		request.open("get", "../jsp/isConnected.jsp");
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		request.onreadystatechange = function() {
			if (request.readyState===4) {
				var respuesta=JSON.parse(request.responseText);
				if (respuesta.result==="OK") {
					connected = true;
					nombre=respuesta.nombre;
				}	
			}
			else if (request.readyState<4){
				console.log("No ha realizado la peticion con exito");
			}
			flag=true;
		};
		request.send();	
	},
	logout: function(){
		var request = new XMLHttpRequest();	
		request.open("get", "../jsp/logout.jsp");
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		request.onreadystatechange=function() {
			if (request.readyState===4) {
				var respuesta=JSON.parse(request.responseText);
				if (respuesta.result==="OK") {
					 location.href ="../index.html";
				}
			}
		};
		request.send();	
	},
};
