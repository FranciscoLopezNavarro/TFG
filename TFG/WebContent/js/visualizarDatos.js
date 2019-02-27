"use strict";

function seleccion() {

    seleccionHistorico();
    seleccionCursoActual();
    mostrarAsigElegida($("#comboAsignaturas :selected").text());
}

$("input").change(function(){
    alert("The text has been changed.");
  }); 
function addRow()
{
  
    $("#tr_clone").clone(true).appendTo("#tablaCursoActual"); 
//    var new_name=document.getElementById("alumno").value;
//    var new_country=document.getElementById("prueba").value;
//    var new_age=document.getElementById("riesgo").value;
//
//    var table=document.getElementById("tablaCursoActual");
//    var table_len=(table.rows.length);
//    var row = table.insertRow(table_len).outerHTML=
//	"<tr id='row"+table_len+"'>" +
//	"<td contenteditable='true' id='alumno'></td>" +
//	"<td contenteditable='true' id='prueba'></td>" +
//	"<td contenteditable='true' id='riesgo'></td><td>" +
//	"<a><i class='fa fa-save' onclick='saveRow()'></i></a>" +
//	"<a><i class='fa fa-trash' onclick='deleteRow(this)'></i></a>" +
//	"</td>" +
//	"</tr>";

    
}
function deleteRow(row){
    var txt;
    var r = confirm("¿Estás seguro que quieres eliminar esta fila?\nAl borrar la fila también eliminarás cualquier información de este alumno referente al curso actual");
    if (r == true) {
	$(row).parents("tr").remove();
	//BORRAR REGISTRO DE LA TABLA CALIFICACION Y DE LAS ARRAYS DE ESE ALUMNO Y AÑO (No se borra info del alumno, pero no sé que hacer ocn ella)
    }
}
function calcularAlertas(){
    //BUCLE QUE SELECCIONE TODOS LOS ALUMNOS Y LLAME A CALCULAR ALERTA.JSP que a su vez interactuará con Modelo matemático
}

function saveRow(row){
    var txt;
    var r = confirm("¿Estás seguro que quieres guardar los datos");
    if (r == true) {
	//Se guardan los datos referentes a esa fila
    }
}



function seleccionCursoActual(){   
    var xmlhttp = new XMLHttpRequest();

////Set selected
    var seleccionado = $("#comboAsignaturas").val();
    var asig_actual = {
	    asig: seleccionado,
	    curso : obtenerCursoActual()
    }
    if(seleccionado =>1){
	xmlhttp.open("POST","../jsp/TablaCursoActual.jsp");
	xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlhttp.onreadystatechange = function(){
	    if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
		$("#divtablaCursoActual").html(xmlhttp.responseText);	
		$("#p_actual").text("Curso: " + obtenerCursoActual());
		//calcularAlertas();
	    }		
	}
	xmlhttp.send("asig_actual="+JSON.stringify(asig_actual));
    }
}


function seleccionHistorico(){   
    var xmlhttp = new XMLHttpRequest();

////Set selected
    var seleccionado = $("#comboAsignaturas").val();
    if(seleccionado =>1){
	xmlhttp.open("POST","../jsp/CargaTablaHistoricos.jsp");
	xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlhttp.onreadystatechange = function(){
	    if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
		$("#divtablaHistorico").html(xmlhttp.responseText);	
		$("#p_historico").empty();
	    }		
	}
	xmlhttp.send("asignatura="+seleccionado);
    }
}
function mostrarAsigElegida(asignatura) {

    var div = document.getElementById("tituloAsignatura");
    div.style.display ='';
    $('br').remove();
    document.getElementById("asigElegida").innerHTML = "Asignatura elegida: " + asignatura;

}

function obtenerCursoActual(){
    var mes =new Date().getMonth();
    var year = new Date().getFullYear();
    var curso = "";
    if((mes >= 0) && (mes <8)){
	curso = (year-1).toString() + " - " + year.toString();
    }else{
	curso = year.toString +" - " + (year + 1).toString();
    }
    return curso;
}
