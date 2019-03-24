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

    // make a copy of an existing row. We choose the last row of table
    var rowToAdd = $("#tablaCursoActual tbody tr:last").clone();
    // empty cells of this row
    $(rowToAdd).find('td').each(function(){
	if($(this).is(".alumno") || $(this).is(".nota")){
	    $(this).text('');
	}
    });
    //add it to table
    $("#tablaCursoActual tbody").append(rowToAdd);

}
function deleteRow(row){
    var txt;
    var r = confirm("¿Estás seguro que quieres eliminar esta fila?\nAl borrar la fila también eliminarás cualquier información de este alumno referente al curso actual");
    if( r == true ) {
	
	    $(row).parents("tr").remove();
	
	return true;
    } else {
	return false;
    }
}

//BORRAR REGISTRO DE LA TABLA CALIFICACION Y DE LAS ARRAYS DE ESE ALUMNO Y AÑO (No se borra info del alumno, pero no sé que hacer ocn ella)
function calcularAlertas(){
    // Loop through grabbing everything
    var $table = $("#tablaCursoActual"),
    rows = [],
    pruebas = [];

    $table.find(".header_prueba").each(function () {
	pruebas.push($(this).html());
    });

    $table.find("tbody tr").each(function () {
	var row = {};
	$(this).find(".nota").each(function (i) {
	    var key = pruebas[i],
	    value = $(this).html();
	    row[key] = value;
	});
	rows.push(row);
	//AQUI HEMOS RECORRIDO TODA LA TABLA, AL RECORRERLA SE VA RECALCULANDO LA ALERTA PARA CADA FILA
    });
    console.log(rows);
}


function saveRow(alumno){
    var fila = $(a).parents("tr");
    var $table = $("#tablaCursoActual"),
    rows = [],
    pruebas = [];
    $table.find(".header_prueba").each(function () {
	pruebas.push($(this).html());
    });
    
  var row = {};
    $(fila).find(".nota").each(function (i) {
	var key = pruebas[i],
	value = $(this).html();
	row[key] = value;
    });
    rows.push(row);
    
    //FALTA PILLAR EL ALUMNO Y CONSTRUIR UN JSON CON el
    console.log(rows);
}



//Let's put this in the object like you want and convert to JSON (Note: jQuery will also do this for you on the Ajax request)

//var Tx;
//var notas = [];
//function saveRow(row){
//var txt;
//var r = confirm("¿Estás seguro que quieres guardar los datos");
//if (r == true) {
//Tx = $("#alumno_"+row+" .nota");
//for(var i = 0;i<Tx.length;i++){
//notas.push([row,parseFloat(Tx[i].textContent), "T"+(i+1)]);
//}
//console.log(notas)
////Tx representa T1,T2,T3 etc...
////Se guardan los datos referentes a esa fila
//}
//}



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