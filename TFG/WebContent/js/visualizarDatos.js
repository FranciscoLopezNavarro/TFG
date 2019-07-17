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
		else if($(this).is(".alerta")){
		    $(this).find(".progress-bar").css({width : 0 + '%'});
		    $(this).find(".grado_riesgo").text(' ');
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
    var celdaNotas = "<td contenteditable='true' class='nota'>" +
    "<div class='progress'><div class='progress-bar progress-bar-success' style='width: 0%'><span class='sr-only'>" +
    "</span></div><div class='progress-bar progress-bar-warning' style='width: 0%'><span class='sr-only'></span></div>" +
    "<div class='progress-bar progress-bar-danger' style='width: 0%'><span class='sr-only'></span></div></div></td>";
    
    var celdasGenericas =  "<td><div class='progress'><div class='progress-bar progress-bar-success' style='width: 0%'><span class='sr-only'>" +
    "</span></div><div class='progress-bar progress-bar-warning' style='width: 0%'><span class='sr-only'></span></div>" +
    "<div class='progress-bar progress-bar-danger' style='width: 0%'><span class='sr-only'></span></div></div><div class='grado_riesgo'></div></td>" +
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
	    var json = {alumno: alumno,
		    year: obtenerCursoActual()}
	    var xmlhttp = new XMLHttpRequest();
	    xmlhttp.open("POST","../jsp/eliminarFilas.jsp");
	    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	    xmlhttp.onreadystatechange = function(){
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
		    alert("alumno borrado con exito");
		    calcularAlertas();
		}		
	    }
	    xmlhttp.send("delete="+JSON.stringify(json));
	    fila.remove();
	    return true;
	}
    }
}

function calcularAlertas(){

    $("#tablaCursoActual tbody tr").each(function () {
	var filaActual = $(this);
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
		var riesgo = parseFloat((width.riesgo).replace(',', '.')).toFixed(2);
		var celda_alerta = filaActual.find(".alerta");

		if(riesgo >= 60 ){
		        celda_alerta.find(".progress-bar.progress-bar-danger").css({width : '0%'});
		        celda_alerta.find(".progress-bar.progress-bar-warning").css({width : '0%'});
		        celda_alerta.find(".progress-bar.progress-bar-success").css({width : '0%'});
		        
		        celda_alerta.find(".progress-bar.progress-bar-danger").css({width : riesgo + '%'});
		        celda_alerta.find(".grado_riesgo").text(riesgo + '%');
		    }else if (riesgo < 60 && riesgo >=40 ){
		        celda_alerta.find(".progress-bar.progress-bar-danger").css({width : '0%'});
		        celda_alerta.find(".progress-bar.progress-bar-warning").css({width : '0%'});
		        celda_alerta.find(".progress-bar.progress-bar-success").css({width : '0%'});
		        
		        celda_alerta.find(".progress-bar.progress-bar-warning").css({width : riesgo + '%'});
		        celda_alerta.find(".grado_riesgo").text(riesgo + '%');
		    }else{
		        celda_alerta.find(".progress-bar.progress-bar-danger").css({width : '0%'});
		        celda_alerta.find(".progress-bar.progress-bar-warning").css({width : '0%'});
		        celda_alerta.find(".progress-bar.progress-bar-success").css({width : '0%'});
		        
		        celda_alerta.find(".progress-bar.progress-bar-success").css({width : riesgo + '%'});
		        celda_alerta.find(".grado_riesgo").text(riesgo + '%');
		    }
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
	pruebas.push($(this).contents().first().text().trim());
    });
    var alumno =  $(fila).find(".alumno").html();
    var row = {};
    
    $(fila).find(".nota").each(function (i) {
	var key = pruebas[i],
	value = $(this).contents().first().text();
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

$(function() {
    $('#tab-button li').find('a').on('click', function(e) {
	if($(this).text() == "Curso actual"){
	    if($("#comboAsignaturas").val())
		seleccionCursoActual();
	}

	if($(this).text() == "Histórico"){
	    if($("#comboAsignaturas").val())
		seleccionHistorico();
	}

    });
});
$(document).on( "click", ".nota", function() {
    limpiartabla();
    var header =  $(this).closest('table').find('th').eq($(this).index()).contents().first().text().trim();
    var json ={
	    prueba: header,
	    asignatura: obtenerAsignatura()
    }

    infoPrueba(json);
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
		    if($(this).contents().first().text().trim() == nombre_prueba){
			indice = $(this).index();
		    }
		});

		$("#tablaCursoActual tr .nota").each(function() {
		    var celda_nota = $(this)
		    if(celda_nota.index() == indice){
			celda_nota.css({'background-color': 'rgba(201, 76, 76, 0.1)'});

			//LLAMADA AL METODO CALCULAR PROBABILIDAD(PRUEBA, ALUMNO)
			celda_nota.find(".progress").css({'display': '' ,
			    'width' :'300%', 
			    'margin-left': '-100%', 
			    'margin-bottom': '0%',
			});
			
			var riesgo = Math.floor((Math.random() * 100) + 1);
			if(riesgo >= 60 ){
			        celda_nota.find(".progress-bar.progress-bar-danger").css({width : '0%'});
			        celda_nota.find(".progress-bar.progress-bar-warning").css({width : '0%'});
			        celda_nota.find(".progress-bar.progress-bar-success").css({width : '0%'});
			        
			        celda_nota.find(".progress-bar.progress-bar-danger").css({width : riesgo + '%'});
			        celda_nota.find(".grado_riesgo").text(riesgo + '%');
			    }else if (riesgo < 60 && riesgo >=40 ){
				celda_nota.find(".progress-bar.progress-bar-danger").css({width : '0%'});
				celda_nota.find(".progress-bar.progress-bar-warning").css({width : '0%'});
				celda_nota.find(".progress-bar.progress-bar-success").css({width : '0%'});
			        
				celda_nota.find(".progress-bar.progress-bar-warning").css({width : riesgo + '%'});
				celda_nota.find(".grado_riesgo").text(riesgo + '%');
			    }else{
				celda_nota.find(".progress-bar.progress-bar-danger").css({width : '0%'});
				celda_nota.find(".progress-bar.progress-bar-warning").css({width : '0%'});
				celda_nota.find(".progress-bar.progress-bar-success").css({width : '0%'});
			        
				celda_nota.find(".progress-bar.progress-bar-success").css({width : riesgo + '%'});
				celda_nota.find(".grado_riesgo").text(riesgo + '%');
			    }
		    }
		});
	    }
	}
    }
    xmlhttp.send("header="+JSON.stringify(json));
});

function infoPrueba(json){
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST","../jsp/infoPrueba.jsp");
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function(){
	if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
	    var respuesta = JSON.parse(xmlhttp.responseText);
	    //  parseFloat((width.riesgo).replace(',', '.')).toFixed(2)
	    var nota_min = parseFloat(respuesta.Pruebas[0]);
	    var nota_corte = parseFloat(respuesta.Pruebas[1]);
	    var nota_max = parseFloat(+respuesta.Pruebas[2]);
	    $("#info_prueba").text("Nota mínima: " + nota_min + " Nota de corte: "+ nota_corte +" Nota máxima: " + nota_max);
	}
    }
    xmlhttp.send("header="+JSON.stringify(json));
}
function limpiartabla(){
    $("#tablaCursoActual tr .nota").css('background-color', 'initial');
    $("#tablaCursoActual tr .nota").find(".progress").css({'display': 'none'});
    $("#tablaCursoActual tr .nota").find(".progress-bar.progress-bar-striped.bg-success.progress-bar-animated").css({'width': '0%'});
    
}
