"use strict";

function seleccion() {

    seleccionHistorico();
    seleccionCursoActual();
    mostrarAsigElegida($("#comboAsignaturas :selected").text());
}

function addRow()
{
    if($("#tablaCursoActual tbody tr").length> 0){
	var rowToAdd = $("#tablaCursoActual tbody tr:last");

	if($("#tablaCursoActual tbody .alumno:last").html() === ""){
	    $("#tablaCursoActual tbody").append(rowToAdd.clone());
	}else{
	    $("#tablaCursoActual tbody tr:last").after(rowToAdd.clone());
	    $("#tablaCursoActual tbody tr:last").find('td').each(function(){
		if($(this).is(".alumno") || $(this).is(".nota")){
		    $(this).text('');
		}
	    });  
	}

    }else{
	crearFilaInicial();
    }

}

function crearFilaInicial()
{
    var fila = "<tr></tr>";
    var celdaAlumno = "<td contenteditable='true' class='alumno'></td>";
    var celdaNotas = "<td contenteditable='true' class='nota'></td>";
    var celdasGenericas =  "<td><div class='progress'><div class='progress-bar progress-bar-striped' style='width: 30%'>30%</div></div></td>" +
    "<td><a><i class='fa fa-save' onclick='saveRow(this)'></i></a>" +
    "<a><i class='fa fa-trash' onclick='deleteRow(this)'></i></a></td>";

    $("#tablaCursoActual tbody").append(fila);

    $(celdaAlumno).appendTo("#tablaCursoActual tbody tr");
    $("#tablaCursoActual thead").find(".header_prueba").each(function(){
	$(celdaNotas).appendTo("#tablaCursoActual tbody tr") 
    });
    $(celdasGenericas).appendTo("#tablaCursoActual tbody tr");

}


function deleteRow(ref){
    var txt;
    var fila = $(ref).parents("tr");
    var alumno =  $(fila).find(".alumno").html();

    if(alumno != ""){
	var r = confirm("¿Estás seguro que quieres eliminar esta fila?\n Al borrar la fila también eliminarás cualquier información de este alumno referente al curso actual");
	if( r == true ) {
	    fila.remove();
	    var json = {alumno: alumno}
	    var xmlhttp = new XMLHttpRequest();
	    xmlhttp.open("POST","../jsp/eliminarFilas.jsp");
	    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	    xmlhttp.onreadystatechange = function(){
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
		    //Comportamiento del html
		    //calcularAlertas();
		    alert("alumno borrado con exito");
		    //$('#tablaCursoActual').DataTable().ajax.reload();
		}		
	    }
	    xmlhttp.send("delete="+JSON.stringify(json));
	    return true;
	}
    }
    fila.remove();
}
//BORRAR REGISTRO DE LA TABLA CALIFICACION Y DE LAS ARRAYS DE ESE ALUMNO Y AÑO (No se borra info del alumno, pero no sé que hacer ocn ella)
function calcularAlertas(){
    // Loop through grabbing everything

    $("#tablaCursoActual").find("tbody tr").each(function () {
	var alumno = $(this).find(".alumno").html();
	var json ={
		alumno: alumno,
		asignatura: obtenerAsignatura(),
		curso: obtenerCursoActual()
	}
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.open("POST","../jsp/calculoAlerta.jsp");
	xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlhttp.onreadystatechange = function(){
	    if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
		var width = JSON.parse(xmlhttp.responseText);
		//poner anchura a this barrita
		 $('.progress-bar').css('width', width.riesgo+'%').attr('aria-valuenow', width.riesgo);
	    }
	}

	xmlhttp.send("calculo_alerta="+JSON.stringify(json));
    });

}

function saveRow(ref){
    var json ={};
    var fila = $(ref).parents("tr");
    var $table = $("#tablaCursoActual"),
    pruebas = [];
    $table.find(".header_prueba").each(function () {
	pruebas.push($(this).html());
    });
    var alumno =  $(fila).find(".alumno").html();
    var row = {};
    $(fila).find(".nota").each(function (i) {
	var key = pruebas[i],
	value = $(this).html();
	row[key] = value;
    });

    guardarFila(row, alumno);
}


function guardarFila(row, alumno){
    var json = {
	    alumno: alumno,
	    pruebas :[row], 
	    asignatura :obtenerAsignatura(),
	    curso: obtenerCursoActual()
    } 
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST","../jsp/guardarFilas.jsp");
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function(){
	if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
	    //Comportamiento del html
	    calcularAlertas();
	}		
    }
    xmlhttp.send("save="+JSON.stringify(json));
}

function seleccionCursoActual(){   
    var xmlhttp = new XMLHttpRequest();
    var seleccionado = obtenerAsignatura();
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
		calcularAlertas();
	    }		
	}
	xmlhttp.send("asig_actual="+JSON.stringify(asig_actual));
    }
}


function seleccionHistorico(){   
    var xmlhttp = new XMLHttpRequest();
    var seleccionado = obtenerAsignatura();
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

function obtenerAsignatura(){
    var asignatura =  $("#comboAsignaturas").val();
    return asignatura;
}

$(document).on( "click", ".nota", function() {
    limpiartabla();
    var header =  $(this).closest('table').find('th').eq($(this).index()).html();
    var json ={
	    prueba: header,
	    asignatura: obtenerAsignatura()
    }
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST","../jsp/relacionesPruebas.jsp");
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function(){
	if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
	    var respuesta = JSON.parse(xmlhttp.responseText);
	    for (var i = 0; i < respuesta.Pruebas.length; i++) {
		var nombre_prueba = respuesta.Pruebas[i];
		var indice;
		$("#tablaCursoActual").find(".header_prueba").each(function () {
		    if($(this).html() == nombre_prueba){
			indice = $(this).index();
		    }
		});

		$("#tablaCursoActual tr .nota").each(function() {
		    if($(this).index() == indice){
			$(this).css('background-color', 'rgba(201, 76, 76, 0.1)');	
		    }
		});
	    }
	}
    }
    xmlhttp.send("header="+JSON.stringify(json));
});
function limpiartabla(){
    $("#tablaCursoActual tr .nota").css('background-color', 'initial');
}
