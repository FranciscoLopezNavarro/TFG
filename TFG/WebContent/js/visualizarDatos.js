"use strict";
//Get select object


function seleccion() {
	var objSelect = document.getElementById("comboAsignaturas");

	//Set selected
	var selectedOption = objSelect.options[objSelect.selectedIndex].value;
	//console.log(selectedOption)

	if(selectedOption =>1){
		console.log("index es 1 o mas ");	
		//setTimeout("actualizar()",2000);
		mostrarAsigElegida(objSelect.options[objSelect.selectedIndex].text);
	}

}

function mostrarAsigElegida(asignatura) {

	var div = document.getElementById("tituloAsignatura");
	div.style.display ='';
	$('br').remove();
	document.getElementById("asigElegida").innerHTML = "Asignatura elegida: " + asignatura;
	
}

function cerrarAsigElegida() {
	div = document.getElementById("tituloAsignatura");
	div.style.display='none';
}

//Función para actualizar cada 4 segundos(4000 milisegundos)
function actualizar(){location.reload(true);}
window.setInterval("actualizar()",300000);