var websocket;
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojknockout', 'promise',
	'ojs/ojdatagrid', 'ojs/ojarraydatagriddatasource'],
 function(oj, ko, $, app) { /////
    function JuegosViewModel(){

      var self=this;
      self.nombreDeUsuario=ko.observable(sessionStorage.jugador);
      self.estado= ko.observable(" ");

     this.ranking=function(){
     var p={
       tipo: "Ranking",
       jugador:this.nombreDeUsuario()
     };
    
    $.ajax({
      type: "POST",
      url: "http://127.0.0.1:8080/ServidorJuego/Ranking.jsp",
      data: "p="+ JSON.stringify(p),
    }).done(function(respuesta){
      if(respuesta.tipo=="OK") {

      } else
        alert(respuesta.texto);
    });
    }
  
}
    return new JuegosViewModel();
  }
);
