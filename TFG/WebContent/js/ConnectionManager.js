"use strict";
var connected= false;
var nick="";
var flag =false;
var google_user=false;
var ConnectionManager={
	isConnected: function() {
		var request = new XMLHttpRequest();	
		request.open("get", "../isConnected.jsp");
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		request.onreadystatechange=function() {
			if (request.readyState===4) {
				var respuesta=JSON.parse(request.responseText);
				if (respuesta.result==="OK") {
					connected = true;
					nick=respuesta.nick;
				}
				flag=true;
			}
		};
		request.send();	
	},
	logout: function(){
		var request = new XMLHttpRequest();	
		request.open("get", "../logout.jsp");
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
	
	logoutGoogle: function() {
		alert("desconectado google");
		ConnectionManager.logout();
		/*
	      var auth2 = gapi.auth2.getAuthInstance();
	      auth2.signOut().then(function () {
	        console.log('User signed out.');
	        ConnectionManager.logout();
	      });*/
    }
};
