"use strict";
function registrar(){
  var p = {
      id : document.getElementById("id").value,
      pwd1 : document.getElementById("pwd1").value,
      pwd2 : document.getElementById("pwd2").value,
      nombre: document.getElementById("nombre").value
  };
  var request = new XMLHttpRequest();
	request.open("post", "../jsp/registrar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if(request.readyState === 4){
			var respuesta = JSON.parse(request.responseText);
			console.log(respuesta);
			if(respuesta.result ==="OK"){
				location.href="../html/main.html";
				console.log("Usuario creado con éxito");
			}else{
			
				alert(respuesta.mensaje);

			}
		}
	};
  request.send("p=" + JSON.stringify(p));
}