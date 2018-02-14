var websocket;
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojinputtext'],
 function(oj, ko, $, app) { 
 function LoginViewModel() { 
    var self=this;
 	self.nombreDeUsuario=ko.observable(sessionStorage.jugador);
   	//self.pwd1=ko.observable("1234");
    self.estado= ko.observable(" ");

    this.recuperar=function(){
   		var p={
   			tipo: "Recuperar",
   			jugador: this.nombreDeUsuario(),
   			//pwd: this.pwd1()
   		};
   	$.ajax({
      type: "POST",
      url: "http://127.0.0.1:8080/ServidorJuego/Recuperar.jsp",
      data: "p="+ JSON.stringify(p),
    }).done(function(respuesta){
    	//var mensaje = JSON.parse(respuesta);
      if(respuesta.tipo=="OK") {
      	alert(respuesta.pwd)
     //sessionStorage.jugador=self.nombreDeUsuario();
      } else{
      	alert("Recuperar");
      	//var mensaje = JSON.parse(respuesta.data);
      	//var pwd=respuesta.pwd;
        //alert(respuesta.pwd);
      }
      
    });
    }     
  }
    return new LoginViewModel();
  });