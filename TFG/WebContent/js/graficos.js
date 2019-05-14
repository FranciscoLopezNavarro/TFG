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
    calcularGraficosPruebas();
}

function calcularGraficoCursoActual(){
    
	// Create chart instance
	var chart = am4core.create("grafico_curso_actual", am4charts.PieChart);

	// Add data
	chart.data = [ {
	  "country": "Lithuania",
	  "litres": 501.9
	}, {
	  "country": "Czech Republic",
	  "litres": 301.9
	}, {
	  "country": "Ireland",
	  "litres": 201.1
	}, {
	  "country": "Germany",
	  "litres": 165.8
	}, {
	  "country": "Australia",
	  "litres": 139.9
	}, {
	  "country": "Austria",
	  "litres": 128.3
	}, {
	  "country": "UK",
	  "litres": 99
	}, {
	  "country": "Belgium",
	  "litres": 60
	}, {
	  "country": "The Netherlands",
	  "litres": 50
	} ];

	// Add and configure Series
	var pieSeries = chart.series.push(new am4charts.PieSeries());
	pieSeries.dataFields.value = "litres";
	pieSeries.dataFields.category = "country";
	pieSeries.slices.template.stroke = am4core.color("#fff");
	pieSeries.slices.template.strokeWidth = 2;
	pieSeries.slices.template.strokeOpacity = 1;

	// This creates initial animation
	pieSeries.hiddenState.properties.opacity = 1;
	pieSeries.hiddenState.properties.endAngle = -90;
	pieSeries.hiddenState.properties.startAngle = -90;

	 // end am4core.ready()
}
function calcularGraficoHistorico(){
    var chart = am4core.create("grafico_historico", am4charts.XYChart);

 // Add data
 chart.data = [{
   "country": "USA",
   "visits": 2025
 }, {
   "country": "China",
   "visits": 1882
 }, {
   "country": "Japan",
   "visits": 1809
 }, {
   "country": "Germany",
   "visits": 1322
 }, {
   "country": "UK",
   "visits": 1122
 }, {
   "country": "France",
   "visits": 1114
 }, {
   "country": "India",
   "visits": 984
 }, {
   "country": "Spain",
   "visits": 711
 }, {
   "country": "Netherlands",
   "visits": 665
 }, {
   "country": "Russia",
   "visits": 580
 }, {
   "country": "South Korea",
   "visits": 443
 }, {
   "country": "Canada",
   "visits": 441
 }, {
   "country": "Brazil",
   "visits": 395
 }];

 // Create axes

 var categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
 categoryAxis.dataFields.category = "country";
 categoryAxis.renderer.grid.template.location = 0;
 categoryAxis.renderer.minGridDistance = 30;

 categoryAxis.renderer.labels.template.adapter.add("dy", function(dy, target) {
   if (target.dataItem && target.dataItem.index & 2 == 2) {
     return dy + 25;
   }
   return dy;
 });

 var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());

 // Create series
 var series = chart.series.push(new am4charts.ColumnSeries());
 series.dataFields.valueY = "visits";
 series.dataFields.categoryX = "country";
 series.name = "Visits";
 series.columns.template.tooltipText = "{categoryX}: [bold]{valueY}[/]";
 series.columns.template.fillOpacity = .8;

 var columnTemplate = series.columns.template;
 columnTemplate.strokeWidth = 2;
 columnTemplate.strokeOpacity = 1;

}