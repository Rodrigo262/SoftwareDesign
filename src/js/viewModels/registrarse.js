define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojinputtext'],
 function(oj, ko, $, app) { 
 function LoginViewModel() { 
 	this.nombreDeUsuario=ko.observable("MANUEL");
   	this.pwd1=ko.observable("1234");
   	this.pwd2=ko.observable("1234");
   	this.registrarse=function(){
   		var p={
   			tipo: "registrarse",
   			jugador: this.nombreDeUsuario(),
   			pwd1: this.pwd1(),
   			pwd2:this.pwd2()
   		};
   	$.ajax({
      type: "POST",
      url: "http://127.0.0.1:8080/ServidorJuego/Registrarse.jsp",
      data: "p="+ JSON.stringify(p),
    }).done(function(respuesta){
      if(respuesta.tipo=="OK") {
        window.location="index.html?root"
      } else
        alert(respuesta.texto);
    });
    }   	
	}
    return new LoginViewModel();
  });