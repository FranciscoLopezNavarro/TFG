"use strict";

function seleccion() {

    mostrarAsigElegida($("#comboAsignaturas :selected").text());
    calculoGraficos();
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

function mostrarAsigElegida(asignatura) {

    var div = document.getElementById("tituloAsignatura");
    div.style.display ='';
    $('br').remove();
    document.getElementById("asigElegida").innerHTML = "Asignatura elegida: " + asignatura;

    var graficos = document.getElementById("graficos");
    graficos.style.display ='';
}

function calculoGraficos(){
    //  calcularGraficoArbol();
    calcularGraficoCursoActual();
    calcularGraficoHistorico();
    calcularGraficoPruebas();
}
function calcularGraficoArbol(){


//  am4core.useTheme(am4themes_animated);
//  var chart = am4core.create("tree-map", am4plugins_forceDirected.ForceDirectedTree);
//  var networkSeries = chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries())
//  var data = [];
//  var children = [];
//  var condicion= true;

//  $(".tab-contents").each(function(){
//  var div = $(this).prop("id");
//  var aprobados_prueba = aprobadosPrueba(div);

//  var json ={
//  prueba: div,
//  asignatura: obtenerAsignatura()
//  }
//  var xmlhttp = new XMLHttpRequest();
//  xmlhttp.open("POST","../jsp/relacionesPruebas.jsp");
//  xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
//  xmlhttp.onreadystatechange = function(){
//  if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
//  var respuesta = JSON.parse(xmlhttp.responseText);
//  for (var i = 0; i < respuesta.Pruebas.length; i++) {
//  var nombre_prueba = respuesta.Pruebas[i];
//  children.push({
//  name: nombre_prueba, 
//  value: aprobadosPrueba(nombre_prueba)
//  })
//  }
//  }
//  }
//  current[splitted[i]] = {};
//  current = current[splitted[i]];

//  if(condicion){
//  data.push({
//  name: div, value: aprobados_prueba,
//  children: calcularHijos()
//  });
//  }else{
//  data.push({
//  name: div, value: aprobados_prueba
//  });
//  }
//  }

//  data = [{
//  name: 'Flora',
//  children: [{
//  name: div, value: aprobados_prueba
//  }, {
//  name: 'Floral',
//  children: [{
//  name: 'Chamomile', value: 1
//  }, {
//  name: 'Rose', value: 1
//  }, {
//  name: 'Jasmine', value: 1
//  }]
//  }]
//  }, {
//  name: 'Fruity',
//  children: [{
//  name: 'Berry',
//  children: [{
//  name: 'Blackberry', value: 1
//  }, {
//  name: 'Raspberry', value: 1
//  }, {
//  name: 'Blueberry', value: 1
//  }, {
//  name: 'Strawberry', value: 1
//  }]
//  }]
//  }];
//  });

//  networkSeries.data = data;
//  networkSeries.dataFields.linkWith = "linkWith";
//  networkSeries.dataFields.name = "name";
//  networkSeries.dataFields.id = "name";
//  networkSeries.dataFields.value = "value";
//  networkSeries.dataFields.children = "children";

//  networkSeries.nodes.template.tooltipText = "{name}";
//  networkSeries.nodes.template.fillOpacity = 1;

//  networkSeries.nodes.template.label.text = "{name}"
//  networkSeries.fontSize = 8;
//  networkSeries.maxLevels = 2;
//  networkSeries.maxRadius = am4core.percent(6);
//  networkSeries.manyBodyStrength = -16;
//  networkSeries.nodes.template.label.hideOversized = true;
//  networkSeries.nodes.template.label.truncate = true;
//  chart.legend = new am4charts.Legend();
}

function calcularGraficoCursoActual(){
    $("#h3cursoActual").text("Porcentaje (%) de alumnos en riesgo. Curso: " + obtenerCursoActual());

    //Se crea el grafico
    var total_alumnos = 70;
    var alumnos_riesgo = 10;
    var chart = am4core.create("grafico_curso_actual", am4charts.PieChart);

    // Se añaden los datos
    chart.data = [ {
	"alumnos": "En riesgo",
	"numero": alumnos_riesgo
    }, {
	"alumnos": "Sin riesgo",
	"numero": (total_alumnos - alumnos_riesgo)
    }];

    var pieSeries = chart.series.push(new am4charts.PieSeries());
    pieSeries.dataFields.value = "numero";
    pieSeries.dataFields.category = "alumnos";


    //Coloress
    pieSeries.colors.list = [
	am4core.color("#ce5c5c"),
	am4core.color("#438BCA")
	];

    //Personalización del grafico
    pieSeries.labels.template.text = "{category}: {value.value}";
    pieSeries.slices.template.stroke = am4core.color("#fff");
    pieSeries.slices.template.strokeWidth = 2;
    pieSeries.slices.template.strokeOpacity = 1;

    // This creates initial animation
    pieSeries.hiddenState.properties.opacity = 1;
    pieSeries.hiddenState.properties.endAngle = -90;
    pieSeries.hiddenState.properties.startAngle = -90;

    // Posicionamiento
    chart.paddingTop = am4core.percent(10);


    // Leyenda
    chart.legend = new am4charts.Legend();
    chart.legend.labels.template.text = "Alumnos: [bold {color}]{name}[/]";
    chart.legend.valueLabels.template.text = "{value.value}";
    chart.legend.useDefaultMarker = true;

    var marker = chart.legend.markers.template.children.getIndex(0);
    marker.cornerRadius(12, 12, 12, 12);
    marker.strokeWidth = 2;
    marker.strokeOpacity = 1;
    marker.stroke = am4core.color("#ccc");

}
function calcularGraficoHistorico(){
    $("#h3historico").text("Número de alumnos aprobados anualmente");
    //Creamos el grafico e insertamos los valores
    var chart = am4core.create("grafico_historico", am4charts.XYChart);

    var years = ['2017', '2018', '2019','2020'];
    var aprobados = ['35','60','50','60'];
    var data = [];
    var i = 0;

    var chicha = aprobadosCurso();

    for (i = 0; i <= years.length; i++) {
	data.push(
		{ year: years[i],
		    aprobados: aprobados[i]});
    }

    chart.data = data;
    chart.cursor= new am4charts.XYCursor();
    chart.responsive.enabled = true;
    // Create axes

    var xAxis = chart.xAxes.push(new am4charts.ValueAxis());
    var yAxis = chart.yAxes.push(new am4charts.ValueAxis());

    xAxis.title.text = "Año";
    yAxis.title.text = "Aprobados";
    xAxis.dataFields.category = "year";


    // Create series
    var series1 = chart.series.push(new am4charts.LineSeries());
    series1.dataFields.valueX = "year";
    series1.dataFields.valueY = "aprobados";
    series1.strokeWidth = 2;

    var bullet1 = series1.bullets.push(new am4charts.CircleBullet());
    series1.heatRules.push({
	target: bullet1.circle,
	min: 10,
	max: 20,
	property: "radius"
    });

    bullet1.tooltipText = "{year}: [bold]{aprobados}[/]";

}
function calcularGraficoPruebas(){
    $(".tab-contents").each(function(){
	var div = $(this).prop("id");

	var aprobados_prueba_general = aprobadosPrueba(div);
	var aprobados_prueba_asig_general = "60";

	var aprobados_prueba_actual = aprobadosPruebaActual(div);
	var aprobados_prueba_asig_actual = "60";
	var data = [];

	data = [{
	    "prueba": div,
	    "aprobados_general": aprobados_prueba_general,
	    "aprobados_curso_actual": aprobados_prueba_actual
	},{
	    "prueba": div + " + asignatura",
	    "aprobados_general": aprobados_prueba_asig_general,
	    "aprobados_curso_actual":aprobados_prueba_asig_actual
	}];

	var chart = am4core.create(div, am4charts.XYChart);
	chart.data = data;

	var categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
	categoryAxis.dataFields.category = "prueba";

	var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
	valueAxis.title.text = " % Aprobados";
	valueAxis.min = 0;
	valueAxis.max = 100;


	categoryAxis.renderer.cellStartLocation = 0.1;
	categoryAxis.renderer.cellEndLocation = 0.9;
	// Create series
	function createSeries(field, name, color) {
	    var series = chart.series.push(new am4charts.ColumnSeries());
	    series.dataFields.valueY = field;
	    series.dataFields.categoryX = "prueba";
	    series.name = name;
	    series.columns.template.fill = am4core.color(color);
	    series.fillOpacity = 0.95;
	    series.columns.template.tooltipText = "{name}: [bold]{valueY}[/]";
	    series.columns.template.width = am4core.percent(40);

	}

	createSeries("aprobados_general", "% Aprobados histórico","#438bca");
	createSeries("aprobados_curso_actual", "% Aprobados curso actual","#f6ae2d");

	chart.legend = new am4charts.Legend();
	chart.legend.labels.template.text = "{name} [bold]{valueY}[/]";
	chart.legend.valueLabels.template.text = "{value.value}";
	chart.paddingRight = am4core.percent(10);

    });
}

function aprobadosPrueba(tituloPrueba){
    var aprobados;
    var asignatura = obtenerAsignatura();
    var json = {
	    prueba: tituloPrueba,
	    asignatura : asignatura
    }
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST","../jsp/aprobadosPrueba.jsp", false);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function(){
	if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
	    var respuesta = JSON.parse(xmlhttp.responseText);
	    aprobados = parseFloat((respuesta.aprobados).replace(',', '.')).toFixed(2);
	}		
    }
    xmlhttp.send("datos_prueba="+JSON.stringify(json));
    return aprobados;
}

function aprobadosPruebaActual(tituloPrueba){
    var aprobados;
    var asignatura = obtenerAsignatura();
    var json = {
	    prueba: tituloPrueba,
	    asignatura : asignatura,
	    curso: obtenerCursoActual()
    }
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST","../jsp/aprobadosPruebaActual.jsp", false);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function(){
	if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
	    var respuesta = JSON.parse(xmlhttp.responseText);
	    aprobados = parseFloat((respuesta.aprobados).replace(',', '.')).toFixed(2);
	}		
    }
    xmlhttp.send("datos_prueba="+JSON.stringify(json));
    return aprobados;
}
function aprobadosCurso(){
    var respuesta;
    var asignatura = obtenerAsignatura();
    var json = {
	    asignatura : asignatura
    }
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST","../jsp/aprobadosCurso.jsp", false);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function(){
	if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
	    respuesta = JSON.parse(xmlhttp.responseText);

	}		
    }
    xmlhttp.send("datos_prueba="+JSON.stringify(json));
    return respuesta;
}