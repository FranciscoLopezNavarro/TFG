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
    calcularGraficoCursoActual();
    calcularGraficoHistorico();
    calcularGraficoPruebas();
}

function calcularGraficoCursoActual(){
$("#info_curso_actual").text("Alumnos en riesgo, curso: " + obtenerCursoActual());
    
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
    $("#info_historico").text("Aprobados por año");
    //Creamos el grafico e insertamos los valores
    var chart = am4core.create("grafico_historico", am4charts.XYChart);

    var years = ['2017', '2018', '2019','2020'];
    var aprobados = ['35','60','50','60'];
    var data = [];
    var i = 0;

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



	var aprobados_prueba = "37";
	var aprobados_prueba_y_asig = "14";
	var data = [];
//	var i = 0;
//
//	for (i = 0; i <= years.length; i++) {
//	    data.push(
//		    { prueba: div,
//			aprobados: aprobados[i]});
//	}

	data = [{
	    "prueba": div,
	    "aprobados": aprobados_prueba
	},{
	    "prueba": div + " + asignatura",
	    "aprobados": aprobados_prueba_y_asig
	}];
	
	var chart = am4core.create(div, am4charts.XYChart);
	chart.data = data;
	
	var categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
	categoryAxis.dataFields.category = "prueba";

	var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
	valueAxis.title.text = "Aprobados";
	
	var series = chart.series.push(new am4charts.ColumnSeries());
	series.name = "Aprobados";
	series.columns.template.fill = am4core.color("#104547"); 
	series.dataFields.valueY = "aprobados";
	series.dataFields.categoryX = "prueba";
	series.columns.template.width = am4core.percent(10);
	
	chart.paddingRight = am4core.percent(10);
    });
}