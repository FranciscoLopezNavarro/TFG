"use strict";
var titulo = document.getElementsByTagName("title")[0].innerHTML;
var MenuManager={
	 //HTML
	  inicio : '<nav class="navbar navbar-inverse"><div class="container-fluid">',
	  tituloBar : '<div class="navbar-header"><a class="navbar-brand">'+titulo+'</a></div>',
	  inicioBar :  '<ul class="nav navbar-nav"><li class="active"><a href="../html/index.html">Inicio</a></li></ul>',
	  inicioDerecha :  '<ul class="nav navbar-nav navbar-right">',
	  registrarBar : '<li><a href="../registrar/registrar.html"><span class="glyphicon glyphicon-user"></span> Registrarse</a></li>',
	  indetificarseBar : '<li><a href="../html/login.html"><span class="glyphicon glyphicon-log-in"></span> Identificarse</a></li>',
	  desconectarseBar : '<li><a href="#" onclick="ConnectionManager.logout();return false;" ><span class="glyphicon glyphicon-log-in"></span> Desconectarse</a></li>',
	  desconectarseGoogle:   '<li><a href="#" onclick="ConnectionManager.logoutGoogle();return false;" ><span class="glyphicon glyphicon-log-in"></span> Desconectarse</a></li>',
	  finalDerecha : '</ul>',
	  final : '</div></nav>', 
	  settings : '<ul class="nav navbar-nav"><li class="active"><a href="../settings/settings.html">Settings</a></li></ul>',
	  
	  LoadMenuNoConnection: function(){
		 var finalMenu = this.inicio+
		 					this.tituloBar+
		 					this.inicioBar+
			 					this.inicioDerecha+
				 					this.registrarBar+
				 					this.indetificarseBar+
			 					this.finalDerecha+
		 					this.final;
		 $("#menu").append(finalMenu);
	  },
	  LoadMenuWithConnection: function(){
			 var finalMenu = this.inicio+
			 					this.tituloBar+
			 					this.inicioBar+
			 						this.inicioDerecha+
			 						this.settings+
			 						'<li><a>Conectado:</a></li>'+
			 						'<li><button class="btn btn-danger navbar-btn">'+nick+'</button></li>'+
			 							this.desconectarseBar+
			 						this.finalDerecha+
			 					this.final;
			 $("#menu").append(finalMenu);
		  }
 };